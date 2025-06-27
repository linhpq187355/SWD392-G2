<%--
  Created by IntelliJ IDEA.
  User: feedc
  Date: 6/27/2025
  Time: 5:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<!-- Navigation -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/HomePage">Shop<span>Online</span></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/HomePage">Trang chủ</a>
                </li>
                <li class="nav-item"><a class="nav-link" href="#">Sản phẩm</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Danh mục</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Giới thiệu</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Liên hệ</a></li>
            </ul>
            <div class="d-flex ms-3">
                <a href="#" class="btn btn-outline-secondary me-2"><i class="fas fa-user"></i></a>
                <a href="#" class="btn btn-outline-secondary me-2"><i class="fas fa-search"></i></a>
                <a href="${pageContext.request.contextPath}/cartView" class="btn btn-outline-secondary position-relative">
                    <i class="fas fa-shopping-cart"></i>

                    <c:set var="totalQuantity" value="0"/>
                    <c:forEach var="item" items="${sessionScope.cartItems}">
                        <c:set var="totalQuantity" value="${totalQuantity + item.quantity}" />
                    </c:forEach>

                    <c:if test="${totalQuantity > 0}">
            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                    ${totalQuantity}
            </span>
                    </c:if>
                </a>
            </div>
        </div>
    </div>
</nav>
</html>
