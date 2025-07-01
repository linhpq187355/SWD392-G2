<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Checkout Page</title>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

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
<jsp:include page="header.jsp"/>


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
        <c:choose>
            <c:when test="${not empty items}">
                <c:forEach var="item" items="${items}" varStatus="i">
                    <tr>
                        <td>
                            <c:set var="product" value="${products[i.index]}" />
                            <img src="${product.images[0]}" alt="${product.name}"
                                 class="img-fluid rounded-3" alt="Image" width="80" height="80">
                        </td>
                        <td>
                            <strong>${products[i.index].name}</strong><br>
                                ${products[i.index].description}
                        </td>
                        <td>
                            <span class="price-old">${products[i.index].originalPrice * item.quantity} đ</span><br>
                            <span class="price-new">${products[i.index].salePrice * item.quantity} đ</span>
                        </td>
                        <td>
                            <form action="cartUpdate" method="post" class="d-flex">
                                <input type="hidden" name="cartItemId" value="${item.cartItemId}" />
                                <input name="quantity" type="number" min="1" value="${item.quantity}"
                                       class="form-control form-control-sm mx-1 text-center" style="width: 60px;">
                            </form>
                        </td>
                        <td>
                        <strong>${item.quantity * products[i.index].salePrice} đ</strong>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>

            <c:otherwise>
                <c:forEach var="product" items="${guestProducts}">
                    <tr>
                        <td>
                            <img src="${product.images[0]}" alt="${product.name}"
                                 class="img-fluid rounded-3" alt="Image" width="80" height="80">
                        </td>
                        <td>
                            <strong>${product.name}</strong><br>
                                ${product.description}
                        </td>
                        <td>
                            <span class="price-old">${product.originalPrice } đ</span><br>
                            <span class="price-new">${product.salePrice } đ</span>
                        </td>
                        <td>
                            <input name="quantity" type="number" min="1" readonly
                                   value="${guestQuantities[product.id]}"
                                   class="form-control form-control-sm mx-1 text-center" style="width: 60px;">
                        </td>
                        <td>
                            <strong>${guestQuantities[product.id] * product.salePrice} đ</strong>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>

        </tbody>
    </table>

    <div class="row">
        <form id="orderForm" action="orderConfirm" method="post" class="d-flex">
            <!-- Left Column: Delivery Form -->
            <div class="col-md-8 pe-4">
                <h4>Delivery Detail</h4>
                <div class="row mb-3">
                    <div class="col">
                        <label class="form-label">Full Name *</label>
                        <input type="text" name="receiveName" class="form-control" required
                               value="${user.fullName}">
                    </div>
                    <div class="col">
                        <label class="form-label">Email *</label>
                        <input type="email" name="email" class="form-control" required
                               value="${user.email}">
                    </div>
                    <div class="col">
                        <label class="form-label">Phone Number *</label>
                        <input type="text" name="receivePhone" class="form-control" required
                               value="${user.phone}">
                    </div>
                </div>


                <div class="row mb-3">
<%--                    <input type="hidden" id="loggedInProvinceId" value="${user.provinceId}" />--%>
<%--                    <input type="hidden" id="loggedInDistrictId" value="${user.districtId}" />--%>
<%--                    <input type="hidden" id="loggedInWardCode" value="${user.wardId}" />--%>

                    <input type="hidden" id="loggedInProvinceId" value="${user.provinceId}" />
                    <input type="hidden" id="loggedInDistrictId" value="${user.districtId}" />
                    <input type="hidden" id="loggedInWardCode" value="${user.wardId}" />

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
                    <input type="text" name="receiveAddress" class="form-control" required
                           value="${user.addressDetail}">
                </div>
                <div class="mb-3">
                    <label class="form-label">Note</label>
                    <textarea name="note" class="form-control note-area"></textarea>
                </div>
            </div>
            <div class="col-md-4">
                <div class="bg-light p-4 rounded" id="summaryData"
                     data-subtotal="${subtotal}" data-discount="${discount}">
                    <h4>Summary</h4>
                    <p><strong>Subtotal:</strong> <span id="subtotalDisplay">${subtotal} đ</span></p>
                    <p><strong>Discount:</strong> <span id="discountDisplay">${discount} đ</span></p>
                    <p><strong>Shipping Fee:</strong> <span id="shippingFeeDisplay">...</span></p>
                    <p class="total-area">Total Cost: <span id="totalCostDisplay">...</span></p>


                <hr class="my-4">

                    <h5>Payment Method</h5>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="shippingMethod" value="Visa" id="visa">
                        <label class="form-check-label" for="visa">VnPay</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="shippingMethod" value="Card" id="card">
                        <label class="form-check-label" for="card">ZaloPay</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="shippingMethod" value="COD" id="cod" checked>
                        <label class="form-check-label" for="cod">COD</label>
                    </div>

                    <button type="button" id="confirmOrderBtn" class="btn btn-primary w-100 mt-3">Confirm</button>
                </div>
            </div>
        </form>
</div>
</div>

<script>
    const contextPath = "${pageContext.request.contextPath}";
    const GHN_TOKEN = "a18e846b-51b8-11f0-928a-1a690f81b498";

    const loggedInProvinceId = document.getElementById("loggedInProvinceId")?.value || null;
    const loggedInDistrictId = document.getElementById("loggedInDistrictId")?.value || null;
    const loggedInWardCode = document.getElementById("loggedInWardCode")?.value || null;

    function loadProvinces() {
        fetch("https://online-gateway.ghn.vn/shiip/public-api/master-data/province", {
            method: "GET",
            headers: { "Token": GHN_TOKEN }
        })
            .then(res => res.json())
            .then(data => {
                const select = document.getElementById("provinceSelect");
                select.innerHTML = '<option value="">Select province</option>';
                data.data.forEach(p => {
                    const opt = document.createElement("option");
                    opt.value = p.ProvinceID;
                    opt.text = p.ProvinceName;
                    console.log(p.ProvinceID)
                    if (p.ProvinceID == loggedInProvinceId) opt.selected = true;
                    select.appendChild(opt);
                });

                if (loggedInProvinceId) loadDistricts(loggedInProvinceId, true);
            });
    }

    function loadDistricts(provinceId, autoLoad = false) {
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
                    if (d.DistrictID == loggedInDistrictId) opt.selected = true;
                    select.appendChild(opt);
                });

                if (autoLoad && loggedInDistrictId) loadWards(loggedInDistrictId, true);
            });
    }

    function loadWards(districtId, autoLoad = false) {
        fetch("https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + districtId, {
            method: "GET",
            headers: { "Token": GHN_TOKEN }
        })
            .then(res => res.json())
            .then(data => {
                const select = document.getElementById("wardSelect");
                select.innerHTML = '<option value="">Select ward</option>';
                data.data.forEach(w => {
                    const opt = document.createElement("option");
                    opt.value = w.WardCode;
                    opt.text = w.WardName;
                    if (w.WardCode === loggedInWardCode) opt.selected = true;
                    select.appendChild(opt);
                });

                if (autoLoad && loggedInWardCode) updateShippingFee();
            });
    }

    function formatCurrency(value) {
        return new Intl.NumberFormat("vi-VN").format(value) + " đ";
    }

    function updateShippingFee() {
        const summaryDiv = document.getElementById("summaryData");
        if (!summaryDiv) return;

        const subtotal = parseFloat(summaryDiv.dataset.subtotal || 0);
        const discount = parseFloat(summaryDiv.dataset.discount || 0);
        const districtId = document.getElementById("districtSelect")?.value;
        const wardCode = document.getElementById("wardSelect")?.value;

        if (!districtId || !wardCode) return;

        fetch(contextPath + "/calculateShippingFee", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ districtId: parseInt(districtId), wardCode })
        })
            .then(res => res.json())
            .then(data => {
                const fee = data.fee || 0;
                const total = subtotal - discount + fee;

                document.getElementById("shippingFeeDisplay").innerText = formatCurrency(fee);
                document.getElementById("totalCostDisplay").innerText = formatCurrency(total);
                console.log("✅ Tính toán lại:", { subtotal, discount, fee, total });
            })
            .catch(err => {
                console.error("Shipping fee fetch error:", err);
                document.getElementById("shippingFeeDisplay").innerText = "Lỗi";
            });
    }

    document.addEventListener("DOMContentLoaded", function () {
        console.log("📦 loggedInProvinceId:", loggedInProvinceId);
        console.log("🏙️ loggedInDistrictId:", loggedInDistrictId);
        console.log("📍 loggedInWardCode:", loggedInWardCode);

        loadProvinces();
        document.getElementById("provinceSelect").addEventListener("change", e => loadDistricts(e.target.value));
        document.getElementById("districtSelect").addEventListener("change", e => loadWards(e.target.value));
        document.getElementById("wardSelect").addEventListener("change", updateShippingFee);
    });
</script>

<script><%--<script>--%>
<%--    const contextPath = "${pageContext.request.contextPath}";--%>
<%--    const GHN_TOKEN = "a18e846b-51b8-11f0-928a-1a690f81b498";--%>
<%--    const loggedInWardCode = document.getElementById("loggedInWardCode")?.value || null;--%>

<%--        function loadProvinces() {--%>
<%--            fetch("https://online-gateway.ghn.vn/shiip/public-api/master-data/province", {--%>
<%--                method: "GET",--%>
<%--                headers: { "Token": GHN_TOKEN }--%>
<%--            })--%>
<%--                .then(res => res.json())--%>
<%--                .then(data => {--%>
<%--                    const select = document.getElementById("provinceSelect");--%>
<%--                    select.innerHTML = '<option value="">Select province</option>';--%>
<%--                    data.data.forEach(p => {--%>
<%--                        const opt = document.createElement("option");--%>
<%--                        opt.value = p.ProvinceID;--%>
<%--                        opt.text = p.ProvinceName;--%>
<%--                        console.log("✅ First provinceId:", data.data[0].ProvinceID);--%>

<%--                        select.appendChild(opt);--%>
<%--                    });--%>
<%--                });--%>

<%--        }--%>

<%--        function loadDistricts(provinceId) {--%>
<%--            fetch("https://online-gateway.ghn.vn/shiip/public-api/master-data/district", {--%>
<%--                method: "POST",--%>
<%--                headers: {--%>
<%--                    "Content-Type": "application/json",--%>
<%--                    "Token": GHN_TOKEN--%>
<%--                },--%>
<%--                body: JSON.stringify({ province_id: parseInt(provinceId) })--%>
<%--            })--%>
<%--                .then(res => res.json())--%>
<%--                .then(data => {--%>
<%--                    const select = document.getElementById("districtSelect");--%>
<%--                    select.innerHTML = '<option value="">Select district</option>';--%>
<%--                    data.data.forEach(d => {--%>
<%--                        const opt = document.createElement("option");--%>
<%--                        opt.value = d.DistrictID;--%>
<%--                        opt.text = d.DistrictName;--%>
<%--                        select.appendChild(opt);--%>
<%--                    });--%>
<%--                });--%>
<%--        }--%>

<%--        function loadWards(districtId) {--%>
<%--            fetch("https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + districtId, {--%>
<%--                method: "GET",--%>
<%--                headers: { "Token": GHN_TOKEN }--%>
<%--            })--%>
<%--                .then(res => res.json())--%>
<%--                .then(data => {--%>
<%--                    const select = document.getElementById("wardSelect");--%>
<%--                    select.innerHTML = '<option value="">Select ward</option>';--%>
<%--                    data.data.forEach(w => {--%>
<%--                        const opt = document.createElement("option");--%>
<%--                        opt.value = w.WardCode;--%>
<%--                        opt.text = w.WardName;--%>
<%--                        select.appendChild(opt);--%>
<%--                    });--%>
<%--                });--%>
<%--        }--%>

<%--        function formatCurrency(value) {--%>
<%--            return new Intl.NumberFormat("vi-VN").format(value) + " đ";--%>
<%--        }--%>


<%--    function updateShippingFee() {--%>
<%--        const summaryDiv = document.getElementById("summaryData");--%>
<%--        if (!summaryDiv) return;--%>

<%--        const subtotal = parseFloat(summaryDiv.dataset.subtotal || 0);--%>
<%--        const discount = parseFloat(summaryDiv.dataset.discount || 0);--%>
<%--        const districtId = document.getElementById("districtSelect")?.value;--%>
<%--        const wardCode = document.getElementById("wardSelect")?.value;--%>

<%--        if (!districtId || !wardCode) return;--%>

<%--        fetch("${pageContext.request.contextPath}/calculateShippingFee", {--%>
<%--            method: "POST",--%>
<%--            headers: {--%>
<%--                "Content-Type": "application/json"--%>
<%--            },--%>
<%--            body: JSON.stringify({ districtId: parseInt(districtId), wardCode })--%>
<%--        })--%>
<%--            .then(res => res.json())--%>
<%--            .then(data => {--%>
<%--                const fee = data.fee || 0;--%>
<%--                const total = subtotal - discount + fee;--%>

<%--                document.getElementById("shippingFeeDisplay").innerText = formatCurrency(fee);--%>
<%--                document.getElementById("totalCostDisplay").innerText = formatCurrency(total);--%>
<%--                console.log("✅ Tính toán lại:", { subtotal, discount, fee, total });--%>

<%--            })--%>
<%--            .catch(err => {--%>
<%--                console.error("Shipping fee fetch error:", err);--%>
<%--                document.getElementById("shippingFeeDisplay").innerText = "Lỗi";--%>
<%--            });--%>
<%--    }--%>

<%--    document.addEventListener("DOMContentLoaded", function () {--%>
<%--        loadProvinces();--%>
<%--        document.getElementById("provinceSelect").addEventListener("change", e => loadDistricts(e.target.value));--%>
<%--        document.getElementById("districtSelect").addEventListener("change", e => loadWards(e.target.value));--%>
<%--        document.getElementById("wardSelect").addEventListener("change", updateShippingFee);--%>
<%--    });--%>


<%--</script>--%>

document.addEventListener("DOMContentLoaded", function () {
        const confirmBtn = document.getElementById("confirmOrderBtn");
        const orderForm = document.getElementById("orderForm");

        if (confirmBtn && orderForm) {
            confirmBtn.addEventListener("click", function () {
                console.log("🔔 Confirm button clicked");

                Swal.fire({
                    title: 'Xác nhận đặt hàng?',
                    text: "Bạn chắc chắn muốn tạo đơn hàng với thông tin đã nhập?",
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonText: 'Có, đặt hàng',
                    cancelButtonText: 'Hủy'

                }).then((result) => {
                    if (result.isConfirmed) {
                        orderForm.submit(); // Gửi form nếu người dùng đồng ý
                    }
                });
            });
        }
    });
</script>

<script>
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get("success") === "true") {
        Swal.fire({
            title: '🎉 Đặt hàng thành công!',
            text: 'Đơn hàng của bạn đã được ghi nhận.',
            icon: 'success',
            confirmButtonText: 'Về trang chủ'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = "${pageContext.request.contextPath}/HomePage";
            }
        });
    } else if (urlParams.get("error") === "true") {
        Swal.fire({
            title: '❌ Lỗi đặt hàng!',
            text: 'Không thể tạo đơn hàng. Vui lòng thử lại.',
            icon: 'error',
            confirmButtonText: 'Thử lại'
        });
    }
</script>


<!-- Footer -->
<jsp:include page="footer.jsp"/>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
