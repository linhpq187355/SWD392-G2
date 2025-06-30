package services;

import daos.OrderAssignmentDao;
import daos.OrderDao;
import daos.UserDao;
import models.Order;
import models.UserWithOrderCount;

import java.util.Comparator;
import java.util.List;

public class OrderAssignmentService {

    private final UserDao userDao = new UserDao();
    private final OrderDao orderDao = new OrderDao();
    private final OrderAssignmentDao assignmentDao = new OrderAssignmentDao();

    /**
     * Tự động gán order chưa gán cho staff có ít order nhất.
     * Mỗi staff không vượt quá 5 order đang được gán.
     */
    public void autoAssignOrders() {
        List<UserWithOrderCount> staffs = userDao.getStaffWithOrderCount();
        List<Order> unassignedOrders = orderDao.getUnassignedOrders();

        for (Order order : unassignedOrders) {
            // Chọn staff phù hợp
            UserWithOrderCount selectedStaff = staffs.stream()
                    .filter(staff -> staff.getTotalOrders() < 5)
                    .min(Comparator.comparingInt(UserWithOrderCount::getTotalOrders))
                    .orElse(null);

            if (selectedStaff != null) {
                boolean success = assignmentDao.assignStaffToOrder(order.getOrderId(), selectedStaff.getUserId(), null, 1);
                if (success) {
                    // Tăng bộ đếm local để tránh gán vượt quá 5
                    selectedStaff.setTotalOrders(selectedStaff.getTotalOrders() + 1);
                    System.out.println("Assigned Order " + order.getOrderId() + " to Staff: " + selectedStaff.getFullName());
                } else {
                    System.out.println("Failed to assign Order " + order.getOrderId());
                }
            } else {
                System.out.println("No available staff for Order " + order.getOrderId());
            }
        }
    }
}
