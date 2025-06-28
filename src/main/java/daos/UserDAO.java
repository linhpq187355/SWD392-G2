package daos;

import models.User;

import java.sql.*;

public class UserDAO extends DBContext {
    // register account

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

