

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                                        <h1 class="fw-bold mb-0">Shopping Cart</h1>
                                    </div>
                                    <hr class="my-4">

                                    <!-- Product Item -->
                                    <c:forEach var="item" items="${items}" varStatus="status">
                                        <c:set var="product" value="${products[status.index]}" />
                                        <div class="row mb-4 align-items-center cart-item border-bottom pb-3">
                                            <div class="col-md-2">
                                                <img src="${empty product.img ? '/assets/default-product.jpg' : product.img}" class="https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/b113ad06-5ba3-4b01-b2e2-3593d764dc9a/NIKE+QUEST+5.png">
                                            </div>
                                            <div class="col-md-3">
                                                <h6 class="text-muted mb-1">${product.cate}</h6>
                                                <h6 class="fw-bold mb-1">${product.name}</h6>
                                                <span class="discount-badge">-10%</span>
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
                                                <div class="old-price">${product.price * 0.1}</div>
                                                <div class="fw-bold">€ ${product.price}</div>
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


                                    <hr class="my-4">

                                    <!-- Thêm các sản phẩm khác tương tự nếu cần -->

                                    <div class="pt-4">
                                        <h6><a href="#" class="text-body"><i class="bi bi-arrow-left"></i> Back to shop</a></h6>
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
                                        <h5>€ ${subtotal}</h5>
                                    </div>

                                    <div class="d-flex justify-content-between mb-4">
                                        <h5 class="text-uppercase text-success">Discount</h5>
                                        <h5>-€ ${discount}</h5>
                                    </div>

                                    <hr class="my-4">

                                    <div class="d-flex justify-content-between mb-5">
                                        <h5 class="text-uppercase">Total Price</h5>
                                        <h5><strong>€ ${total}</strong></h5>
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
