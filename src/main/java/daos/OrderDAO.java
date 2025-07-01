// OrderDAO.java
package daos;

import models.Order;
import models.OrderItem;

import java.sql.*;
import java.util.List;

public class OrderDAO extends DBContext {

    public String generateNextOrderCode() {
        String sql = "SELECT TOP 1 orderId FROM [Order] ORDER BY orderId DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastCode = rs.getString("orderId");
                int lastNum = Integer.parseInt(lastCode.substring(2));
                int nextNum = lastNum + 1;
                return String.format("OD%04d", nextNum);
            } else {
                return "OD0001";
            }

        } catch (SQLException e) {
            throw new RuntimeException("❌ Lỗi khi sinh orderCode", e);
        }
    }

    public String insertOrder(Order order) {
        String sql = "INSERT INTO [Order] (orderId, userId, statusId, receiveName, receivePhone, receiveAddress, receiveEmail, shippingMethod, note, createdAt, updatedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String newOrderId = generateNextOrderCode();
        order.setOrderId(newOrderId);

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newOrderId);
            if (order.getUserId() != null) {
                ps.setInt(2, order.getUserId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setInt(3, order.getStatusId());
            ps.setString(4, order.getReceiveName());
            ps.setString(5, order.getReceivePhone());
            ps.setString(6, order.getReceiveAddress());
            ps.setString(7, order.getReceiveEmail());
            ps.setString(8, order.getShippingMethod());
            ps.setString(9, order.getNote());
            ps.setTimestamp(10, Timestamp.valueOf(order.getCreatedAt()));
            ps.setTimestamp(11, Timestamp.valueOf(order.getUpdatedAt()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("❌ Không tạo được đơn hàng.");
            }

            return newOrderId;
        } catch (SQLException e) {
            throw new RuntimeException("❌ Lỗi khi insert đơn hàng", e);
        }
    }

    public void insertOrderItems(List<OrderItem> items) {
        String sql = "INSERT INTO OrderItem (orderId, productId, quantity, unitPrice) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (OrderItem item : items) {
                ps.setString(1, item.getOrderId()); // ← sửa thành setString
                ps.setInt(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setDouble(4, item.getUnitPrice());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Lỗi batch insert OrderItems", e);
        }
    }




    public static void main(String[] args) {
        OrderDAO orderDAO = new OrderDAO();

        // Tạo đơn hàng
    }

}
