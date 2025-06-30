package daos;

import models.Setting;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SettingDao extends DBContext {

    // Lấy danh sách setting theo type (province, district, ward)
    public List<Setting> getSettingsByType(String type) {
        List<Setting> list = new ArrayList<>();
        String sql = "SELECT * FROM Setting WHERE type = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractSetting(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách setting con theo parentId (VD: các huyện của 1 tỉnh)
    public List<Setting> getSettingsByParentId(int parentId) {
        List<Setting> list = new ArrayList<>();
        String sql = "SELECT * FROM Setting WHERE parentId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, parentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractSetting(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Setting extractSetting(ResultSet rs) throws SQLException {
        Setting s = new Setting();
        s.setSettingId(rs.getInt("settingId"));
        s.setParentId(rs.getInt("parentId"));
        s.setName(rs.getString("name"));
        s.setType(rs.getString("type"));
        // Thêm các trường khác nếu cần
        return s;
    }

    // Tìm theo tên và type (ví dụ: Tỉnh có name = "Hà Nội" và type = "province")
    public Setting findByNameAndType(String name, String type) {
        String sql = "SELECT * FROM Setting WHERE name = ? AND type = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, type);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractSetting(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm theo tên và parentId (ví dụ: Quận "Ba Đình" thuộc tỉnhId = 1)
    public Setting findByNameAndParent(String name, int parentId) {
        String sql = "SELECT * FROM Setting WHERE name = ? AND parentId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, parentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractSetting(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Setting getSettingById(int id) {
        String sql = "SELECT * FROM Setting WHERE settingId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractSetting(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
