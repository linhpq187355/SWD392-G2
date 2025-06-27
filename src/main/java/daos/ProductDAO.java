package daos;
import models.Cart;
import models.Order;
import models.OrderItem;
import models.Product;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class ProductDAO extends DBContext {
    public List<Product> getLatestProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT TOP 4 p.*, c.description AS cate " +
                "FROM Product p " +
                "JOIN Category c ON p.categoryId = c.categoryId " +
                "ORDER BY p.createdAt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("productId"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setCategoryId(rs.getInt("categoryId"));
                product.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                product.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                product.setCreatedBy(rs.getString("createdBy"));
                product.setCate(rs.getString("cate")); // category description
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching latest products with category", e);
        }
        return products;
    }

    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();

        List<Product> products = dao.getLatestProducts();

        if (products == null || products.isEmpty()) {
            System.out.println("❌ No products found.");
        } else {
            System.out.println("✅ Latest Products:");
            for (Product p : products) {
                System.out.println("ID: " + p.getProductId()
                        + " | Name: " + p.getName()
                        + " | Category: " + p.getCate()
                        + " | Price: $" + p.getPrice());
            }
        }
    }
}
