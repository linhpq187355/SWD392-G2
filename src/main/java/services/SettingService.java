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
        this.settingDao = new SettingDao();
    }

    public List<Setting> getAllProvinces() {
        return settingDao.getSettingsByType("province");
    }

    public List<Setting> getDistrictsByProvinceId(int provinceId) {
        return settingDao.getSettingsByParentId(provinceId);
    }

    public List<Setting> getWardsByDistrictId(int districtId) {
        return settingDao.getSettingsByParentId(districtId);
    }

    public List<Setting> getOrderStatuses() {
        return settingDao.getSettingsByType("order_status");
    }

    public String getLocationNameById(int id) {
        Setting setting = settingDao.getSettingById(id);
        return setting != null ? setting.getName() : "";
    }

}
