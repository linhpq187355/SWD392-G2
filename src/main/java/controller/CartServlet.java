package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Cart;
import models.CartItem;
import models.Product;
import models.User;
import services.CartService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@WebServlet(name = "CartServlet", urlPatterns = {
        "/cartView",
        "/cartAdd",
        "/cartUpdate",
        "/cartRemove",
        "/cartClear",
        "/cartUpdateGuest",
        "/cartRemoveGuest",
        "/cartClearGuest",
        "/cartBuyNow"
})
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/cartView":
                handleViewCart(request, response);
                break;
            case "/cartClear":
                handleClearCart(request, response);
                break;
            case "/cartClearGuest":
                request.getSession().removeAttribute("guestCart");
                response.sendRedirect(request.getContextPath() + "/cartView");
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/cartAdd":
                handleAddToCart(request, response);
                break;
            case "/cartUpdate":
                handleUpdateCartItem(request, response);
                break;
            case "/cartRemove":
                handleRemoveCartItem(request, response);
                break;
            case "/cartUpdateGuest":
                handleUpdateGuestItem(request, response);
                break;
            case "/cartRemoveGuest":
                handleRemoveGuestItem(request, response);
                break;
            case "/cartBuyNow":
                handleBuyNow(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    private void handleBuyNow(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        if (quantity <= 0) {
            response.sendRedirect("cartView");
            return;
        }

        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            Cart cart = cartService.getOrCreateCartForUser(user.getUserId());

            cartService.addCartItem(cart.getCartId(), productId, quantity);
            List<CartItem> items = cartService.getCartItemsByCartId(cart.getCartId());
            session.setAttribute("cartItems", items);
        } else {
            Map<Integer, Integer> guestCart = (Map<Integer, Integer>) session.getAttribute("guestCart");
            if (guestCart == null) {
                guestCart = new HashMap<>();
            }
            guestCart.put(productId, guestCart.getOrDefault(productId, 0) + quantity);
            session.setAttribute("guestCart", guestCart);
        }

        // 👉 Chuyển đến trang xác nhận đơn hàng
        response.sendRedirect(request.getContextPath() + "/orderConfirm");
    }


    private void handleViewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
            request.getRequestDispatcher("/cart-list.jsp").forward(request, response);
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
        request.getRequestDispatcher("/cart-list.jsp").forward(request, response);
    }


    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        if (quantity <= 0) {
            response.sendRedirect("cartView");
            return;
        }

        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            Cart cart = cartService.getOrCreateCartForUser(user.getUserId());

            cartService.addCartItem(cart.getCartId(), productId, quantity);
            List<CartItem> items = cartService.getCartItemsByCartId(cart.getCartId());
            session.setAttribute("cartItems", items);
        } else {
            Map<Integer, Integer> guestCart = (Map<Integer, Integer>) session.getAttribute("guestCart");
            if (guestCart == null) {
                guestCart = new HashMap<>();
            }
            guestCart.put(productId, guestCart.getOrDefault(productId, 0) + quantity);
            session.setAttribute("guestCart", guestCart);

            System.out.println("👥 Guest cart updated: " + guestCart);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println("✅ Add-to-cart process completed.\n");
    }

    private void handleUpdateCartItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        if (quantity > 0) {
            cartService.updateCartItem(cartItemId, quantity);
        }
        response.sendRedirect(request.getContextPath() + "/cartView");
    }

    private void handleRemoveCartItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
        cartService.removeCartItem(cartItemId);
        response.sendRedirect(request.getContextPath() + "/cartView");
    }

    private void handleClearCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = getUserIdFromSession(request);
        Cart cart = cartService.getCartByUserId(userId);
        cartService.clearCart(cart.getCartId());
        response.sendRedirect(request.getContextPath() + "/cartView");
    }

    private void handleUpdateGuestItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Map<Integer, Integer> guestCart = (Map<Integer, Integer>) session.getAttribute("guestCart");

        if (guestCart != null && guestCart.containsKey(productId)) {
            int qty = guestCart.get(productId);
            if ("increase".equals(action)) {
                guestCart.put(productId, qty + 1);
            } else if ("decrease".equals(action) && qty > 1) {
                guestCart.put(productId, qty - 1);
            }
        }

        session.setAttribute("guestCart", guestCart);
        response.sendRedirect(request.getContextPath() + "/cartView");
    }

    private void handleRemoveGuestItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        HttpSession session = request.getSession();
        Map<Integer, Integer> guestCart = (Map<Integer, Integer>) session.getAttribute("guestCart");

        if (guestCart != null) {
            guestCart.remove(productId);
            session.setAttribute("guestCart", guestCart);
        }

        response.sendRedirect(request.getContextPath() + "/cartView");
    }

    private int getUserIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            return ((User) session.getAttribute("user")).getUserId();
        } else {
            throw new IllegalStateException("User not logged in.");
        }
    }
}
