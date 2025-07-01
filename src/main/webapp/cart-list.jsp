

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Shopping Cart</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <style>
        .old-price {
            text-decoration: line-through;
            color: #888;
            font-size: 0.9rem;
        }

        .discount-badge {
            font-size: 0.8rem;
            color: red;
            font-weight: bold;
        }

        .remove-btn {
            cursor: pointer;
            color: #dc3545;
            font-weight: bold;
            font-size: 1.2rem;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>
    <section class="h-100 h-custom bg-light">
    <div class="container py-5 h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-12">
                <div class="card" style="border-radius: 15px;">
                    <div class="card-body p-0">
                        <div class="row g-0">
                            <!-- Cart Items -->
                            <div class="col-lg-8">
                                <div class="p-5">
                                    <div class="d-flex justify-content-between align-items-center mb-5">
                                        <h1 class="fw-bold mb-0">Giỏ hàng</h1>
                                    </div>
                                    <hr class="my-4">

                                    <c:choose>

                                        <c:when test="${not empty items}">
                                            <c:forEach var="item" items="${items}" varStatus="status">
                                                <c:set var="product" value="${products[status.index]}" />
                                                <c:set var="discountRate" value="${(product.originalPrice - product.salePrice) / product.originalPrice * 100}" />

                                                <div class="row mb-4 align-items-center cart-item border-bottom pb-3">
                                                    <div class="col-md-2">
                                                        <img src="${product.images[0]}" alt="${product.name}" class="img-fluid" />
                                                    </div>
                                                    <div class="col-md-3">
                                                        <h6 class="fw-bold mb-1">${product.name}</h6>
                        <span class="discount-badge">
                            -<fmt:formatNumber value="${discountRate}" maxFractionDigits="0"/>%
                        </span>
                                                    </div>
                                                    <div class="col-md-3 d-flex">
                                                        <form action="cartUpdate" method="post" class="d-flex">
                                                            <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                                                            <button type="submit" class="btn btn-outline-secondary btn-sm"
                                                                    onclick="this.parentNode.querySelector('input[type=number]').stepDown()">−</button>
                                                            <input name="quantity" type="number" min="1" value="${item.quantity}"
                                                                   class="form-control form-control-sm mx-1 text-center" style="width: 60px;">
                                                            <button type="submit" class="btn btn-outline-secondary btn-sm"
                                                                    onclick="this.parentNode.querySelector('input[type=number]').stepUp()">+</button>
                                                        </form>
                                                    </div>
                                                    <div class="col-md-2 text-end">
                                                        <div class="text-muted small">
                                                            <del><fmt:formatNumber value="${product.originalPrice + item.quantity} " type="currency"/></del>
                                                        </div>
                                                        <div class="fw-bold"><fmt:formatNumber value="${product.salePrice * item.quantity}" type="currency"/></div>
                                                    </div>
                                                    <div class="col-md-2 text-end">
                                                        <form action="cartRemove" method="post">
                                                            <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                                                            <button class="remove-btn" type="submit">×</button>
                                                        </form>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                            <div class="mt-4 text-end">
                                                <form action="cartClear" method="get">
                                                    <button class="btn btn-danger">Clear Cart</button>
                                                </form>
                                            </div>
                                        </c:when>

                                        <c:when test="${not empty guestProducts}">
                                            <c:forEach var="product" items="${guestProducts}">
                                                <c:set var="quantity" value="${guestQuantities[product.id]}" />
                                                <c:set var="discountRate" value="${(product.originalPrice - product.salePrice) / product.originalPrice * 100}" />

                                                <div class="row mb-4 align-items-center cart-item border-bottom pb-3">
                                                    <div class="col-md-2">
                                                        <img src="${product.images[0]}" alt="${product.name}" class="img-fluid" />
                                                    </div>
                                                    <div class="col-md-3">
                                                        <h6 class="fw-bold mb-1">${product.name}</h6>
                                                        <c:if test="${product.originalPrice > product.salePrice}">
                        <span class="discount-badge">
                            -<fmt:formatNumber value="${discountRate}" maxFractionDigits="0"/>%
                        </span>
                                                        </c:if>
                                                    </div>
                                                    <div class="col-md-3 d-flex">
                                                        <div class="col-md-3 d-flex">
                                                            <form action="cartUpdateGuest" method="post" class="d-flex">
                                                                <input type="hidden" name="productId" value="${product.id}" />
                                                                <button type="submit" name="action" value="decrease" class="btn btn-outline-secondary btn-sm">−</button>
                                                                <input type="number" value="${quantity}" class="form-control form-control-sm mx-1 text-center" readonly style="width: 60px;" />
                                                                <button type="submit" name="action" value="increase" class="btn btn-outline-secondary btn-sm">+</button>
                                                            </form>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-2 text-end">
                                                        <div class="text-muted small">
                                                            <del><fmt:formatNumber value="${product.originalPrice * quantity}" type="currency"/></del>
                                                        </div>
                                                        <div class="fw-bold"><fmt:formatNumber value="${product.salePrice * quantity}" type="currency"/> </div>
                                                    </div>
                                                    <div class="col-md-2 text-end">
                                                        <form action="cartRemoveGuest" method="post">
                                                            <input type="hidden" name="productId" value="${product.id}">
                                                            <button class="remove-btn" type="submit">×</button>
                                                        </form>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                            <div class="mt-4 text-end">
                                                <form action="cartClearGuest" method="get">
                                                    <button class="btn btn-danger">Clear Cart</button>
                                                </form>
                                            </div>
                                        </c:when>

                                        <c:otherwise>
                                            <p class="text-center text-muted">🛒 Giỏ hàng của bạn đang trống.</p>
                                        </c:otherwise>

                                    </c:choose>


                                    <hr class="my-4">


                                    <div class="pt-4">
                                        <h6><a href="/homePage" class="text-body"><i class="bi bi-arrow-left"></i> Back to shop</a></h6>
                                    </div>
                                </div>
                            </div>

                            <!-- Summary -->
                            <div class="col-lg-4 bg-body-tertiary">
                                <div class="p-5">
                                    <h3 class="fw-bold mb-5">Summary</h3>
                                    <hr class="my-4">

                                    <div class="d-flex justify-content-between mb-4">
                                        <h5 class="text-uppercase">Subtotal</h5>
                                        <h5> ${originalTotal} đ</h5>
                                    </div>

                                    <div class="d-flex justify-content-between mb-4">
                                        <h5 class="text-uppercase text-success">Discount</h5>
                                        <h5>- ${discount} đ</h5>
                                    </div>

                                    <hr class="my-4">

                                    <div class="d-flex justify-content-between mb-5">
                                        <h5 class="text-uppercase">Total Price</h5>
                                        <h5><strong> ${total} đ</strong></h5>
                                    </div>

                                    <form action="orderConfirm" method="get">
                                        <button class="btn btn-dark btn-block w-100" type="submit">Place order</button>
                                    </form>
                                </div>
                            </div>


                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="footer.jsp"/>


</html>
