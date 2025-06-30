<%--
  Created by IntelliJ IDEA.
  User: feedc
  Date: 6/27/2025
  Time: 4:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><!DOCTYPE html>
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

        <c:forEach var="p" items="${latestProducts}">
            <div class="col-lg-3 col-md-4 col-6">
                <div class="card product-card shadow-sm">
                    <span class="badge badge-discount p-2">-20%</span>

                    <img src="https://storage.googleapis.com/workspace-0f70711f-8b4e-4d94-86f1-2a93ccde5887/image/9df370d7-b021-4222-8435-9d275d226c51.png" class="card-img-top product-img" alt="Điện thoại iPhone 13 Pro màu xanh ngọc bích với màn hình sáng">

                    <div class="card-body flex-grow-1">
                        <h5 class="card-title">${p.name}</h5>
                        <p class="card-text text-muted">${p.description}</p>

                        <div class="d-flex justify-content-between align-items-center mt-auto">
                            <div>
                                <span class="price">${p.price}₫</span>
                                <span class="discount ms-2"><del>${p.price * 1.25}₫</del></span>
                            </div>
                        </div>
                    </div>

                    <div class="card-footer bg-transparent">
                        <div class="btn-grid">
                            <button class="btn btn-primary btn-add-to-cart" data-product-id="${p.productId}">Thêm vào giỏ</button>

                            <button class="btn btn-success">Mua ngay</button>
                        </div>
                        <div class="product-detail-link">
                            <a href="product-detail.jsp?id=${p.productId}">Xem chi tiết sản phẩm</a>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>

    </div>
</section>

<!-- Features Section -->
<section class="container mb-5 py-4 bg-light rounded-3">
    <div class="row text-center">
        <div class="col-md-3 col-6 mb-4">
            <div class="p-3">
                <div class="features-icon">
                    <i class="fas fa-shipping-fast"></i>
                </div>
                <h5>Miễn phí vận chuyển</h5>
                <p class="text-muted">Cho đơn hàng từ 500k</p>
            </div>
        </div>
        <div class="col-md-3 col-6 mb-4">
            <div class="p-3">
                <div class="features-icon">
                    <i class="fas fa-exchange-alt"></i>
                </div>
                <h5>Đổi trả dễ dàng</h5>
                <p class="text-muted">Trong vòng 7 ngày</p>
            </div>
        </div>
        <div class="col-md-3 col-6 mb-4">
            <div class="p-3">
                <div class="features-icon">
                    <i class="fas fa-headset"></i>
                </div>
                <h5>Hỗ trợ 24/7</h5>
                <p class="text-muted">Tư vấn nhiệt tình</p>
            </div>
        </div>
        <div class="col-md-3 col-6 mb-4">
            <div class="p-3">
                <div class="features-icon">
                    <i class="fas fa-shield-alt"></i>
                </div>
                <h5>Bảo hành chính hãng</h5>
                <p class="text-muted">Từ 12-24 tháng</p>
            </div>
        </div>
    </div>
</section>

<!-- Testimonials -->
<section class="container mb-5">
    <h2 class="text-center mb-5">Khách hàng nói gì về chúng tôi</h2>
    <div class="row">
        <div class="col-md-4 mb-4">
            <div class="card h-100 border-0 shadow-sm">
                <div class="card-body text-center">
                    <img src="https://storage.googleapis.com/workspace-0f70711f-8b4e-4d94-86f1-2a93ccde5887/image/c2589bc1-6c18-4b35-9b3a-32d1b6b5618e.png" class="testimonial-img mb-3" alt="Người đàn ông trung niên mỉm cười, mặc vest đen">
                    <h5 class="card-title">Anh Văn</h5>
                    <div class="text-warning mb-2">
                        <i class="fas fa-star"></i>
                        <i class="fas fa-star"></i>
                        <i class="fas fa-star"></i>
                        <i class="fas fa-star"></i>
                        <i class="fas fa-star"></i>
                    </div>
                    <p class="card-text">"Sản phẩm chất lượng tốt, giao hàng nhanh. Tôi rất hài lòng!"</p>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-4">
            <div class="card h-100 border-0 shadow-sm">
                <div class="card-body text-center">
                    <img src="https://storage.googleapis.com/workspace-0f70711f-8b4e-4d94-86f1-2a93ccde5887/image/b416ac97-c874-4cf0-b184-816fd07cb3cc.png" class="testimonial-img mb-3" alt="Cô gái trẻ cười tươi, tóc dài màu đen">
                    <h5 class="card-title">Chị Hương</h5>
                    <div class="text-warning mb-2">
                        <i class="fas fa-star"></i>
                        <i class="fas fa-star"></i>
                        <i class="fas fa-star"></i>
                        <i class="fas fa-star"></i>
                        <i class="fas fa-star-half-alt"></i>
                    </div>
                    <p class="card-text">"Nhân viên tư vấn rất nhiệt tình, sẽ tiếp tục ủng hộ shop."</p>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-4">
            <div class="card h-100 border-0 shadow-sm">
                <div class="card-body text-center">
                    <img src="https://storage.googleapis.com/workspace-0f70711f-8b4e-4d94-86f1-2a93ccde5887/image/584433fc-8cb8-4430-9eba-bdf6f15fe6a8.png" class="testimonial-img mb-3" alt="Người đàn ông trẻ đeo kính mỉm cười, áo trắng">
                    <h5 class="card-title">Anh Tuấn</h5>
                    <div class="text-warning mb-2">
                        <i class="fas fa-star"></i>
                        <i class="fas fa-star"></i>
                        <i class="fas fa-star"></i>
                        <i class="fas fa-star"></i>
                        <i class="far fa-star"></i>
                    </div>
                    <p class="card-text">"Giá cả hợp lý, chính sách đổi trả rõ ràng và minh bạch."</p>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Newsletter -->
<section class="container mb-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="bg-primary text-white p-5 rounded-3 text-center">
                <h3 class="mb-3">Đăng ký nhận tin khuyến mãi</h3>
                <p class="mb-4">Nhận thông báo về các chương trình giảm giá và ưu đãi đặc biệt</p>
                <div class="input-group mb-3 mx-auto" style="max-width: 500px;">
                    <input type="email" class="form-control" placeholder="Nhập email của bạn">
                    <button class="btn btn-light" type="button">Đăng ký</button>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Footer -->
<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        document.querySelectorAll('.btn-add-to-cart').forEach(function (btn) {
            btn.addEventListener('click', function () {
                const productId = this.getAttribute('data-product-id');

                fetch('${pageContext.request.contextPath}/cartAdd', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: new URLSearchParams({
                        productId: productId,
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
