package services;

import daos.SettingDao;
import models.Setting;
import java.util.List;

public class SettingService {
    private final SettingDao settingDao;

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
