// OrderDAO.java
package daos;

import models.Order;
import models.OrderItemDetail;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDao extends DBContext {
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM [Order]";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> getOrdersByStatus(int statusId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM [Order] WHERE statusId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, statusId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> getOrdersSortedByDate(boolean ascending) {
        List<Order> list = new ArrayList<>();
        String order = ascending ? "ASC" : "DESC";
        String sql = "SELECT * FROM [Order] ORDER BY createdAt " + order;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> searchOrders(String keyword) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM [Order] WHERE receiveName LIKE ? OR receiveEmail LIKE ? OR receiveAddress LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> getOrdersBySales(int salesId) {
        List<Order> list = new ArrayList<>();
        String sql = """
        SELECT o.*
        FROM [Order] o
        JOIN [OrderAssignment] oa ON o.orderId = oa.orderId
        WHERE oa.assignedTo = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, salesId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy đơn hàng theo Customer ID
    public List<Order> getOrdersByCustomer(int customerId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM [Order] WHERE userId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(extractOrder(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public double getOrderTotal(String orderId) {
        double total = 0.0;
        String sql = """
        SELECT SUM(quantity * unitPrice) AS total
        FROM OrderItem
        WHERE orderId = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public Order getOrderById(String orderId) {
        String sql = "SELECT * FROM [Order] WHERE orderId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<OrderItemDetail> getOrderItemsWithProductInfo(String orderId) {
        List<OrderItemDetail> list = new ArrayList<>();

        String sql = """
        SELECT oi.productId, oi.quantity, oi.unitPrice,
               p.name AS productName
        FROM OrderItem oi
        JOIN Product p ON oi.productId = p.id
        WHERE oi.orderId = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItemDetail item = new OrderItemDetail();
                item.setProductId(rs.getInt("productId"));
                item.setProductName(rs.getString("productName"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getDouble("unitPrice"));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateOrderDetails(Order order) throws SQLException {
        String sql = """
            UPDATE [Order]
            SET receiveName = ?, receivePhone = ?, receiveEmail = ?, receiveAddress = ?,
                statusId = ?, note = ?, updatedAt = ?
            WHERE orderId = ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, order.getReceiveName());
            ps.setString(2, order.getReceivePhone());
            ps.setString(3, order.getReceiveEmail());
            ps.setString(4, order.getReceiveAddress());
            ps.setInt(5, order.getStatusId());
            ps.setString(6, order.getNote());
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(8, order.getOrderId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateOrderItemQuantities(String orderId, Map<String, Integer> quantities) throws SQLException {
        String sql = "UPDATE OrderItem SET quantity = ? WHERE orderId = ? AND productId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false); // Bắt đầu transaction
            for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
                String productId = entry.getKey();
                int newQuantity = entry.getValue();

                ps.setInt(1, newQuantity);
                ps.setString(2, orderId);
                ps.setString(3, productId);
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            connection.commit(); // Kết thúc transaction
            return results.length == quantities.size(); // Kiểm tra xem tất cả batch có thành công không
        } catch (SQLException e) {
            connection.rollback(); // Rollback nếu có lỗi
            throw e; // Ném lại exception để tầng service xử lý
        } finally {
            connection.setAutoCommit(true); // Trả lại trạng thái mặc định
        }
    }


    private Order extractOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getString("orderId"));
        o.setUserId(rs.getInt("userId"));
        o.setStatusId(rs.getInt("statusId"));
        o.setReceiveName(rs.getString("receiveName"));
        o.setReceivePhone(rs.getString("receivePhone"));
        o.setReceiveAddress(rs.getString("receiveAddress"));
        o.setShippingMethod(rs.getString("shippingMethod"));
        o.setNote(rs.getString("note"));
        o.setTotalPrice(getOrderTotal(rs.getString("orderId")));
        Timestamp createdTs = rs.getTimestamp("createdAt");
        if (createdTs != null) {
            o.setCreatedAt(createdTs.toLocalDateTime());
        }

        Timestamp updatedTs = rs.getTimestamp("updatedAt");
        if (updatedTs != null) {
            o.setUpdatedAt(updatedTs.toLocalDateTime());
        }
        o.setReceiveEmail(rs.getString("receiveEmail"));
        return o;
    }

    public void markOrderForManualSync(String orderId, String reason) throws SQLException {
        String sql = "UPDATE [Order] SET needsGhnSync = 1, ghnSyncError = ? WHERE orderId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, reason);
            ps.setString(2, orderId);
            ps.executeUpdate();
        }
        System.out.println("Order " + orderId + " marked for manual GHN sync. Reason: " + reason);
    }

    public List<Order> getUnassignedOrders() {
        List<Order> list = new ArrayList<>();
        String sql = """
            SELECT O.*
            FROM [Order] O
            LEFT JOIN OrderAssignment OA ON O.orderId = OA.orderId
            WHERE OA.orderId IS NULL
            ORDER BY O.createdAt ASC
            """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractOrder(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}