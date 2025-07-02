package daos;

import models.Address;

import java.sql.*;

public class AddressDAO extends DBContext {

    public boolean updateAddress(Address address)  {
        String sql = "UPDATE [Address] SET addressDetail = ?, updatedAt = GETDATE() WHERE userId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, address.getAddressDetail());
            ps.setInt(2, address.getUserId());
            return ps.executeUpdate() > 0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertAddress(Address address){
        String sql = "INSERT INTO [Address] (userId, addressDetail, createdAt) VALUES (?, ?, GETDATE())";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, address.getUserId());
            ps.setString(2, address.getAddressDetail());
            return ps.executeUpdate() > 0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addressExists(int userId) {
        String sql = "SELECT 1 FROM [Address] WHERE userId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveOrUpdate(Address address)  {
        if (addressExists(address.getUserId())) {
            return updateAddress(address);
        } else {
            return insertAddress(address);
        }
    }
}
