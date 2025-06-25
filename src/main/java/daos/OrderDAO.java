// OrderDAO.java
package daos;

import models.Order;
import models.OrderItem;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends DBContext {

    public int insertOrder(Order order) {
        String sql = "INSERT INTO [Order](userId, status, receiveName, receivePhone, receiveAddress, shippingMethod, note, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserId());
            ps.setString(2, order.getStatus());
            ps.setString(3, order.getReceiveName());
            ps.setString(4, order.getReceivePhone());
            ps.setString(5, order.getReceiveAddress());
            ps.setString(6, order.getShippingMethod());
            ps.setString(7, order.getNote());
            ps.setTimestamp(8, Timestamp.valueOf(order.getCreatedAt()));
            ps.setTimestamp(9, Timestamp.valueOf(order.getUpdatedAt()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting order", e);
        }
        return 0;
    }

    public void insertOrderItem(OrderItem item) {
        String sql = "INSERT INTO OrderItem (orderId, productId, quantity, unitPrice) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getUnitPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting order item", e);
        }
    }

    public int insertOrderWithItems(Order order, List<OrderItem> items) {
        int orderId = insertOrder(order);
        for (OrderItem item : items) {
            item.setOrderId(orderId);
            insertOrderItem(item);
        }
        return orderId;
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderItem WHERE orderId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("orderItemId"));
                item.setOrderId(rs.getInt("orderId"));
                item.setProductId(rs.getInt("productId"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getDouble("unitPrice"));
                list.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving order items", e);
        }
        return list;
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM [Order] WHERE orderId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("orderId"));
                order.setUserId(rs.getInt("userId"));
                order.setStatus(rs.getString("status"));
                order.setReceiveName(rs.getString("receiveName"));
                order.setReceivePhone(rs.getString("receivePhone"));
                order.setReceiveAddress(rs.getString("receiveAddress"));
                order.setShippingMethod(rs.getString("shippingMethod"));
                order.setNote(rs.getString("note"));
                order.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                order.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                return order;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving order", e);
        }
        return null;
    }
    public static void main(String[] args) {
        OrderDAO dao = new OrderDAO();

        // Create a new Order
        Order order = new Order();
        order.setUserId(2);
        order.setStatus("PENDING");
        order.setReceiveName("John Doe");
        order.setReceivePhone("0901234567");
        order.setReceiveAddress("123 ABC Street");
        order.setShippingMethod("COD");
        order.setNote("Please deliver in the morning");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Create OrderItems
        List<OrderItem> items = new ArrayList<>();

        OrderItem item1 = new OrderItem();
        item1.setProductId(4);
        item1.setQuantity(2);
        item1.setUnitPrice(100000);
        items.add(item1);

        OrderItem item2 = new OrderItem();
        item2.setProductId(5);
        item2.setQuantity(1);
        item2.setUnitPrice(200000);
        items.add(item2);

        // Insert and get generated orderId
        int orderId = dao.insertOrderWithItems(order, items);
        System.out.println("✅ Order created with ID: " + orderId);

        // Retrieve the order back
        Order savedOrder = dao.getOrderById(orderId);
        System.out.println("🧾 Order: " + savedOrder.getReceiveName() + " | " + savedOrder.getReceivePhone());

        List<OrderItem> savedItems = dao.getOrderItemsByOrderId(orderId);
        for (OrderItem oi : savedItems) {
            System.out.printf("🔹 ProductID: %d | Qty: %d | Price: %.0f%n", oi.getProductId(), oi.getQuantity(), oi.getUnitPrice());
        }
    }
}
