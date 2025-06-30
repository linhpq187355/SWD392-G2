package services;

import daos.OrderDao;
import daos.ProductDAO;
import models.Order;
import models.OrderItemDetail;
import models.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderService {

    private final OrderDao orderDao;
    private final AddressService addressService;
    private final GhnService ghnService;
    private final ProductDAO productDao;
    public OrderService() {
        addressService = new AddressService();
        orderDao = new OrderDao(); // Khởi tạo Dao
        ghnService = new GhnService();
        productDao = new ProductDAO();
    }

    // Lấy tất cả đơn hàng
    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    // Lấy đơn hàng theo statusId
    public List<Order> getOrdersByStatus(int statusId) {
        return orderDao.getOrdersByStatus(statusId);
    }

    // Lấy đơn hàng sắp xếp theo ngày tạo (tăng/giảm dần)
    public List<Order> getOrdersSortedByDate(boolean ascending) {
        return orderDao.getOrdersSortedByDate(ascending);
    }

    // Tìm kiếm đơn hàng theo từ khóa (name, email, address)
    public List<Order> searchOrders(String keyword) {
        return orderDao.searchOrders(keyword);
    }

    // Lấy đơn hàng của 1 customer cụ thể
    public List<Order> getOrdersByCustomer(int customerId) {
        return orderDao.getOrdersByCustomer(customerId);
    }

    // Lấy đơn hàng được giao cho 1 sales cụ thể
    public List<Order> getOrdersAssignedToSales(int salesId) {
        return orderDao.getOrdersBySales(salesId);
    }

    public Order getOrderById(String orderId) {
        Order order = orderDao.getOrderById(orderId);
        List<OrderItemDetail> itemDetails = orderDao.getOrderItemsWithProductInfo(orderId);
        order.setItems(itemDetails); // Gán vào đối tượng Order
        return order;
    }

    public Order getOrderForEdit(String orderId) {
        Order order = orderDao.getOrderById(orderId);
        if (order != null) {
            // Lấy danh sách sản phẩm
            List<OrderItemDetail> itemDetails = orderDao.getOrderItemsWithProductInfo(orderId);
            order.setItems(itemDetails);

            // Phân tách địa chỉ
            addressService.parseFullAddress(order.getReceiveAddress(), order);
        }
        return order;
    }

    /**
     * Kiểm tra xem người dùng có quyền sửa đơn hàng không.
     * @param userId ID người dùng
     * @param role Vai trò người dùng (Sales, Customer)
     * @param orderId ID đơn hàng
     * @return true nếu có quyền, false nếu không.
     */
    public boolean hasPermissionToEdit(int userId, String role, String orderId) {
        Order order = orderDao.getOrderById(orderId);
        if (order == null) return false;

        if ("Customer".equalsIgnoreCase(role)) {
            // Khách hàng chỉ được sửa đơn của chính mình
            return order.getUserId() == userId;
        } else if ("Sales".equalsIgnoreCase(role)) {
            // Sales được sửa đơn hàng được gán cho mình
            // (Cần bổ sung logic kiểm tra trong OrderAssignmentDAO)
            // Tạm thời giả định là sales có quyền với mọi đơn
            return true;
        }
        return false;
    }

    public void updateOrder(Order orderToUpdate, Map<Integer, Integer> quantities) throws Exception {
        // 1. Validate Business Rules
        validateBusinessRules(orderToUpdate, quantities);

        // 2. Lấy thông tin đơn hàng gốc để so sánh
        Order originalOrder = orderDao.getOrderById(orderToUpdate.getOrderId());
        if (originalOrder == null) {
            throw new Exception("Order not found.");
        }

        // 3. Thực hiện cập nhật trong CSDL (sử dụng transaction)
        try {
            // DAO đã được thiết kế để xử lý transaction
            orderDao.updateOrderDetails(orderToUpdate);
            if (quantities != null && !quantities.isEmpty()) {
                orderDao.updateOrderItemQuantities(orderToUpdate.getOrderId(), quantities);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Database update failed. " + e.getMessage());
        }

        // 4. (UC Step 9) Tích hợp GHN nếu có thay đổi liên quan đến vận chuyển
        if (isShippingInfoChanged(originalOrder, orderToUpdate)) {
            try {
                ghnService.updateOrderOnGhn(orderToUpdate);
            } catch (GhnException e) {
                // (UC Exception E9.1) Xử lý khi GHN có lỗi
                System.err.println("GHN Integration Error: " + e.getMessage());
                orderDao.markOrderForManualSync(orderToUpdate.getOrderId(), e.getMessage()); // Đánh dấu đơn hàng cần đồng bộ lại
                // Ném lại lỗi để Controller hiển thị thông báo phù hợp
                throw e;
            }
        }
    }

    private void validateBusinessRules(Order order, Map<Integer, Integer> quantities) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (quantities != null) {
            for (Map.Entry<Integer, Integer> item : quantities.entrySet()) {
                Integer productId = item.getKey();
                int requestedQuantity = item.getValue();

                Product product = getProduct(productId);

                // Kiểm tra sản phẩm có tồn tại không
                if (product == null) {
                    errors.add("Sản phẩm với mã '" + productId + "' không tồn tại.");
                    continue; // Bỏ qua các kiểm tra khác cho sản phẩm này
                }

                // **QUAN TRỌNG: Kiểm tra sản phẩm có đang hoạt động không (`isActive`)**
                if (!product.isActive()) {
                    errors.add("Sản phẩm '" + product.getName() + "' hiện không được kinh doanh.");
                }

                // Kiểm tra tồn kho
                int availableStock = product.getStock();
                if (requestedQuantity > availableStock) {
                    errors.add("Tồn kho không đủ cho sản phẩm '" + product.getName() + "'. Hiện có: " + availableStock + ", Yêu cầu: " + requestedQuantity);
                }
            }
        }

        // b. Validate quyền thay đổi trạng thái (A7.1)
        // Ví dụ: không cho phép chuyển từ "Đã giao" về "Đang xử lý"
        // Cần logic phức tạp hơn ở đây
        // if (!isStatusChangePermitted(originalStatusId, newStatusId)) {
        //     errors.add("Order status change not permitted.");
        // }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private Product getProduct(int productId) {
        return productDao.getProduct(productId);
    }

    private boolean isShippingInfoChanged(Order original, Order updated) {
        if (!original.getReceiveName().equals(updated.getReceiveName())) return true;
        if (!original.getReceivePhone().equals(updated.getReceivePhone())) return true;
        if (!original.getReceiveAddress().equals(updated.getReceiveAddress())) return true;
        // Kiểm tra nếu trạng thái thay đổi là trạng thái liên quan đến GHN
        // if (isShippingStatus(updated.getStatusId())) return true;
        return false;
    }
}


