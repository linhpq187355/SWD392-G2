<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Register Account</title>
    <link rel="stylesheet" href="register.css" />
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
<style>
    * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
    }

    body {
        font-family: 'Poppins', sans-serif;
        background: linear-gradient(to right, #74ebd5, #ACB6E5);
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .register-container {
        background: white;
        padding: 2.5rem;
        border-radius: 20px;
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
        width: 100%;
        max-width: 600px;
    }

    .register-form h2 {
        text-align: center;
        margin-bottom: 1.8rem;
        color: #333;
    }

    .input-group {
        display: flex;
        gap: 1rem;
        margin-bottom: 1.2rem;
    }

    .input-group input,
    .input-group select {
        flex: 1;
        padding: 0.75rem;
        border-radius: 8px;
        border: 1px solid #ccc;
        font-size: 1rem;
    }

    .input-group input:focus,
    .input-group select:focus {
        border-color: #508bfc;
        outline: none;
    }

    .btn-register {
        width: 100%;
        padding: 0.75rem;
        background-color: #508bfc;
        color: white;
        border: none;
        border-radius: 10px;
        font-size: 1rem;
        margin-top: 0.8rem;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    .btn-register:hover {
        background-color: #3a6edb;
    }

    .divider {
        text-align: center;
        margin: 1rem 0;
        color: #888;
        font-size: 0.9rem;
    }

    .btn-google {
        width: 100%;
        padding: 0.7rem;
        background-color: #dd4b39;
        color: white;
        border: none;
        border-radius: 10px;
        font-weight: bold;
        cursor: pointer;
    }

    .login-link {
        font-size: 0.9rem;
        text-align: center;
        margin-top: 0.5rem;
    }

    .login-link a {
        color: #508bfc;
        text-decoration: none;
    }

</style>
</head>
<body>
<div class="register-container">
    <form class="register-form">
        <h2>Register Account</h2>
        <div class="input-group">
            <input type="email" placeholder="Email" required />
        </div>

        <div class="input-group">
            <input type="password" placeholder="Password" required />
            <input type="password" placeholder="Confirm Password" required />
        </div>

        <p class="login-link">Already have an account? <a href="login.jsp">Login</a></p>

        <button type="submit" class="btn-register">Register Now</button>

        <div class="divider">or</div>
        <div class="social-login">
            
            <a href="https://accounts.google.com/o/oauth2/auth?scope=openid%20email%20profile
&redirect_uri=http://localhost:8080/SWD392-G2/login-google
&response_type=code
&client_id=749837398859-74d9j1f6b6cl0ign9bah52igbe8s2q10.apps.googleusercontent.com
&approval_prompt=force
&access_type=offline" class="google-link">
                <div class="google-button">Register with Google</div>
            </a>
        </div>
    </form>
</div>
</body>
</html>
