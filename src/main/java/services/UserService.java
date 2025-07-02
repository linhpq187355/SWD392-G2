package services;

import daos.*;
import models.Address;
import models.Order;
import models.Setting;
import models.User;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserService {
    private final UserDAO userDAO = new UserDAO();
    private final AddressDAO addressDAO = new AddressDAO();
    private final SettingDAO settingDAO = new SettingDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderAssignmentDAO orderAssignmentDAO = new OrderAssignmentDAO();

    public boolean login(User user) {
        User dbUser = userDAO.findByEmail(user.getEmail());
        if (dbUser != null && dbUser.getPassword().equals(user.getPassword())) {
            user.setUserId(dbUser.getUserId());
            user.setFullName(dbUser.getFullName());
            user.setRoleId(dbUser.getRoleId());
            return true;
        }
        return false;
    }

    public String getUserRole(User user) {
        String email = user.getEmail();
        User dbUser = userDAO.findByEmail(email);
        if (dbUser != null) {
            Setting setting = settingDAO.getSettingById(dbUser.getRoleId());
            return setting.getName();
        } else {
            return null;
        }
    }

    public boolean checkRoleForUpdateOrder(int userId, String role, String orderId){
        if(role.equals("Customer")) {
            Order order = orderDAO.getOrderById(orderId);
            return order.getUserId() == userId;
        }
        if(role.equals("Sales")) {
            return orderAssignmentDAO.isOrderAssignedToSales(orderId, userId);
        }
        return false;
    }

    public String registerUser(String fullName, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp";
        }

        if (userDAO.findByEmail(email) != null) {
            return "Email đã được sử dụng";
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoleId(2); // Mặc định role khách hàng

        userDAO.insert(user);
        return null; // Thành công
    }

    public User handleGoogleLogin(String code, String clientId, String clientSecret, String redirectUri) {
        try {
            String token = getAccessToken(code, clientId, clientSecret, redirectUri);
            if (token == null) return null;

            User googleUser = getUserInfo(token);
            if (googleUser == null || googleUser.getGoogleId() == null) return null;

            User existingUser = userDAO.findByGoogleId(googleUser.getGoogleId());

            if (existingUser == null) {
                User emailUser = userDAO.findByEmail(googleUser.getEmail());
                if (emailUser != null) {
                    emailUser.setGoogleId(googleUser.getGoogleId());
                    userDAO.updateGoogleId(emailUser);
                    return emailUser;
                } else {
                    googleUser.setRoleId(2); // mặc định khách hàng
                    userDAO.insertGoogleUser(googleUser);
                    return userDAO.findByGoogleId(googleUser.getGoogleId());
                }
            }

            return existingUser;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getAccessToken(String code, String clientId, String clientSecret, String redirectUri) throws IOException {
        URL url = new URL("https://oauth2.googleapis.com/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String params = "code=" + code
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&redirect_uri=" + redirectUri
                + "&grant_type=authorization_code";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes());
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        JSONObject json = new JSONObject(response.toString());
        return json.optString("access_token", null);
    }

    private User getUserInfo(String accessToken) throws IOException {
        URL url = new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        JSONObject json = new JSONObject(response.toString());

        User user = new User();
        user.setGoogleId(json.optString("id", null));
        user.setEmail(json.optString("email", null));
        user.setFullName(json.optString("name", null));
        return user;
    }

    public boolean updateProfile(User user, String fullName, String phone, String gender, String addressDetail) {
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setGender(gender);

        Address address = user.getAddress();
        if (address == null) {
            address = new Address();
            address.setUserId(user.getUserId());
        }
        address.setAddressDetail(addressDetail);
        user.setAddress(address);

        boolean userUpdated = userDAO.updateUser(user);
        boolean addressUpdated = addressDAO.saveOrUpdate(address);

        return userUpdated && addressUpdated;
    }
}
