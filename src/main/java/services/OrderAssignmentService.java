package services;

import daos.OrderAssignmentDAO;
import daos.OrderDAO;
import daos.UserDAO;
import models.Order;
import models.UserWithOrderCount;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderAssignmentService {

    private final UserDAO userDao = new UserDAO();
    private final OrderDAO orderDao = new OrderDAO();
    private final OrderAssignmentDAO assignmentDao = new OrderAssignmentDAO();

    public void autoAssignOrders() {
        List<UserWithOrderCount> staffs = userDao.getStaffWithOrderCount();
        System.out.println(staffs);
        List<Order> unassignedOrders = orderDao.getUnassignedOrders();

        // Sắp xếp nhân viên một lần theo số lượng đơn hàng tăng dần
        // và lọc những người có thể gán
        List<UserWithOrderCount> availableStaffs = staffs.stream()
                .filter(staff -> staff.getTotalOrders() < 5)
                .sorted(Comparator.comparingInt(UserWithOrderCount::getTotalOrders))
                .collect(Collectors.toList());

        int currentStaffIndex = 0;

        for (Order order : unassignedOrders) {
            UserWithOrderCount selectedStaff = null;

            while (currentStaffIndex < availableStaffs.size()) {
                UserWithOrderCount staff = availableStaffs.get(currentStaffIndex);
                if (staff.getTotalOrders() < 5) {
                    selectedStaff = staff;
                    break;
                }
                currentStaffIndex++;
            }

            if (selectedStaff != null) {
                boolean success = assignmentDao.assignStaffToOrder(order.getOrderId(), selectedStaff.getUserId(), null, 1);
                if (success) {
                    selectedStaff.setTotalOrders(selectedStaff.getTotalOrders() + 1);
                    System.out.println("Assigned Order " + order.getOrderId() + " to Staff: " + selectedStaff.getFullName());

                    if (selectedStaff.getTotalOrders() >= 5) {
                        currentStaffIndex++;
                    }
                } else {
                    System.out.println("Failed to assign Order " + order.getOrderId());
                }
            } else {
                System.out.println("No available staff for Order " + order.getOrderId());
                break;
            }
        }
    }
}
