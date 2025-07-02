package controller;

import models.Order;
import models.OrderUpdateDTO;
import models.Setting;
import models.User;
import services.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

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
    private UserService userService;
    @Override
    public void init() {
        orderService = new OrderService();
        settingService = new SettingService();
        addressService = new AddressService();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

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
                throws IOException {
            HttpSession session = request.getSession(false);
            if (session == null || !(session.getAttribute("user") instanceof User user)) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String role = userService.getUserRole(user);
            List<Order> orders = orderService.getOrdersForUser(user.getUserId(), role);
            try {
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
        if (session == null || !(session.getAttribute("user") instanceof User user)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String role = userService.getUserRole(user);
        Order order = orderService.getOrderForEdit(orderId);
        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            return;
        }
        if (!userService.checkRoleForUpdateOrder(user.getUserId(), role, orderId)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No permission to edit order");
            return;
        }
        List<Setting> provinces = settingService.getAllProvinces();
        request.setAttribute("order", order);
        request.setAttribute("provinces", provinces);
        List<Setting> statuses = settingService.getOrderStatuses();
        request.setAttribute("statuses", statuses);
        request.setAttribute("userRole", role);
        request.getRequestDispatcher("/orderUpdate.jsp").forward(request, response);
    }

    private void updateOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OrderUpdateDTO dto = new OrderUpdateDTO();
        dto.setOrderId(request.getParameter("orderId"));
        dto.setReceiverName(request.getParameter("receiverName"));
        dto.setReceiverPhone(request.getParameter("receiverPhone"));
        dto.setReceiverMail(request.getParameter("receiverMail"));
        dto.setProvinceId(Integer.parseInt(request.getParameter("province")));
        dto.setDistrictId(Integer.parseInt(request.getParameter("district")));
        dto.setWardId(Integer.parseInt(request.getParameter("ward")));
        dto.setStreet(request.getParameter("street"));
        dto.setOrderNotes(request.getParameter("orderNotes"));
        dto.setStatusId(Integer.parseInt(request.getParameter("orderStatus")));

        Map<Integer, Integer> quantities = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (key.startsWith("quantity_")) {
                int productId = Integer.parseInt(key.substring(9));
                quantities.put(productId, Integer.parseInt(value[0]));
            }
        });
        dto.setQuantities(quantities);

        try {
            Order orderToUpdate = orderService.validateAndBuildOrderForUpdate(dto);
            orderService.updateOrder(orderToUpdate, dto.getQuantities());
            response.sendRedirect(request.getContextPath() + "/orders?action=list&update_success=true");
        } catch (ValidationException ve) {
            request.setAttribute("errors", ve.getErrorMessages());
            Order originalOrder = orderService.getOrderForEdit(dto.getOrderId());
            originalOrder.setReceiveName(dto.getReceiverName());
            originalOrder.setReceivePhone(dto.getReceiverPhone());
            originalOrder.setWardId(dto.getWardId());
            originalOrder.setStreet(dto.getStreet());
            originalOrder.setDistrictId(dto.getDistrictId());
            originalOrder.setProvinceId(dto.getProvinceId());
            originalOrder.setStatusId(dto.getStatusId());
            originalOrder.setNote(dto.getOrderNotes());
            request.setAttribute("order", originalOrder);
            request.setAttribute("provinces", settingService.getAllProvinces());
            request.getRequestDispatcher("/orderUpdate.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errors", List.of("Update failed due to a system error: " + e.getMessage()));
            Order originalOrder = orderService.getOrderForEdit(dto.getOrderId());
            request.setAttribute("order", originalOrder);
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
