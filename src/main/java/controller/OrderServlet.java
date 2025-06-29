package controller;

import models.Order;
import models.Setting;
import services.AddressService;
import services.OrderService;
import services.SettingService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import services.ValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/api/locations"})
public class OrderServlet extends HttpServlet {

    private OrderService orderService;
    private SettingService settingService;
    private AddressService addressService;

    @Override
    public void init() {
        orderService = new OrderService();
        settingService = new SettingService();
        addressService = new AddressService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        // API Endpoint để lấy dữ liệu địa chỉ động (AJAX)
        if ("/api/locations".equals(servletPath)) {
            handleLocationApi(request, response);
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Mặc định là list
        }

        switch (action) {
            case "edit":
                showEditForm(request, response);
                break;
            case "list":
            default:
                handleOrderList(request, response);
                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("update".equals(action)) {
            updateOrder(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/orders?action=list");
        }
    }

    private void handleOrderList(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            HttpSession session = request.getSession();
            if (session.getAttribute("userRole") == null || session.getAttribute("userId") == null) {
                session.setAttribute("userRole", "Customer"); // Hoặc "Customer"
                session.setAttribute("userId", 2);
            }

            if (session == null || session.getAttribute("userRole") == null || session.getAttribute("userId") == null) {
                response.sendRedirect("/login.jsp");
                return;
            }

            String role = (String) session.getAttribute("userRole");
            int userId = (int) session.getAttribute("userId");

            String search = request.getParameter("search");

            List<Order> orders = null;

            try {
                if (search != null && !search.trim().isEmpty()) {
                    orders = orderService.searchOrders(search);
                } else if ("Staff".equalsIgnoreCase(role)) {
                    orders = orderService.getOrdersAssignedToSales(userId);
                } else if ("Customer".equalsIgnoreCase(role)) {
                    orders = orderService.getOrdersByCustomer(userId);
                }

                if (orders == null || orders.isEmpty()) {
                    request.setAttribute("message", "No orders found.");
                } else {
                    request.setAttribute("orders", orders);
                }
                List<Setting> listStatus = settingService.getOrderStatuses();
                request.setAttribute("listStatus", listStatus);
                request.setAttribute("userRole", role);
                request.getRequestDispatcher("/orderList.jsp").forward(request, response);

            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
            }
        }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderId = request.getParameter("id");
        HttpSession session = request.getSession(false);

        // Giả lập session
        if (session.getAttribute("userRole") == null || session.getAttribute("userId") == null) {
            session.setAttribute("userRole", "Customer"); // Hoặc "Customer"
            session.setAttribute("userId", 2);
        }

        // Tạm thời bỏ qua kiểm tra quyền để tập trung vào logic
        // String role = (String) session.getAttribute("userRole");
        // int userId = (int) session.getAttribute("userId");

        Order order = orderService.getOrderForEdit(orderId);

        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            return;
        }

        // Chuẩn bị dữ liệu cho các dropdown địa chỉ
        List<Setting> provinces = settingService.getAllProvinces();
        request.setAttribute("order", order);
        request.setAttribute("provinces", provinces);

        // Lấy danh sách trạng thái (ví dụ)
        List<Setting> statuses = settingService.getOrderStatuses();
        request.setAttribute("statuses", statuses);


        request.getRequestDispatcher("/orderUpdate.jsp").forward(request, response);
    }

    private void updateOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy tất cả tham số từ form
        String orderId = request.getParameter("orderId");
        String receiveName = request.getParameter("receiverName");
        String receivePhone = request.getParameter("receiverPhone");
        String receiveEmail = request.getParameter("receiverMail");
        String province = request.getParameter("province");
        String district = request.getParameter("district");
        String ward = request.getParameter("ward");
        String street = request.getParameter("street");
        String note = request.getParameter("orderNotes");
        int statusId = Integer.parseInt(request.getParameter("orderStatus"));
        int provinceId = Integer.parseInt(province);
        int districtId = Integer.parseInt(district);
        int wardId = Integer.parseInt(ward);
        // Lấy số lượng sản phẩm mới
        Map<String, Integer> quantities = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (key.startsWith("quantity_")) {
                String productId = key.substring(9);
                try {
                    int quantity = Integer.parseInt(value[0]);
                    quantities.put(productId, quantity);
                } catch (NumberFormatException e) {
                    // Bỏ qua số lượng không hợp lệ
                }
            }
        });

        List<String> errors = new ArrayList<>();

        // Validation cơ bản
        if (receiveName == null || receiveName.trim().isEmpty()) {
            errors.add("Receiver name is required.");
        }
        if (receivePhone == null || !receivePhone.matches("^\\d{10,11}$")) {
            errors.add("Invalid phone number format.");
        }

        if (receiveEmail == null || receiveEmail.trim().isEmpty()) {
            errors.add("Receiver email is required.");
        } else if (!receiveEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errors.add("Invalid email format.");
        }

        if (province == null || province.isEmpty()) {
            errors.add("Province must be selected.");
        }
        if (district == null || district.isEmpty()) {
            errors.add("District must be selected.");
        }
        if (ward == null || ward.isEmpty()) {
            errors.add("Ward must be selected.");
        }
        if (street == null || street.trim().isEmpty()) {
            errors.add("Street/House number is required.");
        }

        if (!errors.isEmpty()) {
            // Nếu có lỗi thì load lại order gốc để giữ lại thông tin
            Order order = orderService.getOrderForEdit(orderId);
            if (order == null) {
                order = new Order();
                order.setOrderId(orderId);
            }
            // Gán lại các trường để hiển thị lại form
            order.setReceiveName(receiveName);
            order.setReceivePhone(receivePhone);
            order.setReceiveEmail(receiveEmail);
            order.setProvinceId(provinceId);
            order.setDistrictId(districtId);
            order.setWardId(wardId);
            order.setStreet(street);
            order.setNote(note);
            order.setStatusId(statusId);

            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.setAttribute("provinces", settingService.getAllProvinces());
            request.getRequestDispatcher("/orderUpdate.jsp").forward(request, response);
            return;
        }

        String provinceName = settingService.getLocationNameById(provinceId);
        String districtName = settingService.getLocationNameById(districtId);
        String wardName = settingService.getLocationNameById(wardId);
        // Tạo đối tượng Order để update
        Order orderToUpdate = new Order();
        orderToUpdate.setOrderId(orderId);
        orderToUpdate.setReceiveName(receiveName);
        orderToUpdate.setReceivePhone(receivePhone);
        orderToUpdate.setReceiveEmail(receiveEmail);
        orderToUpdate.setReceiveAddress(addressService.combineAddressParts(street, wardName, districtName, provinceName));
        orderToUpdate.setProvinceId(provinceId);
        orderToUpdate.setDistrictId(districtId);
        orderToUpdate.setWardId(wardId);
        orderToUpdate.setStreet(street);
        orderToUpdate.setNote(note);
        orderToUpdate.setStatusId(statusId);
        orderToUpdate.setProvince(provinceName);
        orderToUpdate.setDistrict(districtName);
        orderToUpdate.setWard(wardName);
        try {
            // Gọi service update
            orderService.updateOrder(orderToUpdate, quantities);
            response.sendRedirect(request.getContextPath() + "/orders?action=list&update_success=true");
        } catch (ValidationException ve) {
            // 👉👉 Bắt lỗi VALIDATION và hiển thị lại form

            Order order = orderService.getOrderForEdit(orderId);
            if (order == null) {
                order = orderToUpdate; // Dùng lại data user nhập
            }

            request.setAttribute("errors", ve.getErrorMessages()); // Lấy lỗi từ Service
            request.setAttribute("order", order);
            request.setAttribute("provinces", settingService.getAllProvinces());
            request.getRequestDispatcher("/orderUpdate.jsp").forward(request, response);

        } catch (Exception e) {
            errors.add("Update failed due to a system error: " + e.getMessage());
            Order order = orderService.getOrderForEdit(orderId);
            if (order == null) {
                order = orderToUpdate; // Nếu lỗi DB thì dùng lại dữ liệu người dùng nhập
            }
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.setAttribute("provinces", settingService.getAllProvinces());
            request.getRequestDispatcher("/orderUpdate.jsp").forward(request, response);
        }
    }

    // API để lấy huyện và xã
    private void handleLocationApi(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String type = request.getParameter("type"); // "districts" or "wards"
        String parentIdStr = request.getParameter("parentId");
        int parentId = Integer.parseInt(parentIdStr);

        List<Setting> locations = new ArrayList<>();
        if ("districts".equals(type)) {
            locations = settingService.getDistrictsByProvinceId(parentId);
        } else if ("wards".equals(type)) {
            locations = settingService.getWardsByDistrictId(parentId);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Simple JSON serialization
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < locations.size(); i++) {
            Setting loc = locations.get(i);
            json.append("{");
            json.append("\"id\":").append(loc.getSettingId()).append(",");
            json.append("\"name\":\"").append(loc.getName()).append("\"");
            json.append("}");
            if (i < locations.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        response.getWriter().write(json.toString());
    }
    }
