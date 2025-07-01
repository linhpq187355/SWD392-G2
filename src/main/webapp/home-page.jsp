<%--
  Created by IntelliJ IDEA.
  User: feedc
  Date: 6/27/2025
  Time: 4:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cửa hàng Online</title>
  <!-- Bootstrap 5 CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <style>
    /* Custom CSS */
    :root {
      --primary-color: #4e73df;
      --secondary-color: #f8f9fc;
      --accent-color: #ff6b6b;
    }

    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background-color: #f8f9fa;
    }

    .navbar-brand {
      font-weight: 700;
      color: var(--primary-color);
    }

    .hero-section {
      background: linear-gradient(135deg, var(--primary-color), #224abe);
      color: white;
      padding: 5rem 0;
      margin-bottom: 3rem;
    }

    .search-box {
      max-width: 600px;
      margin: 0 auto;
    }

    .product-card {
      transition: all 0.3s ease;
      border-radius: 10px;
      overflow: hidden;
      height: 100%;
      display: flex;
      flex-direction: column;
    }

    .product-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 20px rgba(0,0,0,0.1);
    }

    .product-img {
      height: 200px;
      object-fit: cover;
    }

    .price {
      color: var(--accent-color);
      font-weight: 700;
      font-size: 1.2rem;
    }

    .discount {
      text-decoration: line-through;
      color: #6c757d;
      font-size: 0.9rem;
    }

    .btn-primary {
      background-color: var(--primary-color);
      border-color: var(--primary-color);
    }

    .btn-success {
      background-color: #28a745;
      border-color: #28a745;
    }

    .btn-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 0.5rem;
    }

    .product-detail-link {
      text-align: center;
      margin-top: 0.5rem;
      font-size: 0.9rem;
    }

    .product-detail-link a {
      color: var(--primary-color);
      text-decoration: none;
    }

    .btn-primary:hover {
      background-color: #2e59d9;
      border-color: #2e59d9;
    }

    .badge-discount {
      position: absolute;
      top: 10px;
      right: 10px;
      background-color: var(--accent-color);
    }

    footer {
      background-color: #343a40;
      color: white;
      padding: 3rem 0;
      margin-top: 3rem;
    }

    .features-icon {
      font-size: 2.5rem;
      color: var(--primary-color);
      margin-bottom: 1rem;
    }

    .category-img {
      height: 120px;
      object-fit: cover;
      border-radius: 10px 10px 0 0;
    }

    .testimonial-img {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      object-fit: cover;
    }
  </style>
</head>
<body>
<!-- Navigation -->
<jsp:include page="header.jsp"/>

<!-- Hero Section -->
<section class="hero-section">
  <div class="container text-center">
    <h1 class="display-4 fw-bold mb-4">Mua sắm trực tuyến dễ dàng</h1>
    <p class="lead mb-5">Hàng ngàn sản phẩm chất lượng với giá cả hợp lý</p>

    <div class="search-box">
      <div class="input-group mb-3">
        <input type="text" class="form-control form-control-lg" placeholder="Tìm kiếm sản phẩm...">
        <button class="btn btn-primary" type="button"><i class="fas fa-search"></i></button>
      </div>
    </div>
  </div>
</section>

<!-- Featured Categories -->
<section class="container mb-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Danh mục nổi bật</h2>
    <a href="#" class="btn btn-sm btn-outline-primary">Xem tất cả</a>
  </div>
  <div class="row g-4">
    <div class="col-md-3 col-6">
      <div class="card h-100 border-0 shadow-sm">
        <img src="https://storage.googleapis.com/workspace-0f70711f-8b4e-4d94-86f1-2a93ccde5887/image/75b39145-bf9e-4f35-bd2f-c3be770b9c81.png" class="category-img" alt="Bộ sưu tập điện thoại thông minh mới nhất nhiều màu sắc">
        <div class="card-body text-center">
          <h5 class="card-title">Điện thoại</h5>
        </div>
      </div>
    </div>
    <div class="col-md-3 col-6">
      <div class="card h-100 border-0 shadow-sm">
        <img src="https://storage.googleapis.com/workspace-0f70711f-8b4e-4d94-86f1-2a93ccde5887/image/6f51afcb-eb82-4e32-9f8a-2459ac311d5e.png" class="category-img" alt="Máy tính xách tay hiện đại đặt trên bàn làm việc">
        <div class="card-body text-center">
          <h5 class="card-title">Laptop</h5>
        </div>
      </div>
    </div>
    <div class="col-md-3 col-6">
      <div class="card h-100 border-0 shadow-sm">
        <img src="https://storage.googleapis.com/workspace-0f70711f-8b4e-4d94-86f1-2a93ccde5887/image/0e777fdc-397d-4f44-a235-5db43f842089.png" class="category-img" alt="Đồng hồ thông minh và đồng hồ đeo tay cao cấp">
        <div class="card-body text-center">
          <h5 class="card-title">Đồng hồ</h5>
        </div>
      </div>
    </div>
    <div class="col-md-3 col-6">
      <div class="card h-100 border-0 shadow-sm">
        <img src="https://storage.googleapis.com/workspace-0f70711f-8b4e-4d94-86f1-2a93ccde5887/image/ad9eacaf-f15c-4f9e-ad4d-1b99c6f7b6c3.png" class="category-img" alt="Tai nghe không dây chất lượng cao trên nền trắng">
        <div class="card-body text-center">
          <h5 class="card-title">Tai nghe</h5>
        </div>
      </div>
    </div>
  </div>
</section>

<!-- Featured Products -->
<section class="container mb-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Sản phẩm bán chạy</h2>
    <a href="#" class="btn btn-sm btn-outline-primary">Xem tất cả</a>
  </div>
  <div class="row g-4">
    <c:forEach var="p" items="${mostSellProduct}">
      <div class="col-lg-3 col-md-4 col-6">
        <div class="card product-card shadow-sm">
          <c:choose>
            <c:when test="${not empty p.images}">
              <img src="${p.images[0]}"
                   class="card-img-top product-img"
                   alt="${p.name}">
            </c:when>
            <c:otherwise>
              <img src="https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/b16739dd-0f2a-4180-90bf-fb9324ea74b8/ZM+VAPOR+16+ELITE+AM+95+SE+FG.png"
                   class="card-img-top product-img"
                   alt="${p.name}" />
            </c:otherwise>
          </c:choose>

          <div class="card-body flex-grow-1">
            <h5 class="card-title">${p.name}</h5>
            <p class="card-text text-muted">${p.description}</p>

            <div class="d-flex justify-content-between align-items-center mt-auto">
              <div>
            <span class="price"${p.salePrice}>
              <fmt:formatNumber value="${p.salePrice}" type="currency" currencySymbol="₫" />
            </span>

                <c:if test="${p.originalPrice > p.salePrice}">
              <span class="discount ms-2">
                <del>
                  <fmt:formatNumber value="${p.originalPrice}" type="currency" currencySymbol="₫" />
                </del>
              </span>
                </c:if>
              </div>
            </div>
          </div>

          <div class="card-footer bg-transparent">
            <div class="d-flex gap-2">
              <button class="btn btn-primary w-100 btn-add-to-cart" data-id="${p.id}">
                Thêm vào giỏ
              </button>

              <form method="post" action="${pageContext.request.contextPath}/cartBuyNow" class="w-100">
                <input type="hidden" name="productId" value="${p.id}">
                <input type="hidden" name="quantity" value="1">
                <button class="btn btn-success w-100" type="submit">Mua ngay</button>
              </form>
            </div>

            <div class="product-detail-link">
              <a href="product-detail.jsp?code=${p.id}">Xem chi tiết sản phẩm</a>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>

  </div>
</section>
<section class="container mb-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Sản phẩm mới nhất</h2>
    <a href="#" class="btn btn-sm btn-outline-primary">Xem tất cả</a>
  </div>
  <div class="row g-4">
    <c:forEach var="p" items="${latestProducts}">
      <div class="col-lg-3 col-md-4 col-6">
        <div class="card product-card shadow-sm">
          <c:choose>
            <c:when test="${not empty p.images}">
              <img src="${p.images[0]}"
                   class="card-img-top product-img"
                   alt="${p.name}">
            </c:when>
            <c:otherwise>
              <img src="https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/b16739dd-0f2a-4180-90bf-fb9324ea74b8/ZM+VAPOR+16+ELITE+AM+95+SE+FG.png"
                   class="card-img-top product-img"
                   alt="${p.name}" />
            </c:otherwise>
          </c:choose>

          <div class="card-body flex-grow-1">
            <h5 class="card-title">${p.name}</h5>
            <p class="card-text text-muted">${p.description}</p>

            <div class="d-flex justify-content-between align-items-center mt-auto">
              <div>
            <span class="price"${p.salePrice}>
              <fmt:formatNumber value="${p.salePrice}" type="currency" currencySymbol="₫" />
            </span>

                <c:if test="${p.originalPrice > p.salePrice}">
              <span class="discount ms-2">
                <del>
                  <fmt:formatNumber value="${p.originalPrice}" type="currency" currencySymbol="₫" />
                </del>
              </span>
                </c:if>
              </div>
            </div>
          </div>

          <div class="card-footer bg-transparent">
            <div class="d-flex gap-2">
              <button class="btn btn-primary w-100 btn-add-to-cart" data-id="${p.id}">
                Thêm vào giỏ
              </button>

              <form method="post" action="${pageContext.request.contextPath}/cartBuyNow" class="w-100">
                <input type="hidden" name="productId" value="${p.id}">
                <input type="hidden" name="quantity" value="1">
                <button class="btn btn-success w-100" type="submit">Mua ngay</button>
              </form>
            </div>

            <div class="product-detail-link">
              <a href="product-detail.jsp?code=${p.id}">Xem chi tiết sản phẩm</a>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>

  </div>
</section>


<!-- Footer -->
<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
  document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.btn-add-to-cart').forEach(function (btn) {
      btn.addEventListener('click', function () {
        const id = this.getAttribute('data-id');

        fetch('${pageContext.request.contextPath}/cartAdd', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          body: new URLSearchParams({
            productId: id,
            quantity: 1
          })
        })
                .then(response => {
                  if (!response.ok) throw new Error("Failed to add product");
                  return response.text();
                })
                .then(() => {
                  Swal.fire({
                    icon: 'success',
                    title: 'Đã thêm vào giỏ hàng',
                    text: 'Sản phẩm của bạn đã được thêm thành công!',
                    timer: 2000,
                    showConfirmButton: false
                  });
                })
                .catch(err => {
                  Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: 'Không thể thêm vào giỏ hàng.'
                  });
                });
      });
    });
  });
</script>
<!-- Bootstrap 5 JS Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

