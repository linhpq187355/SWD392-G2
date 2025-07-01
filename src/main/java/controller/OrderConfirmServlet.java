package controller;

import daos.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.*;
import services.CartService;
import services.OrderService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet(name = "OrderConfirmServlet", urlPatterns = "/orderConfirm")
public class OrderConfirmServlet extends HttpServlet {
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Lấy thông tin từ form
        String name = request.getParameter("receiveName");
        String phone = request.getParameter("receivePhone");
        String address = request.getParameter("receiveAddress");
        String email = request.getParameter("email");
        String note = request.getParameter("note");
        String shippingMethod = request.getParameter("shippingMethod");

        // Khởi tạo đơn hàng
        Order order = new Order();
        order.setReceiveName(name);
        order.setReceivePhone(phone);
        order.setReceiveAddress(address);
        order.setReceiveEmail(email);
        order.setNote(note);
        order.setShippingMethod(shippingMethod);
        order.setStatusId(1); // Chờ xác nhận
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        try {
            if (user != null) {
                // Logged-in user
                int userId = user.getUserId();
                order.setUserId(userId);

                Cart cart = cartService.getCartByUserId(userId);
                List<CartItem> cartItems = cartService.getCartItemsByCartId(cart.getCartId());
                List<OrderItem> orderItems = new ArrayList<>();

                for (CartItem item : cartItems) {
                    Product product = cartService.getProductById(item.getProductId());
                    if (product == null) continue;

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(product.getId());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setUnitPrice(product.getSalePrice());

                    orderItems.add(orderItem);
                }

                if ("COD".equalsIgnoreCase(shippingMethod)) {
                    if (!orderItems.isEmpty()) {
                        orderService.createOrderForUser(order, orderItems);
                        cartService.clearCart(cart.getCartId());
                        System.out.println("🟢 Start doPost");
                        System.out.println("🟢 User: " + user);
                        System.out.println("🟢 Shipping Method: " + shippingMethod);
                        System.out.println("🟢 Order Items: " + orderItems.size());
                    } else {
                        throw new RuntimeException("❌ Không có sản phẩm nào trong giỏ hàng!");
                    }
                }


            } else {
                @SuppressWarnings("unchecked")
                Map<Integer, Integer> guestCart = (Map<Integer, Integer>) session.getAttribute("guestCart");
                if (guestCart != null && !guestCart.isEmpty()) {
                    Map<Product, Integer> productMap = new LinkedHashMap<>();
                    for (Map.Entry<Integer, Integer> entry : guestCart.entrySet()) {
                        Product product = cartService.getProductById(entry.getKey());
                        if (product == null) continue;
                        productMap.put(product, entry.getValue());
                    }

                    if ("COD".equalsIgnoreCase(shippingMethod)) {
                        orderService.createOrderForGuest(order, productMap);
                        session.removeAttribute("guestCart");
                    }
                }
            }

            response.sendRedirect("order-cf.jsp?success=true");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("order-cf.jsp?error=true");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String success = request.getParameter("success");
        String error = request.getParameter("error");

        if ("true".equals(success) || "true".equals(error)) {
            request.getRequestDispatcher("/order-cf.jsp").forward(request, response);
            return;
        }
        OrderService orderService = new OrderService();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // GUEST
            Map<Integer, Integer> guestCart = (Map<Integer, Integer>) session.getAttribute("guestCart");
            List<Product> products = new ArrayList<>();
            double subtotal = 0;
            double originalTotal = 0;
            double totalDiscount = 0;

            if (guestCart != null && !guestCart.isEmpty()) {
                for (Map.Entry<Integer, Integer> entry : guestCart.entrySet()) {
                    Product product = cartService.getProductById(entry.getKey());
                    if (product != null) {
                        products.add(product);

                        int quantity = entry.getValue();
                        double qty = (double) quantity;

                        double lineOriginal = product.getOriginalPrice() * qty;
                        double lineSale = product.getSalePrice() * qty;
                        double lineDiscount = lineOriginal - lineSale;

                        subtotal += lineSale;
                        originalTotal += lineOriginal;
                        totalDiscount += lineDiscount;
                    }
                }
            }

            request.setAttribute("guestProducts", products);
            request.setAttribute("guestQuantities", guestCart);
            request.setAttribute("originalTotal", originalTotal);
            request.setAttribute("discount", totalDiscount);
            request.setAttribute("subtotal", subtotal);
            request.setAttribute("total", subtotal);
            request.getRequestDispatcher("/order-cf.jsp").forward(request, response);
            return;
        }

        // LOGGED-IN USER
        int userId = user.getUserId();
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            cartService.createCartForUser(userId);
            cart = cartService.getCartByUserId(userId);
        }

        List<CartItem> items = cartService.getCartItemsByCartId(cart.getCartId());
        List<Product> products = cartService.getProductsForCartItems(items);

        double subtotal = 0;
        double originalTotal = 0;
        double totalDiscount = 0;

        for (CartItem item : items) {
            Product product = cartService.getProductById(item.getProductId());
            if (product != null) {
                int quantity = item.getQuantity();
                double qty = (double) quantity;

                double lineOriginal = product.getOriginalPrice() * qty;
                double lineSale = product.getSalePrice() * qty;
                double lineDiscount = lineOriginal - lineSale;

                originalTotal += lineOriginal;
                subtotal += lineSale;
                totalDiscount += lineDiscount;

                products.add(product);
            }
        }

        request.setAttribute("cart", cart);
        request.setAttribute("items", items);
        request.setAttribute("products", products);
        request.setAttribute("originalTotal", originalTotal);
        request.setAttribute("discount", totalDiscount);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("total", subtotal);
        request.getRequestDispatcher("/order-cf.jsp").forward(request, response);
    }
}
