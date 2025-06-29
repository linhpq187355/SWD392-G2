package services;


import daos.SettingDAO;
import models.Category;

import java.util.List;

public class SettingService {
    SettingDAO settingDAO = new SettingDAO();
    public List<Category> getAllCategories() {
        return settingDAO.getAllCategory();
    }
}
