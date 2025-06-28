package daos;

import models.User;
import java.sql.*;

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

}
