// OrderServlet.java
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Cart;
import models.CartItem;
import models.Product;
import services.CartService;
import services.GhnShippingService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderConfirmServlet", urlPatterns = "/orderConfirm")
public class OrderConfirmServlet extends HttpServlet {

    private final CartService cartService = new CartService();
    private final GhnShippingService ghnShippingService = new GhnShippingService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = 2; // giả lập

        // Lấy thông tin người dùng từ form
        String name = request.getParameter("receiveName");
        String phone = request.getParameter("receivePhone");
        String address = request.getParameter("receiveAddress");
        String note = request.getParameter("note");
        String shippingMethod = request.getParameter("shippingMethod");

        int shippingFee = 0;
        try {
            int toDistrictId = Integer.parseInt(request.getParameter("districtId"));
            String toWardCode = request.getParameter("wardCode");

            // Cấu hình mặc định GHN
            int fromDistrictId = 1450; // Gò Vấp
            int serviceId = 53320;
            int height = 10, length = 20, width = 10, weight = 200;
            int insuranceValue = 1000000;

            shippingFee = ghnShippingService.calculateShippingFee(
                    fromDistrictId, serviceId, toDistrictId, toWardCode,
                    height, length, weight, width, insuranceValue
            );
        } catch (Exception e) {
            System.out.println("Không thể tính phí GHN: " + e.getMessage());
        }

        // Lưu session để hiển thị tại trang checkout.jsp
        HttpSession session = request.getSession();
        session.setAttribute("receiveName", name);
        session.setAttribute("receivePhone", phone);
        session.setAttribute("receiveAddress", address);
        session.setAttribute("note", note);
        session.setAttribute("shippingMethod", shippingMethod);
        session.setAttribute("shippingFee", shippingFee);

        // Lấy giỏ hàng
        Cart cart = cartService.getCartByUserId(userId);
        List<CartItem> items = cartService.getCartItemsByCartId(cart.getCartId());
        List<Product> products = cartService.getProductsForCartItems(items);

        double subtotal = 0.0;
        for (int i = 0; i < items.size(); i++) {
            subtotal += products.get(i).getPrice() * items.get(i).getQuantity();
        }

        double discount = Math.round(subtotal * 0.10 * 100.0) / 100.0;
        double total = Math.round((subtotal - discount + shippingFee) * 100.0) / 100.0;

        session.setAttribute("items", items);
        session.setAttribute("products", products);
        session.setAttribute("subtotal", subtotal);
        session.setAttribute("discount", discount);
        session.setAttribute("total", total);

        // Sau khi người dùng Confirm → chuyển đến trang checkout.jsp để xác nhận lại lần cuối
        response.sendRedirect("checkout.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hiển thị form xác nhận từ Cart sang
        int userId = 2;
        Cart cart = cartService.getCartByUserId(userId);
        List<CartItem> items = cartService.getCartItemsByCartId(cart.getCartId());
        List<Product> products = cartService.getProductsForCartItems(items);

        double subtotal = 0.0;
        for (int i = 0; i < items.size(); i++) {
            subtotal += products.get(i).getPrice() * items.get(i).getQuantity();
        }

        double discount = Math.round(subtotal * 0.10 * 100.0) / 100.0;

        request.setAttribute("items", items);
        request.setAttribute("products", products);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("discount", discount);

        request.getRequestDispatcher("order-cf.jsp").forward(request, response);
    }
}
