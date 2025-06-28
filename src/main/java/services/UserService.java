package services;

import daos.UserDAO;
import models.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final Map<String, String> users = new HashMap<>();
    static {
        users.put("test@gmail.com", "123456");
        users.put("admin@example.com", "admin123");
    }

    public static boolean isValidUser(String email, String password) {
        return users.containsKey(email) && users.get(email).equals(password);
    }
        private final UserDAO userDAO = new UserDAO();


        public boolean login(User user) {
            return userDAO.loginUser(user);
        }


    }



