package services;


import daos.SettingDAO;
import models.Category;
import models.Setting;

import java.util.List;

public class SettingService {
    SettingDAO settingDAO = new SettingDAO();
    public List<Category> getAllCategories() {
        return settingDAO.getAllCategory();
    }
  public SettingService() {
    }

    public List<Setting> getAllProvinces() {
        return settingDAO.getSettingsByType("province");
    }

    public List<Setting> getDistrictsByProvinceId(int provinceId) {
        return settingDAO.getSettingsByParentId(provinceId);
    }

    public List<Setting> getWardsByDistrictId(int districtId) {
        return settingDAO.getSettingsByParentId(districtId);
    }

    public List<Setting> getOrderStatuses() {
        return settingDAO.getSettingsByType("order_status");
    }

    public String getLocationNameById(int id) {
        Setting setting = settingDAO.getSettingById(id);
        return setting != null ? setting.getName() : "";
    }

}
