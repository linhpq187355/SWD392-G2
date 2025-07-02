<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register Account</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background: #f0f4f8;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .register-box {
            background: white;
            padding: 2.5rem 3rem;
            border-radius: 20px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            width: 500px;
            max-width: 90%;
        }

        h2 {
            text-align: center;
            margin-bottom: 1.5rem;
            color: #1a73e8;
        }

        .form-group {
            display: flex;
            gap: 1rem;
            margin-bottom: 1rem;
        }

        .form-group input {
            flex: 1;
            padding: 0.75rem;
            border: 1px solid #ccc;
            border-radius: 10px;
            font-size: 1rem;
        }

        .single-input {
            width: 100%;
            padding: 0.75rem;
            margin-bottom: 1rem;
            border: 1px solid #ccc;
            border-radius: 10px;
            font-size: 1rem;
        }

        .btn-register {
            width: 100%;
            background-color: #4CAF50;
            color: white;
            padding: 0.8rem;
            font-size: 1rem;
            font-weight: 600;
            border: none;
            border-radius: 10px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .btn-register:hover {
            background-color: #388e3c;
        }

        .bottom-links {
            text-align: center;
            margin-top: 1rem;
            font-size: 0.95rem;
        }

        .bottom-links a {
            color: #1a73e8;
            text-decoration: none;
            font-weight: 600;
        }

        .google-button {
            display: block;
            text-align: center;
            background-color: #dd4b39;
            color: white;
            padding: 0.7rem;
            border-radius: 10px;
            font-weight: 600;
            text-decoration: none;
            margin-top: 1rem;
            transition: background-color 0.3s ease;
        }

        .google-button:hover {
            background-color: #c23321;
        }

        .message {
            text-align: center;
            margin-top: 1rem;
            font-size: 0.95rem;
        }

        .message.error {
            color: red;
        }

        .message.success {
            color: green;
        }
    </style>
</head>
<body>
<div class="register-box">
    <h2>Register account</h2>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <div class="form-group">
            <input type="text" name="firstName" placeholder="Name" required>
            <input type="text" name="lastName" placeholder="Last Name" required>
        </div>
        <div class="form-group">
            <input type="email" name="email" placeholder="Email" required>
        </div>
        <div class="form-group">
            <input type="password" name="password" placeholder="********" required>
            <input type="password" name="confirmPassword" placeholder="********" required>
        </div>
        <button type="submit" class="btn-register">Register now</button>
    </form>

    <div class="bottom-links">
        Already have an account? <a href="login.jsp">Login</a>
    </div>

    <a href="https://accounts.google.com/o/oauth2/auth?scope=openid%20email%20profile&redirect_uri=http://localhost:9999/SWD392_G2_war_exploded/user?action=google&response_type=code&client_id=749837398859-0v26hcmekbpe0t9b3sgs7ce15pmfqufr.apps.googleusercontent.com&approval_prompt=force&access_type=offline" class="google-button">
        Or Login with Google
    </a>

    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="message success">${success}</div>
    </c:if>
</div>
</body>
</html>
