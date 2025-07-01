package daos;

import models.Cart;
import models.CartItem;
import models.Category;
import models.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CartDAO extends DBContext {
   ProductDAO productDAO = new ProductDAO();
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

    public void createCartForUser(int userId) {
        String sql = "INSERT INTO Cart (userId, createdAt, updatedAt) VALUES (?, GETDATE(), GETDATE())";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void addCartItem(int cartId, int id, int quantity) {
        String checkSql = "SELECT quantity FROM CartItem WHERE cartId = ? AND productId = ?";
        String updateSql = "UPDATE CartItem SET quantity = quantity + ? WHERE cartId = ? AND productId = ?";
        String insertSql = "INSERT INTO CartItem (cartId, productId, quantity) VALUES (?, ?, ?)";

        try {
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, cartId);
            checkStmt.setInt(2, id);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, cartId);
                updateStmt.setInt(3, id);
                updateStmt.executeUpdate();
            } else {
                // Chưa có → Thêm mới
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setInt(1, cartId);
                insertStmt.setInt(2, id);
                insertStmt.setInt(3, quantity);
                insertStmt.executeUpdate();
            }
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

    public Product getProductById(int id) {
        String sql =
                "SELECT p.id, p.productCode, p.name, p.description, p.originalPrice, p.salePrice, p.stock, " +
                        "p.createdBy, p.createdAt, p.updatedAt, p.categoryId, " +
                        "s.name AS cateName, s.settingId AS cateId " +
                        "FROM Product p " +
                        "LEFT JOIN Setting s ON p.categoryId = s.settingId AND s.type = 'category' " +
                        "WHERE p.id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = new Product();

                product.setId(rs.getInt("id"));
                product.setCode(rs.getString("productCode"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setOriginalPrice(rs.getDouble("originalPrice"));
                product.setSalePrice(rs.getDouble("salePrice"));
                product.setStock(rs.getInt("stock"));
                product.setCreatedBy(rs.getInt("createdBy"));

                Timestamp created = rs.getTimestamp("createdAt");
                if (created != null) {
                    product.setCreatedAt(created.toLocalDateTime().toLocalDate());
                }

                Timestamp updated = rs.getTimestamp("updatedAt");
                if (updated != null) {
                    product.setUpdatedAt(updated.toLocalDateTime().toLocalDate());
                }

                Category category = new Category();
                category.setId(rs.getInt("cateId"));
                category.setName(rs.getString("cateName"));
                product.setCategory(category);

                // Gọi DAO lấy danh sách ảnh
                product.setImages(productDAO.getProductImages(id)); // ← gọi hàm bạn đã có

                return product;
            }

        } catch (SQLException e) {
            throw new RuntimeException("❌ Lỗi khi lấy sản phẩm theo ID", e);
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


}
