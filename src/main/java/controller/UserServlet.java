package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import services.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebServlet(name = "UserServlet", urlPatterns = {"/login", "/register", "/logout", "/google-login", "/update-profile"})
public class UserServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private static String CLIENT_ID;
    private static String CLIENT_SECRET;
    private static String REDIRECT_URI;

    static {
        try (InputStream input = UserServlet.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot find config.properties");
            }
            Properties props = new Properties();
            props.load(input);

            CLIENT_ID = props.getProperty("google.CLIENT_ID");
            CLIENT_SECRET = props.getProperty("google.CLIENT_SECRET");
            REDIRECT_URI = props.getProperty("google.REDIRECT_URI");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties", e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/logout":
                handleLogout(req, resp);
                break;
            case "/google-login":
                handleGoogleLogin(req, resp);
                break;
            default:
                resp.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/login":
                handleLogin(req, resp);
                break;
            case "/register":
                handleRegister(req, resp);
                break;
            case "/update-profile":
                handleUpdateProfile(req, resp);
                break;
            default:
                resp.sendRedirect("login.jsp");
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        if (userService.login(user)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/home-page.jsp");
        } else {
            req.setAttribute("error", "Email hoặc mật khẩu không chính xác");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        String error = userService.registerUser(fullName, email, password, confirmPassword);
        if (error != null) {
            req.setAttribute("error", error);
        } else {
            req.setAttribute("success", "Đăng ký thành công! Bạn có thể đăng nhập.");
        }
        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    private void handleGoogleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        if (code == null || code.isEmpty()) {
            resp.sendRedirect("login.jsp");
            return;
        }

        User user = userService.handleGoogleLogin(code, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("user", user);

        if (user.getRoleId() == 1) {
            resp.sendRedirect("admin.jsp");
        } else {
            resp.sendRedirect("home-page.jsp");
        }
    }

    private void handleUpdateProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String gender = req.getParameter("gender");
        String addressDetail = req.getParameter("addressDetail");

        boolean success = userService.updateProfile(user, fullName, phone, gender, addressDetail);
        if (success) {
            session.setAttribute("user", user);
            resp.sendRedirect("profile.jsp?success=true");
        } else {
            req.setAttribute("error", "Cập nhật thất bại.");
            req.getRequestDispatcher("updateProfile.jsp").forward(req, resp);
        }
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        resp.sendRedirect("login.jsp");
    }
}