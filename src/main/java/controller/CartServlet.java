package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Cart;
import models.CartItem;
import models.Product;
import services.CartService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = {
        "/cartView",
        "/cartAdd",
        "/cartUpdate",
        "/cartRemove",
        "/cartClear"
})
public class CartServlet extends HttpServlet {

    private CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/cartView":
                handleViewCart(request, response);
                break;
            case "/cartClear":
                handleClearCart(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    private void handleViewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = 2;
        Cart cart = cartService.getCartByUserId(userId);
        List<CartItem> items = cartService.getCartItemsByCartId(cart.getCartId());
        List<Product> products = cartService.getProductsForCartItems(items);
        double subtotal = 0.0;
        for (int i = 0; i < items.size(); i++) {
            subtotal += products.get(i).getPrice() * items.get(i).getQuantity();
        }
        double discount = Math.round(subtotal * 0.10 * 100.0) / 100.0;
        double total = Math.round((subtotal - discount) * 100.0) / 100.0;


        request.setAttribute("cart", cart);
        request.setAttribute("items", items);
        request.setAttribute("products", products);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("discount", discount);
        request.setAttribute("total", total);
        request.getRequestDispatcher("/cart-list.jsp").forward(request, response);
    }


    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = 2;
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        Cart cart = cartService.getCartByUserId(userId);
        cartService.addCartItem(cart.getCartId(), productId, quantity);

        response.sendRedirect("/cartView");
    }

    private void handleUpdateCartItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        cartService.updateCartItem(cartItemId, quantity);
        response.sendRedirect(request.getContextPath() + "/cartView");
    }

    private void handleRemoveCartItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
        cartService.removeCartItem(cartItemId);
        System.out.println("Context path (remove): " + request.getContextPath());
        response.sendRedirect(request.getContextPath() + "/cartView");
    }

    private void handleClearCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = 2;
        Cart cart = cartService.getCartByUserId(userId);
        cartService.clearCart(cart.getCartId());
        response.sendRedirect(request.getContextPath() + "/cartView");
    }
}
