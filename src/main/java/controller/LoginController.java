package controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import models.User;
import services.UserService;

@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    private final UserService userService = new UserService(); // <- THÊM DÒNG NÀY
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User loginAttempt = new User();
        loginAttempt.setEmail(email);
        loginAttempt.setPassword(password);

        if (userService.login(loginAttempt)) {
            User fullUser = userService.getUserByEmail(email);
            if (fullUser != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", fullUser);

                response.sendRedirect(request.getContextPath() + "/homePage");
                return;
            }
        }

        request.setAttribute("error", "Invalid email or password");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

}


