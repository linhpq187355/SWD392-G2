package daos;

import models.Product;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProductDao extends DBContext {

    public Product findProductByCode(String productCode) {
        String sql = "SELECT * FROM Product WHERE productCode = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractProduct(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức này được cập nhật để khớp với cấu trúc bảng của bạn
    private Product extractProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductCode(rs.getString("productCode"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setOriginalPrice(rs.getBigDecimal("originalPrice")); // Đã sửa
        p.setSalePrice(rs.getBigDecimal("salePrice"));         // Đã thêm
        p.setStock(rs.getInt("stock"));                        // Đã sửa
        p.setActive(rs.getBoolean("isActive"));                // Đã thêm
        p.setCreatedBy(rs.getInt("createdBy"));                // Đã thêm
        p.setCategoryId(rs.getInt("categoryId"));

        Timestamp createdTs = rs.getTimestamp("createdAt");
        if (createdTs != null) {
            p.setCreatedAt(createdTs.toLocalDateTime());
        }

        Timestamp updatedTs = rs.getTimestamp("updatedAt");
        if (updatedTs != null) {
            p.setUpdatedAt(updatedTs.toLocalDateTime());
        }
        return p;
    }
}