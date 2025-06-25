// OrderService.java
package services;

import daos.OrderDAO;
import models.Order;
import models.OrderItem;

import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    public int createOrderWithItems(Order order, List<OrderItem> items) {
        return orderDAO.insertOrderWithItems(order, items);
    }

    public Order getOrderById(int orderId) {
        return orderDAO.getOrderById(orderId);
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        return orderDAO.getOrderItemsByOrderId(orderId);
    }
}
