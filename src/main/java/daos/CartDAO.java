package daos;

import models.Cart;
import models.CartItem;
import models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
public class CartDAO extends DBContext {

    public Cart getCartByUserId(int userId) {
        String sql = "SELECT * FROM Cart WHERE userId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getInt("cartId"));
                cart.setUserId(rs.getInt("userId"));
                cart.setCreatedAt(rs.getTimestamp("createdAt") != null ? rs.getTimestamp("createdAt").toLocalDateTime() : null);
                cart.setUpdatedAt(rs.getTimestamp("updatedAt") != null ? rs.getTimestamp("updatedAt").toLocalDateTime() : null);
                return cart;
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving cart for userId " + userId + ": " + e.getMessage());
            throw new RuntimeException("Error retrieving cart", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing DB resources", e);
            }
        }

        return null;
    }
    public void clearCart(int cartId) {
        String sql = "DELETE FROM CartItem WHERE cartId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing cart", e);
        }
    }
    public void addCartItem(int cartId, int productId, int quantity) {
        String sql = "INSERT INTO CartItem (cartId, productId, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding cart item", e);
        }
    }

    public void updateCartItem(int cartItemId, int quantity) {
        String sql = "UPDATE CartItem SET quantity = ? WHERE cartItemId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating cart item", e);
        }
    }

    public void removeCartItem(int cartItemId) {
        String sql = "DELETE FROM CartItem WHERE cartItemId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing cart item", e);
        }
    }
    public Product getProductById(int productId) {
        String sql =
                "SELECT p.productId, p.name, p.description, p.price, p.stock, p.createdBy, " +
                        "p.createdAt, p.updatedAt, p.categoryId, " +
                        "i.url AS img, " +
                        "c.description AS cate " +
                        "FROM Product p " +
                        "LEFT JOIN Image i ON p.productId = i.productId " +
                        "LEFT JOIN Category c ON p.categoryId = c.categoryId " +
                        "WHERE p.productId = ?";


        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("productId"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setCreatedBy(rs.getString("createdBy"));
                product.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                product.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                product.setCategoryId(rs.getInt("categoryId"));

                // chỉ gọi nếu bạn đã thêm field setter tương ứng
                product.setImg(rs.getString("img"));
                product.setCate(rs.getString("cate"));

                return product;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching product with details", e);
        }

        return null;
    }

    public List<CartItem> getCartItemsByCartId(int cartId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT * FROM CartItem WHERE cartId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getInt("cartItemId"));
                item.setCartId(rs.getInt("cartId"));
                item.setProductId(rs.getInt("productId"));
                item.setQuantity(rs.getInt("quantity"));
                items.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving cart items", e);
        }
        return items;
    }
    public static void main(String[] args) {
        CartDAO dao = new CartDAO();
        int userId = 2;
        int testProductId = 4;

        Cart cart = dao.getCartByUserId(userId);
        if (cart == null) {
            System.out.println("❌ Cart not found");
            return;
        }

        int cartId = cart.getCartId();
        System.out.println("✅ Cart found: " + cartId);

        // Add product
        dao.addCartItem(cartId, testProductId, 2);
        System.out.println("✅ Added product to cart");

        // List items
        List<CartItem> items = dao.getCartItemsByCartId(cartId);
        for (CartItem i : items) {
            System.out.printf("🛒 Item: cartItemId=%d, productId=%d, quantity=%d%n",
                    i.getCartItemId(), i.getProductId(), i.getQuantity());
        }

        // Update first item
        if (!items.isEmpty()) {
            dao.updateCartItem(items.get(0).getCartItemId(), 5);
            System.out.println("✏️ Updated quantity to 5");
        }




    }
}

