package daos;

import models.Category;
import models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends DBContext{

    public List<Product> getAllProduct() {
        String sql = "select p.id, p.productCode, p.name, p.description, p.originalPrice, p.salePrice, p.stock, p.isActive, p.createdAt, p.categoryId, s.name [catName]\n" +
                "from Product p\n" +
                "join Setting s on s.settingId = p.categoryId\n" +
                "where s.type = 'category'";
        List<Product> productList = new ArrayList<>();
        try (Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setCode(resultSet.getString("productCode"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setOriginalPrice(resultSet.getDouble("originalPrice"));
                product.setSalePrice(resultSet.getDouble("salePrice"));
                product.setStock(resultSet.getInt("stock"));
                product.setActive(resultSet.getBoolean("isActive"));
                product.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                Category category = new Category();
                category.setId(resultSet.getInt("categoryId"));
                category.setName(resultSet.getString("catName"));
                product.setCategory(category);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public int insertProduct(Product product) {
        String query = "INSERT INTO Product (productCode, name, description, originalPrice, salePrice, stock, isActive, categoryId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, product.getCode());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setDouble(4, product.getOriginalPrice());
            preparedStatement.setDouble(5, product.getSalePrice());
            preparedStatement.setInt(6, product.getStock());
            preparedStatement.setBoolean(7, product.isActive());
            preparedStatement.setInt(8, product.getCategory().getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting product failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Trả về ID mới tạo
                } else {
                    throw new SQLException("Inserting product failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Lỗi
    }

    public boolean insertImages(List<String> images, int id) {
        String query = "insert into Image (productId, url) " +
                "values (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (String url : images) {
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, url);
                preparedStatement.addBatch();
            }

            int[] results = preparedStatement.executeBatch();
            for (int count : results) {
                if (count == 0) return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Product getProduct(int id) {
        String sql = "select p.id, p.productCode, p.name, p.description, p.originalPrice, p.salePrice, p.stock, p.isActive, p.createdAt, p.categoryId, s.name [catName]\n" +
                "                from Product p\n" +
                "                join Setting s on s.settingId = p.categoryId\n" +
                "                where s.type = 'category' and p.id = ?";
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setCode(resultSet.getString("productCode"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setOriginalPrice(resultSet.getDouble("originalPrice"));
                product.setSalePrice(resultSet.getDouble("salePrice"));
                product.setStock(resultSet.getInt("stock"));
                product.setActive(resultSet.getBoolean("isActive"));
                Category category = new Category();
                category.setId(resultSet.getInt("categoryId"));
                category.setName(resultSet.getString("catName"));
                product.setCategory(category);
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getProductImages(int id) {
        String sql = "select i.url" +
                "       from Image i" +
                "                where i.productId = ?";
        List<String> images = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {
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

    public void deleteProductImages(int id) {
        String sql = "DELETE FROM Image WHERE productId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE Product " +
                "SET productCode = ?, name = ?, categoryId = ?, isActive = ?, stock = ?, originalPrice = ?, salePrice = ?, description = ? " +
                "WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getCode());
            stmt.setString(2, product.getName());
            stmt.setInt(3, product.getCategory().getId());
            stmt.setBoolean(4, product.isActive());
            stmt.setInt(5, product.getStock());
            stmt.setDouble(6, product.getOriginalPrice());
            stmt.setDouble(7, product.getSalePrice());
            stmt.setString(8, product.getDescription());
            stmt.setInt(9, product.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
        }
    }

}
