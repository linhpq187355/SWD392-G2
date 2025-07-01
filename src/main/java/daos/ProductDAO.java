package daos;

import models.Category;
import models.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends DBContext {

    public List<Product> getLatestProducts() {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT TOP 4 p.*, s.name AS categoryName, s.settingId AS categoryId " +
                "FROM Product p " +
                "LEFT JOIN Setting s ON p.categoryId = s.settingId AND s.type = 'category' " +
                "WHERE p.isActive = 1 " +
                "ORDER BY p.createdAt DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setCode(rs.getString("productCode")); // Đổi từ productCode -> code
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setOriginalPrice(rs.getDouble("originalPrice")); // dùng getDouble thay vì BigDecimal
                product.setSalePrice(rs.getDouble("salePrice"));
                product.setStock(rs.getInt("stock"));
                product.setActive(rs.getBoolean("isActive"));
                product.setCreatedBy(rs.getInt("createdBy"));

                // createdAt
                Timestamp created = rs.getTimestamp("createdAt");
                if (created != null) {
                    product.setCreatedAt(created.toLocalDateTime().toLocalDate());
                }

                // updatedAt
                Timestamp updated = rs.getTimestamp("updatedAt");
                if (updated != null) {
                    product.setUpdatedAt(updated.toLocalDateTime().toLocalDate());
                }

                // Category
                Category category = new Category();
                category.setId(rs.getInt("categoryId")); // settingId trong Setting
                category.setName(rs.getString("categoryName")); // name trong Setting
                product.setCategory(category);

                // Danh sách hình ảnh chưa xử lý ở đây, có thể viết thêm sau nếu cần

                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Lỗi khi lấy danh sách sản phẩm mới nhất", e);
        }

        return products;
    }

    public List<Product> getMostSellProduct() {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT TOP 4 p.*, s.name AS categoryName, s.settingId AS categoryId, SUM(oi.quantity) AS totalSold " +
                "FROM OrderItem oi " +
                "JOIN Product p ON oi.productId = p.id " +
                "LEFT JOIN Setting s ON p.categoryId = s.settingId AND s.type = 'category' " +
                "WHERE p.isActive = 1 " +
                "GROUP BY p.id, p.productCode, p.name, p.description, p.originalPrice, p.salePrice, p.stock, " +
                "p.isActive, p.createdBy, p.createdAt, p.updatedAt, p.categoryId, s.name, s.settingId " +
                "ORDER BY totalSold DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setCode(rs.getString("productCode"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setOriginalPrice(rs.getDouble("originalPrice"));
                product.setSalePrice(rs.getDouble("salePrice"));
                product.setStock(rs.getInt("stock"));
                product.setActive(rs.getBoolean("isActive"));
                product.setCreatedBy(rs.getInt("createdBy"));

                // createdAt
                Timestamp created = rs.getTimestamp("createdAt");
                if (created != null) {
                    product.setCreatedAt(created.toLocalDateTime().toLocalDate());
                }

                // updatedAt
                Timestamp updated = rs.getTimestamp("updatedAt");
                if (updated != null) {
                    product.setUpdatedAt(updated.toLocalDateTime().toLocalDate());
                }

                // Category
                Category category = new Category();
                category.setId(rs.getInt("categoryId")); // settingId trong Setting
                category.setName(rs.getString("categoryName")); // name trong Setting
                product.setCategory(category);


                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Lỗi khi lấy danh sách sản phẩm mới nhất", e);
        }

        return products;
    }


    public List<String> getProductImages(int id) {
        String sql = "SELECT i.url FROM Image i WHERE i.productId = ?";
        List<String> images = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                images.add(resultSet.getString("url"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return images;
    }
    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();
        List<Product> products = dao.getMostSellProduct();

        if (products == null || products.isEmpty()) {
            System.out.println("❌ No products found.");
        } else {
            System.out.println("✅ Latest Products:");
            for (Product p : products) {
                System.out.println("Code: " + p.getId()
                        + " | Name: " + p.getName()
                        + " | Name: " + p.getCategory().getName()
                        + " | Sale: " + p.getSalePrice()
                        + " | Original: " + p.getOriginalPrice());
            }
        }
    }
}
