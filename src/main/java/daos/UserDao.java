package daos;

import models.UserWithOrderCount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends DBContext {

    public List<UserWithOrderCount> getStaffWithOrderCount() {
        List<UserWithOrderCount> list = new ArrayList<>();
        String sql = """
            SELECT 
                U.userId,
                U.fullName,
                U.email,
                COUNT(OA.orderId) AS totalOrders
            FROM 
                [User] U
            JOIN 
                [Setting] S ON U.roleId = S.settingId
            LEFT JOIN 
                OrderAssignment OA ON U.userId = OA.assignedTo
            WHERE 
                S.name = 'Staff'
            GROUP BY 
                U.userId, U.fullName, U.email
            ORDER BY 
                totalOrders ASC
            """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("userId");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                int totalOrders = rs.getInt("totalOrders");

                list.add(new UserWithOrderCount(userId, fullName, email, totalOrders));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
