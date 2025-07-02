package daos;

import models.User;
import models.UserWithOrderCount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DBContext {

    public boolean loginUser(User user) {
        String sql = "SELECT * FROM [User] WHERE email = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User findByGoogleId(String googleId) {
        String sql = "SELECT * FROM [User] WHERE googleId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, googleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("fullName"));
                user.setGoogleId(rs.getString("googleId"));
                user.setRoleId(rs.getInt("roleId"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertGoogleUser(User user) {
        String sql = "INSERT INTO [User] (email, password, phone, gender, fullName, roleId, googleId, createdAt, updatedAt) " +
                "VALUES (?, NULL, NULL, NULL, ?, ?, ?, GETDATE(), GETDATE())";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());         // email
            ps.setString(2, user.getFullName());      // fullName
            ps.setInt(3, user.getRoleId());           // roleId
            ps.setString(4, user.getGoogleId());      // googleId
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM [User] WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setGender(rs.getString("gender"));
                user.setFullName(rs.getString("fullName"));
                user.setRoleId(rs.getInt("roleId"));
                user.setGoogleId(rs.getString("googleId"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(User user) {
        String sql = "INSERT INTO [User] (email, password, fullName, roleId, createdAt, updatedAt) VALUES (?, ?, ?, ?, GETDATE(), GETDATE())";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setInt(4, user.getRoleId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateGoogleId(User user) {
        String sql = "UPDATE [User] SET googleId = ? WHERE userId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getGoogleId());
            ps.setInt(2, user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean updateUser(User user) {
        String sql = "UPDATE [User] SET fullName=?, phone=?, gender=?, updatedAt=GETDATE() WHERE userId=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getGender());
            ps.setInt(4, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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
        LEFT JOIN
            [Order] O ON OA.orderId = O.orderId
        LEFT JOIN
            [Setting] OS ON O.statusId = OS.settingId
        WHERE
            S.name = 'Sales'
            AND OS.name IN ('Pending', 'Confirmed', 'Shipping') -- Chỉ đếm các đơn hàng đang được xử lý
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
