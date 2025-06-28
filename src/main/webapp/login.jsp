<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
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

        .google-button {
            display: block;
            margin: 1.5rem auto 0;
            background-color: #dd4b39;
            color: white;
            padding: 0.75rem 1.2rem;
            border-radius: 10px;
            font-size: 1rem;
            font-weight: 600;
            text-decoration: none;
            text-align: center;
            transition: background-color 0.3s ease, transform 0.2s;
        }

        .google-button:hover {
            background-color: #c23321;
            transform: scale(1.03);
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
    <form method="post" action="${pageContext.request.contextPath}/login" class="login-form">
        <h2>Welcome Back</h2>

        <input type="email" name="email" placeholder="Email address" required />
        <input type="password" name="password" placeholder="Password" required />
        <button type="submit" class="btn-login">Sign In</button>

        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
        <div class="error-msg"><%= error %></div>
        <% } %>

        <a href="https://accounts.google.com/o/oauth2/auth?scope=openid%20email%20profile&redirect_uri=http://localhost:8080/SWD392_G2_war_exploded/login-google&response_type=code&client_id=749837398859-0v26hcmekbpe0t9b3sgs7ce15pmfqufr.apps.googleusercontent.com&approval_prompt=force&access_type=offline"
           class="google-button">
            Sign in with Google
        </a>
    </form>
</div>
</body>
</html>