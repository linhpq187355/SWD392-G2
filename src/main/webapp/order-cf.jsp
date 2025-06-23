<%--
  Created by IntelliJ IDEA.
  User: feedc
  Date: 6/23/2025
  Time: 9:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .product-img {
            width: 80px;
            height: 80px;
            background-color: #ddd;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .note-area {
            height: 100px;
        }
        .total-area {
            font-weight: bold;
            font-size: 1.2rem;
        }
        .delete-icon {
            border: none;
            background: transparent;
            color: red;
            font-size: 1.2rem;
            float: right;
            cursor: pointer;
        }
        .price-old {
            text-decoration: line-through;
            color: #888;
            font-size: 0.9rem;
        }
        .price-new {
            color: #d63384;
            font-weight: bold;
        }
        footer {
            background-color: #f8f9fa;
            padding: 20px 0;
            text-align: center;
            margin-top: 40px;
        }
    </style>
</head>
<body>

<!-- Header -->
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
        <a class="navbar-brand" href="#">MyShop</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="#">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Cart</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Contact</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container my-5">
    <h2 class="mb-4">Shopping Cart</h2>
    <table class="table">
        <thead>
        <tr>
            <th>Picture</th>
            <th style="width: 35%;">Product</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Amount</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><div class="product-img">IMG</div></td>
            <td>
                <strong>Product name</strong><br>
                Description
                <button class="delete-icon" title="Remove">&times;</button>
            </td>
            <td>
                <span class="price-old">4.000.000 đ</span><br>
                <span class="price-new">3.300.300 đ</span>
            </td>
            <td><input type="number" value="1" class="form-control form-control-sm" style="width: 60px;" min="1"></td>
            <td><strong>3.300.300 đ</strong></td>
        </tr>
        <tr>
            <td><div class="product-img">IMG</div></td>
            <td>
                <strong>Product name</strong><br>
                Description
                <button class="delete-icon" title="Remove">&times;</button>
            </td>
            <td>
                <span class="price-old">4.000.000 đ</span><br>
                <span class="price-new">3.300.300 đ</span>
            </td>
            <td><input type="number" value="1" class="form-control" min="1"></td>
            <td><strong>3.300.300 đ</strong></td>
        </tr>
        </tbody>
    </table>

    <div class="row">
        <div class="col-md-8">
            <h4>Delivery Detail</h4>
            <form>
                <div class="row mb-3">
                    <div class="col">
                        <label class="form-label">Full Name *</label>
                        <input type="text" class="form-control">
                    </div>
                    <div class="col">
                        <label class="form-label">Email *</label>
                        <input type="email" class="form-control">
                    </div>
                    <div class="col">
                        <label class="form-label">Phone Number *</label>
                        <input type="text" class="form-control">
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col">
                        <label class="form-label">City</label>
                        <select class="form-select"><option>Select city</option></select>
                    </div>
                    <div class="col">
                        <label class="form-label">District</label>
                        <select class="form-select"><option>Select district</option></select>
                    </div>
                    <div class="col">
                        <label class="form-label">Province</label>
                        <select class="form-select"><option>Select province</option></select>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label">Detail address</label>
                    <input type="text" class="form-control">
                </div>
                <div class="mb-3">
                    <label class="form-label">Note</label>
                    <textarea class="form-control note-area"></textarea>
                </div>

                <h5>Payment Method</h5>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="payment" id="visa">
                    <label class="form-check-label" for="visa">Visa</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="payment" id="card">
                    <label class="form-check-label" for="card">Card</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="payment" id="cod" checked>
                    <label class="form-check-label" for="cod">COD</label>
                </div>

                <button type="submit" class="btn btn-primary mt-3">Confirm</button>
            </form>
        </div>

        <div class="col-md-4">
            <h4>Summary</h4>
            <p>Subtotal: 6.600.000 đ</p>
            <p>Shipping Fee: 600.000 đ</p>
            <p>Discount: 600.000 đ</p>
            <p class="total-area">Total Cost: 12.000.000 đ</p>
        </div>
    </div>
</div>

<!-- Footer -->
<footer>
    <div class="container">
        <p class="mb-0">&copy; 2025 MyShop. All rights reserved.</p>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

