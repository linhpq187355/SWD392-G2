<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Modern Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #74ebd5, #ACB6E5);
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 1rem;
        }

        .login-container {
            background: #ffffff;
            padding: 2.5rem 2rem;
            border-radius: 20px;
            box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }

        .login-form h2 {
            text-align: center;
            margin-bottom: 2rem;
            color: #333;
        }

        .login-form input[type="email"],
        .login-form input[type="password"] {
            width: 100%;
            padding: 0.75rem;
            margin-bottom: 1.2rem;
            border: 1px solid #ccc;
            border-radius: 10px;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }

        .login-form input:focus {
            border-color: #74ebd5;
            outline: none;
        }

        .actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 0.9rem;
            margin-bottom: 1.5rem;
        }

        .actions a {
            text-decoration: none;
            color: #508bfc;
        }

        .btn-login {
            width: 100%;
            padding: 0.75rem;
            background-color: #508bfc;
            color: white;
            font-size: 1rem;
            border: none;
            border-radius: 12px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .btn-login:hover {
            background-color: #3a6edb;
        }

        .social-login {
            text-align: center;
            margin-top: 2rem;
        }

        .google-button {
            background-color: #dd4b39;
            color: white;
            border: none;
            padding: 0.6rem 1.2rem;
            border-radius: 8px;
            font-size: 0.95rem;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s;
        }

        .google-button:hover {
            background-color: #c23321;
            transform: scale(1.05);
        }

        .google-button:focus {
            outline: none;
        }

        a.google-link {
            text-decoration: none;
        }
    </style>
</head>
<body>
<div class="login-container">
    <form method="post" action="${pageContext.request.contextPath}/login" autocomplete="off">
        <h2>Login</h2>

        <input type="email" id="email" name="email" placeholder="Enter your email" required AUTOCOMPLETE="OFF" />
        <input type="password" id="password" name="password" placeholder="Enter your password" required autocomplete="OFF" />

        <div class="actions">
            <label><input type="checkbox" /> Remember me</label>
            <a href="#">Forgot password?</a>
        </div>

        <button type="submit" class="btn-login">Sign In</button>

        <div class="social-login">
            <% String error = (String) request.getAttribute("error"); %>
            <% if (error != null) { %>
            <p style="color: red; text-align: center; margin-bottom: 1rem;"><%= error %></p>
            <% } %>

            <a href="https://accounts.google.com/o/oauth2/auth?scope=openid%20email%20profile
&redirect_uri=http://localhost:8080/SWD392-G2/login-google
&response_type=code
&client_id=749837398859-74d9j1f6b6cl0ign9bah52igbe8s2q10.apps.googleusercontent.com
&approval_prompt=force
&access_type=offline" class="google-link">
                <div class="google-button">Login with Google</div>
            </a>
        </div>

</body>
</html>
