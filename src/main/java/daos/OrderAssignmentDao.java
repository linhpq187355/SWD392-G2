package daos;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;

public class OrderAssignmentDao extends DBContext {

    /**
     * Gán staff với order.
     *
     * @param orderId ID của Order
     * @param staffId ID của User là Staff
     * @param assignedBy ID người gán (Manager/Staff khác)
     * @param assignmentTypeId ID loại assignment (ví dụ 1 = Assigned)
     * @return true nếu thành công, false nếu lỗi
     */
    public boolean assignStaffToOrder(String orderId, int staffId, Integer assignedBy, int assignmentTypeId) {
        String sql = """
            INSERT INTO OrderAssignment (orderId, assignedTo, assignedBy, assignmentTypeId, assignedAt)
            VALUES (?, ?, ?, ?, ?)
            """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, orderId);
            ps.setInt(2, staffId);
            if (assignedBy == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, assignedBy);
            }
            ps.setInt(4, assignmentTypeId);
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
