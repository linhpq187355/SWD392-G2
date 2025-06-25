<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
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
        .note-area { height: 100px; }
        .total-area { font-weight: bold; font-size: 1.2rem; }
        .delete-icon {
            border: none;
            background: transparent;
            color: red;
            font-size: 1.2rem;
            float: right;
            cursor: pointer;
        }
        .price-old { text-decoration: line-through; color: #888; font-size: 0.9rem; }
        .price-new { color: #d63384; font-weight: bold; }
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
                <li class="nav-item"><a class="nav-link" href="#">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Cart</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Contact</a></li>
            </ul>
        </div>
    </div>
</nav>

<!-- Main -->
<div class="container my-5">
    <h2 class="mb-4">Order Confirm</h2>
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
        <c:forEach var="item" items="${sessionScope.items}" varStatus="i">
            <tr>
                <td>
                    <div class="product-img">
                        <img src="${sessionScope.products[i.index].img != null ? sessionScope.products[i.index].img : 'default.jpg'}" class="img-fluid rounded-3" alt="Image" width="80" height="80">
                    </div>
                </td>
                <td>
                    <strong>${sessionScope.products[i.index].name}</strong><br>
                        ${sessionScope.products[i.index].description}
                </td>
                <td>
                    <span class="price-old">${sessionScope.products[i.index].price} đ</span><br>
                    <span class="price-new">${sessionScope.products[i.index].price * 0.9} đ</span>
                </td>
                <td>${item.quantity}</td>
                <td><strong>${item.quantity * sessionScope.products[i.index].price * 0.9} đ</strong></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="row">
        <form action="orderConfirm" method="post" class="d-flex">
            <!-- Left Column: Delivery Form -->
            <div class="col-md-8 pe-4">
                <h4>Delivery Detail</h4>
                <div class="row mb-3">
                    <div class="col">
                        <label class="form-label">Full Name *</label>
                        <input type="text" name="receiveName" class="form-control" required>
                    </div>
                    <div class="col">
                        <label class="form-label">Email *</label>
                        <input type="email" name="email" class="form-control" required>
                    </div>
                    <div class="col">
                        <label class="form-label">Phone Number *</label>
                        <input type="text" name="receivePhone" class="form-control" required>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col">
                        <label class="form-label">Province</label>
                        <select id="provinceSelect" name="provinceId" class="form-select" required>
                            <option value="">Select province</option>
                        </select>
                    </div>
                    <div class="col">
                        <label class="form-label">District</label>
                        <select id="districtSelect" name="districtId" class="form-select" required>
                            <option value="">Select district</option>
                        </select>
                    </div>
                    <div class="col">
                        <label class="form-label">Ward</label>
                        <select id="wardSelect" name="wardCode" class="form-select" required>
                            <option value="">Select ward</option>
                        </select>
                    </div>
                </div>


                <div class="mb-3">
                    <label class="form-label">Detail address</label>
                    <input type="text" name="receiveAddress" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Note</label>
                    <textarea name="note" class="form-control note-area"></textarea>
                </div>
            </div>

            <!-- Right Column sẽ xử lý ở phần khác -->
        </form>
    </div>
    <script>
        const GHN_TOKEN = "a18e846b-51b8-11f0-928a-1a690f81b498";

        document.addEventListener("DOMContentLoaded", function () {
            loadProvinces();

            document.getElementById("provinceSelect").addEventListener("change", function () {
                const provinceId = this.value;
                loadDistricts(provinceId);
            });

            document.getElementById("districtSelect").addEventListener("change", function () {
                const districtId = this.value;
                loadWards(districtId);
            });
        });

        function loadProvinces() {
            fetch("https://online-gateway.ghn.vn/shiip/public-api/master-data/province", {
                method: "GET",
                headers: {
                    "Token": GHN_TOKEN
                }
            })
                .then(res => res.json())
                .then(data => {
                    const select = document.getElementById("provinceSelect");
                    select.innerHTML = '<option value="">Select province</option>';
                    data.data.forEach(p => {
                        const opt = document.createElement("option");
                        opt.value = p.ProvinceID;
                        opt.text = p.ProvinceName;
                        select.appendChild(opt);
                    });
                })
                .catch(err => console.error("Load province error:", err));
        }

        function loadDistricts(provinceId) {
            fetch("https://online-gateway.ghn.vn/shiip/public-api/master-data/district", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Token": GHN_TOKEN
                },
                body: JSON.stringify({ province_id: parseInt(provinceId) })
            })
                .then(res => res.json())
                .then(data => {
                    const select = document.getElementById("districtSelect");
                    select.innerHTML = '<option value="">Select district</option>';
                    data.data.forEach(d => {
                        const opt = document.createElement("option");
                        opt.value = d.DistrictID;
                        opt.text = d.DistrictName;
                        select.appendChild(opt);
                    });
                })
                .catch(err => console.error("Load district error:", err));
        }

        function loadWards(districtId) {
            fetch("https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + districtId, {
                method: "GET",
                headers: {
                    "Token": GHN_TOKEN
                }
            })
                .then(res => res.json())
                .then(data => {
                    const select = document.getElementById("wardSelect");
                    select.innerHTML = '<option value="">Select ward</option>';
                    data.data.forEach(w => {
                        const opt = document.createElement("option");
                        opt.value = w.WardCode;
                        opt.text = w.WardName;
                        select.appendChild(opt);
                    });
                })
                .catch(err => console.error("Load ward error:", err));
        }
    </script>


<%--    <form action="orderCreate" method="post">--%>
<%--        <div class="row">--%>
<%--            <div class="col-md-8">--%>
<%--                <h4>Delivery Information</h4>--%>
<%--                <div class="mb-3">--%>
<%--                    <label class="form-label">Full Name</label>--%>
<%--                    <input type="text" name="receiveName" class="form-control" value="${sessionScope.receiveName}" readonly>--%>
<%--                </div>--%>
<%--                <div class="mb-3">--%>
<%--                    <label class="form-label">Phone</label>--%>
<%--                    <input type="text" name="receivePhone" class="form-control" value="${sessionScope.receivePhone}" readonly>--%>
<%--                </div>--%>
<%--                <div class="mb-3">--%>
<%--                    <label class="form-label">Detail Address</label>--%>
<%--                    <input type="text" name="receiveAddress" class="form-control" value="${sessionScope.receiveAddress}" readonly>--%>
<%--                </div>--%>
<%--                <div class="mb-3">--%>
<%--                    <label class="form-label">Note</label>--%>
<%--                    <textarea name="note" class="form-control note-area" readonly>${sessionScope.note}</textarea>--%>
<%--                </div>--%>
<%--                <div class="mb-3">--%>
<%--                    <label class="form-label">Shipping Method</label>--%>
<%--                    <input type="text" name="shippingMethod" class="form-control" value="${sessionScope.shippingMethod}" readonly>--%>
<%--                </div>--%>

<%--                <div class="row mb-3">--%>
<%--                    <div class="col">--%>
<%--                        <label class="form-label">City</label>--%>
<%--                        <select class="form-select" disabled><option>Select city</option></select>--%>
<%--                    </div>--%>
<%--                    <div class="col">--%>
<%--                        <label class="form-label">District</label>--%>
<%--                        <select class="form-select" disabled><option>Select district</option></select>--%>
<%--                    </div>--%>
<%--                    <div class="col">--%>
<%--                        <label class="form-label">Province</label>--%>
<%--                        <select class="form-select" disabled><option>Select province</option></select>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>

<%--            <div class="col-md-4">--%>
<%--                <div class="bg-light p-4 rounded">--%>
<%--                    <h4>Summary</h4>--%>
<%--                    <p><strong>Subtotal:</strong> ${sessionScope.subtotal} đ</p>--%>
<%--                    <p><strong>Discount:</strong> ${sessionScope.discount} đ</p>--%>
<%--                    <p class="total-area">Total Cost: ${sessionScope.total} đ</p>--%>

<%--                    <hr class="my-4">--%>
<%--                    <button type="submit" class="btn btn-primary w-100">Confirm Order</button>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </form>--%>
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
