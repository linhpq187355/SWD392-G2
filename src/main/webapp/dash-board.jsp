<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 6/24/2025
  Time: 6:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dashboard</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700&amp;display=swap" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="assets/style.css"/>
</head>
<body>
<div class="container">
    <aside aria-label="Sidebar navigation" class="sidebar" role="complementary">
        <div style="display:flex; align-items:center; justify-content:space-between;">
            <div aria-label="ShopMe logo" class="logo">
                ShopMe
            </div>
            <div aria-label="Menu toggle button" class="menu-toggle" role="button" tabindex="0">
                <i aria-hidden="true" class="fas fa-bars">
                </i>
            </div>
        </div>
        <div aria-label="Admin profile" class="profile">
            <img alt="Profile picture of admin wearing black and white outfit on pink background" height="80" src="https://storage.googleapis.com/a1aa/image/5fc70398-37f0-4a85-56a0-eec4e59a1970.jpg" width="80"/>
            <div class="profile-name">
                Admin name
            </div>
        </div>
        <nav>
            <ul>
                <li>
                    <a aria-current="page" href="#">
                        <i aria-hidden="true" class="fas fa-home">
                        </i>
                        Home
                    </a>
                </li>
                <li>
                    <a href="#">
                        <i aria-hidden="true" class="fas fa-cube">
                        </i>
                        Products
                    </a>
                </li>
                <li>
                    <a href="#">
                        <i aria-hidden="true" class="fas fa-user">
                        </i>
                        Users
                    </a>
                </li>
                <li>
                    <a href="#">
                        <i aria-hidden="true" class="fas fa-briefcase">
                        </i>
                        Orders
                    </a>
                </li>
                <li>
                    <a href="#">
                        <i aria-hidden="true" class="fas fa-cog">
                        </i>
                        Account
                    </a>
                </li>
            </ul>
        </nav>
    </aside>
    <main class="main-content" role="main">
        <header class="header">
            <div aria-label="Welcome message" class="welcome">
                Welcome back,
                <strong>
                    Anish
                </strong>
                <span aria-hidden="true" class="emoji">
       😎
      </span>
            </div>
            <div aria-label="Current time" class="time">
                Time
            </div>
        </header>
        <section aria-label="Dashboard summary cards" class="cards">
            <article aria-labelledby="products-title" class="card">
                <div class="title" id="products-title">
                    Products
                </div>
                <div class="content">
                    <i aria-hidden="true" class="fas fa-cube">
                    </i>
                    <div class="number">
                        5
                    </div>
                </div>
            </article>
            <article aria-labelledby="users-title" class="card">
                <div class="title" id="users-title">
                    Users
                </div>
                <div class="content">
                    <i aria-hidden="true" class="fas fa-user">
                    </i>
                    <div class="number">
                        5
                    </div>
                </div>
            </article>
            <article aria-labelledby="orders-title" class="card">
                <div class="title" id="orders-title">
                    Orders
                </div>
                <div class="content">
                    <i aria-hidden="true" class="fas fa-briefcase">
                    </i>
                    <div class="number">
                        5
                    </div>
                </div>
            </article>
        </section>
    </main>
</div>
</body>
</html>
