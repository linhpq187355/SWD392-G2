<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Modern Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet" />
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(145deg, #e0f7fa, #f1f8ff);
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
            box-shadow: 0 20px 50px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 420px;
        }

        .login-container h2 {
            text-align: center;
            margin-bottom: 1.8rem;
            font-size: 1.8rem;
            color: #333;
        }

        .login-form input {
            width: 100%;
            padding: 0.9rem;
            margin-bottom: 1.3rem;
            border: 1px solid #ccc;
            border-radius: 12px;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }

        .login-form input:focus {
            border-color: #00acc1;
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
            padding: 0.9rem;
            background-color: #008cba;
            color: white;
            font-size: 1.05rem;
            font-weight: 600;
            border: none;
            border-radius: 12px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .btn-login:hover {
            background-color: #007bb5;
        }

        .social-login {
            text-align: center;
            margin-top: 2rem;
        }

        .google-button {
            display: inline-block;
            background-color: #dd4b39;
            color: white;
            border: none;
            padding: 0.75rem 1.2rem;
            border-radius: 10px;
            font-size: 1rem;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s;
        }

        .google-button:hover {
            background-color: #c23321;
            transform: scale(1.05);
        }

        .error-msg {
            color: red;
            text-align: center;
            margin-top: 1rem;
            font-size: 0.95rem;
        }
    </style>
</head>
<body>
<div class="login-container">
    <form method="post" action="${pageContext.request.contextPath}/login" class="login-form" autocomplete="off">
        <h2>Welcome</h2>

        <input type="email" name="email" placeholder="Email address" required />
        <input type="password" name="password" placeholder="Password" required />

        <div class="actions">
            <label><input type="checkbox" /> Remember me</label>
            <a href="#">Forgot password?</a>
        </div>

        <button type="submit" class="btn-login">Sign In</button>

        <c:if test="${not empty error}">
            <div class="error-msg">${error}</div>
        </c:if>

        <div class="social-login">
            <a href="https://accounts.google.com/o/oauth2/auth?scope=openid%20email%20profile
&redirect_uri=http://localhost:9999/SWD392_G2_war_exploded/google-login
&response_type=code
&client_id=749837398859-0v26hcmekbpe0t9b3sgs7ce15pmfqufr.apps.googleusercontent.com
&approval_prompt=force&access_type=offline" class="google-button">
                Sign in with Google
            </a>
        </div>
    </form>
</div>
</body>
</html>
