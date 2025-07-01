package services;

import daos.UserDAO;
import models.User;
import validators.UserValidator;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private UserDAO userDAO = new UserDAO();
    static UserValidator userValidator = new UserValidator();
    private static final Map<String, String> users = new HashMap<>();
    static {
        users.put("test@gmail.com", "123456");
        users.put("admin@example.com", "admin123");
    }

    public static boolean isValidUser(String email, String password) {
        return users.containsKey(email) && users.get(email).equals(password);
    }

        public boolean register(User user) {
            if(userValidator.isValid(user)) {
                return false;
            }

            return userDAO.registerUser(user);
        }

        public boolean login(User user) {
            return userDAO.loginUser(user);
        }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }
    }



