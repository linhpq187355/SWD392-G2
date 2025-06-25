package services;

import daos.CartDAO;
import models.Cart;
import models.CartItem;
import models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartService {
    private final CartDAO cartDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
    }

    public Cart getCartByUserId(int userId) {
        return cartDAO.getCartByUserId(userId);
    }

    public void clearCart(int cartId) {
        cartDAO.clearCart(cartId);
    }

    public void addCartItem(int cartId, int productId, int quantity) {
        cartDAO.addCartItem(cartId, productId, quantity);
    }

    public void updateCartItem(int cartItemId, int quantity) {
        cartDAO.updateCartItem(cartItemId, quantity);
    }

    public void removeCartItem(int cartItemId) {
        cartDAO.removeCartItem(cartItemId);
    }

    public List<CartItem> getCartItemsByCartId(int cartId) {
        return cartDAO.getCartItemsByCartId(cartId);
    }

    public List<Product> getProductsForCartItems(List<CartItem> items) {
        List<Product> products = new ArrayList<>();
        for (CartItem item : items) {
            Product product = cartDAO.getProductById(item.getProductId());
            products.add(product);
        }
        return products;
    }
}
