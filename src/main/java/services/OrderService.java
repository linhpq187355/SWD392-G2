package services;

import daos.OrderDAO;
import daos.ProductDAO;
import models.Order;
import models.OrderItemDetail;
import models.OrderUpdateDTO;
import models.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderService {

    private final OrderDAO orderDao;
    private final AddressService addressService;
    private SettingService settingService;
    private final GhnService ghnService;
    private final ProductDAO productDao;
    public OrderService() {
        addressService = new AddressService();
        orderDao = new OrderDAO(); // Khởi tạo Dao
        ghnService = new GhnService();
        productDao = new ProductDAO();
        settingService = new SettingService();
    }

    public List<Order> getOrdersByCustomer(int customerId) {
        return orderDao.getOrdersByCustomer(customerId);
    }

    public List<Order> getOrdersAssignedToSales(int salesId) {
        return orderDao.getOrdersBySales(salesId);
    }

    public List<Order> getOrdersForUser(int userId, String role) {
        if ("Sales".equalsIgnoreCase(role)) {
            return getOrdersAssignedToSales(userId);
        }
        if ("Customer".equalsIgnoreCase(role)) {
            return getOrdersByCustomer(userId);
        }
        return new ArrayList<>();
    }

    public Order getOrderForEdit(String orderId) {
        Order order = orderDao.getOrderById(orderId);
        if (order != null) {
            List<OrderItemDetail> itemDetails = orderDao.getOrderItemsWithProductInfo(orderId);
            order.setItems(itemDetails);
            addressService.parseFullAddress(order.getReceiveAddress(), order);
        }
        return order;
    }

    public void updateOrder(Order orderToUpdate, Map<Integer, Integer> quantities) throws Exception {
        validateBusinessRules(orderToUpdate, quantities);
        Order originalOrder = orderDao.getOrderById(orderToUpdate.getOrderId());
        if (originalOrder == null) {
            throw new Exception("Order not found.");
        }
        try {
            orderDao.updateOrderDetails(orderToUpdate);
            if (quantities != null && !quantities.isEmpty()) {
                orderDao.updateOrderItemQuantities(orderToUpdate.getOrderId(), quantities);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Database update failed. " + e.getMessage());
        }

        if (isShippingInfoChanged(originalOrder, orderToUpdate)) {
            try {
                ghnService.updateOrderOnGhn(orderToUpdate);
            } catch (GhnException e) {
                System.err.println("GHN Integration Error: " + e.getMessage());
                orderDao.markOrderForManualSync(orderToUpdate.getOrderId(), e.getMessage()); // Đánh dấu đơn hàng cần đồng bộ lại
                throw e;
            }
        }
    }

    public Order validateAndBuildOrderForUpdate(OrderUpdateDTO dto) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (dto.getReceiverName() == null || dto.getReceiverName().trim().isEmpty()) {
            errors.add("Receiver name is required.");
        }
        if (dto.getReceiverPhone() == null || !dto.getReceiverPhone().matches("^\\d{10,11}$")) {
            errors.add("Invalid phone number format.");
        }
        if (dto.getReceiverMail() == null || dto.getReceiverMail().trim().isEmpty()) {
            errors.add("Receiver email is required.");
        } else if (!dto.getReceiverMail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errors.add("Invalid email format.");
        }
        if (dto.getProvinceId() == 0) {
            errors.add("Province must be selected.");
        }
        if (dto.getDistrictId() == 0) {
            errors.add("District must be selected.");
        }
        if (dto.getWardId() == 0) {
            errors.add("Ward must be selected.");
        }
        if (dto.getStreet() == null || dto.getStreet().trim().isEmpty()) {
            errors.add("Street/House number is required.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        String provinceName = settingService.getLocationNameById(dto.getProvinceId());
        String districtName = settingService.getLocationNameById(dto.getDistrictId());
        String wardName = settingService.getLocationNameById(dto.getWardId());
        String fullAddress = addressService.combineAddressParts(dto.getStreet(), wardName, districtName, provinceName);

        Order orderToUpdate = new Order();
        orderToUpdate.setOrderId(dto.getOrderId());
        orderToUpdate.setReceiveName(dto.getReceiverName());
        orderToUpdate.setReceivePhone(dto.getReceiverPhone());
        orderToUpdate.setReceiveEmail(dto.getReceiverMail());
        orderToUpdate.setReceiveAddress(fullAddress);
        orderToUpdate.setProvinceId(dto.getProvinceId());
        orderToUpdate.setDistrictId(dto.getDistrictId());
        orderToUpdate.setWardId(dto.getWardId());
        orderToUpdate.setStreet(dto.getStreet());
        orderToUpdate.setNote(dto.getOrderNotes());
        orderToUpdate.setStatusId(dto.getStatusId());
        orderToUpdate.setProvince(provinceName);
        orderToUpdate.setDistrict(districtName);
        orderToUpdate.setWard(wardName);

        return orderToUpdate;
    }

    private void validateBusinessRules(Order orderToUpdate, Map<Integer, Integer> quantities) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (quantities != null) {
            for (Map.Entry<Integer, Integer> item : quantities.entrySet()) {
                Integer productId = item.getKey();
                int requestedQuantity = item.getValue();

                Product product = getProduct(productId);
                if (product == null) {
                    errors.add("Sản phẩm với mã '" + productId + "' không tồn tại.");
                    continue;
                }
                if (!product.isActive()) {
                    errors.add("Sản phẩm '" + product.getName() + "' hiện không được kinh doanh.");
                }
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
        return false;
    }
}


