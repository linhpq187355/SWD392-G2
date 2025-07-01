package daos;

import models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserDAO extends DBContext {
    // register account
    public boolean registerUser(User user) {
        String sql = "INSERT INTO [User] (email, password,roleId) " +
                "VALUES (?, ?, ?)";
        try (
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getRoleId());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public User getUserByEmail(String email) {
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

                user.setWardId(rs.getString("ward_id"));

                user.setAddressDetail(rs.getString("addressDetail"));

                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                user.setProvinceId(rs.getInt("province_id"));
                user.setDistrictId(rs.getInt("district_id"));

                if (createdAt != null) {
                    user.setCreatedAt(createdAt.toLocalDateTime());
                }
                if (updatedAt != null) {
                    user.setUpdatedAt(updatedAt.toLocalDateTime());
                }

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean loginUser(User user) {
        String sql = "SELECT * FROM [User] WHERE email = ? AND password = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

