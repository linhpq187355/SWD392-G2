// OrderService.java
package services;

import daos.OrderDAO;
import models.Order;
import models.OrderItem;
import models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }



    public String createOrderForUser(Order order, List<OrderItem> items) {
        String orderId = orderDAO.insertOrder(order);
        for (OrderItem item : items) {
            item.setOrderId(orderId);
        }
        orderDAO.insertOrderItems(items);
        return orderId;
    }

    public String createOrderForGuest(Order order, Map<Product, Integer> productMap) {
        String orderId = orderDAO.insertOrder(order);

        List<OrderItem> items = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setProductId(product.getId());
            item.setQuantity(quantity);
            item.setUnitPrice(product.getSalePrice());

            items.add(item);
        }

        orderDAO.insertOrderItems(items);
        return orderId;
    }

}
