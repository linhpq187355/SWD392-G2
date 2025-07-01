package services;

import daos.CartDAO;
import daos.ProductDAO;
import models.Cart;
import models.CartItem;
import models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartService {
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();

    }

    public Cart getCartByUserId(int userId) {
        return cartDAO.getCartByUserId(userId);
    }
    public void createCartForUser(int userId) {
        cartDAO.createCartForUser(userId);
    }

    public void clearCart(int cartId) {
        cartDAO.clearCart(cartId);
    }

    public void addCartItem(int cartId, int id, int quantity) {
        cartDAO.addCartItem(cartId, id, quantity);
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
    public Product getProductById(int productId) {
        return cartDAO.getProductById(productId);
    }
    public List<Product> getProductsForCartItems(List<CartItem> items) {
        List<Product> products = new ArrayList<>();
        for (CartItem item : items) {
            int productId = item.getProductId();
            Product product = cartDAO.getProductById(productId);
            if (product != null) {
                // Lấy danh sách ảnh cho sản phẩm
                List<String> images = productDAO.getProductImages(productId);
                product.setImages(images);

                products.add(product);
            }
        }
        return products;
    }


    public Cart getOrCreateCartForUser(int userId) {
        Cart cart = getCartByUserId(userId);
        if (cart == null) {
            createCartForUser(userId);
            cart = getCartByUserId(userId);
        }
        return cart;
    }


    public static void main(String[] args) {
        CartService cartService = new CartService();
        int userId = 2; // giả lập user tồn tại trong DB
        int t = 1;
        int testQuantity = 2;

        // 1. Tạo cart nếu chưa có
        Cart cart = cartService.getCartByUserId(userId);

        // 2. Thêm sản phẩm vào cart
        cartService.addCartItem(cart.getCartId(), t, testQuantity);
        System.out.println("✅ Added product " + t + " x" + testQuantity + " to cart");

        // 3. Lấy danh sách CartItem
        List<CartItem> items = cartService.getCartItemsByCartId(cart.getCartId());
        System.out.println("🛒 Cart Items:");
        for (CartItem item : items) {
            System.out.println("- ID: " + item.getCartItemId() +
                    " | Product: " + item.getProductId() +
                    " | Quantity: " + item.getQuantity());
        }

        // 4. Lấy thông tin sản phẩm từ CartItem
        List<Product> products = cartService.getProductsForCartItems(items);
        System.out.println("📦 Product details:");
        for (Product product : products) {
            System.out.println("- Code: " + product.getCode() +
                    " | Name: " + product.getName() +
                    " | Sale Price: " + product.getSalePrice());
        }

        // 5. Cập nhật số lượng cart item đầu tiên (nếu có)
        if (!items.isEmpty()) {
            CartItem first = items.get(0);
            cartService.updateCartItem(first.getCartItemId(), 5);
            System.out.println("✏️ Updated quantity of cartItem " + first.getCartItemId() + " to 5");
        }



    }

}
