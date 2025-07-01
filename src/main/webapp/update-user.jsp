<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update Profile</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f4f4;
            padding: 2rem;
        }

        .profile-container {
            max-width: 500px;
            margin: auto;
            background: #fff;
            padding: 2rem;
            border-radius: 15px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }

        h2 {
            text-align: center;
            margin-bottom: 1.5rem;
        }

        .profile-picture {
            text-align: center;
            margin-bottom: 1rem;
        }

        .profile-picture img {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            background: #ddd;
        }

        .form-group {
            margin-bottom: 1rem;
        }

        label {
            display: block;
            margin-bottom: 0.4rem;
            font-weight: bold;
        }

        input[type="text"],
        input[type="email"],
        input[type="password"] {
            width: 100%;
            padding: 0.6rem;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 1rem;
        }

        .form-row {
            display: flex;
            justify-content: space-between;
            gap: 1rem;
        }

        .form-row .form-group {
            flex: 1;
        }

        .btn-submit {
            width: 100%;
            padding: 0.8rem;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 1rem;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .btn-submit:hover {
            background-color: #3e8e41;
        }
    </style>
</head>
<body>
<div class="profile-container">
    <h2>Update User Profile</h2>
    <div class="profile-picture">
        <img src="https://www.w3schools.com/howto/img_avatar.png" alt="Avatar">
    </div>
    <form action="update-profile" method="post">
        <div class="form-group">
            <label for="fullname">Full Name</label>
            <input type="text" id="fullname" name="fullname" placeholder="Nguyen Van A" required>
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" placeholder="your@email.com" required>
        </div>
        <div class="form-group">
            <label for="phone">Contact Number</label>
            <input type="text" id="phone" name="phone" placeholder="0123456789" required>
        </div>
        <div class="form-group">
            <label for="address">Address</label>
            <input type="text" id="address" name="address" placeholder="123 Street">
        </div>
        <div class="form-row">
            <div class="form-group">
                <label for="city">City</label>
                <input type="text" id="city" name="city" placeholder="Ha Noi">
            </div>
            <div class="form-group">
                <label for="country">Country</label>
                <input type="text" id="country" name="country" placeholder="Viet Nam">
            </div>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" placeholder="********" required>
        </div>
        <button type="submit" class="btn-submit">Save Update</button>
    </form>
</div>
</body>
</html>
