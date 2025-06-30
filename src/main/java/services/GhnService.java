package services;

import models.Order;

// Lớp giả lập cho việc tích hợp với GHN
public class GhnService {

    /**
     * Gửi yêu cầu cập nhật thông tin đơn hàng đến GHN.
     * @param order Đối tượng Order chứa thông tin mới.
     * @throws GhnException Khi có lỗi từ API của GHN.
     */
    public void updateOrderOnGhn(Order order) throws GhnException {
        System.out.println("Connecting to GHN API to update order: " + order.getOrderId());

        // --- Logic gọi API thật của GHN sẽ nằm ở đây ---
        // Ví dụ: sử dụng HttpClient để gửi POST/PUT request

        // Giả lập một số trường hợp lỗi
        if ("GHN_FAIL".equals(order.getNote())) {
            throw new GhnException("Invalid parameter from GHN: consignee name is required.");
        }
        if ("GHN_UNAVAILABLE".equals(order.getNote())) {
            throw new GhnException("GHN service is currently unavailable.", true); // Lỗi hệ thống
        }

        // Giả lập thành công
        System.out.println("GHN API: Order " + order.getOrderId() + " updated successfully.");
    }
}

