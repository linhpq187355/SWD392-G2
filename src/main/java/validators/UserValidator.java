package validators;

import models.User;

public class UserValidator {

        public static boolean isValid(User user) {
            if (user == null) return false;

            if (user.getEmail() == null || user.getEmail().isBlank()) return false;
            if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) return false;

            if (user.getPassword() == null || user.getPassword().isBlank()) return false;
            if (user.getPassword().length() < 6) return false;

            if (user.getFirstName() == null || user.getFirstName().isBlank()) return false;
            if (user.getLastName() == null || user.getLastName().isBlank()) return false;



            return true;
        }
    }


