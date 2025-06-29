<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 6/26/2025
  Time: 3:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Product List</title>
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
            <div class="container1" role="main" aria-label="Add Product">
                <h2 style="margin-left: 10%;margin-bottom: 20px;padding-bottom: 10px;border-bottom: solid 1px #ccc;margin-right: 10%;">Add Product</h2>
                <form method="post" action="addProduct" enctype="multipart/form-data">
                    <div style="display: flex; flex-direction: row; align-items: flex-start; justify-content: center; gap: 40px;">
                        <div style="display: flex;flex-wrap: wrap;width: 55%;gap: 20px 90px;">
                            <div class="form-group">
                                <label for="productCode">Product Code<span class="require-field">*</span></label>
                                <input type="text" id="productCode" name="productCode" placeholder="Product Code" value="${requestScope.oldValues.productCode}"/>
                                <c:if test="${not empty requestScope.errors.productCode}">
                                    <p class="error-message">${requestScope.errors.productCode}</p>
                                </c:if>
                            </div>
                            <div class="form-group">
                                <label for="productName">Product Name<span class="require-field">*</span></label>
                                <input type="text" id="productName" name="productName" placeholder="Product Name" value="${requestScope.oldValues.productName}"/>
                                <c:if test="${not empty requestScope.errors.productName}">
                                    <p class="error-message">${requestScope.errors.productName}</p>
                                </c:if>
                            </div>
                            <div class="form-group">
                                <label for="category">Category<span class="require-field">*</span></label>
                                <select id="category" name="category" required>
                                    <option value="" disabled ${empty requestScope.oldValues.category ? 'selected' : ''} hidden>Category</option>
                                    <c:forEach items="${requestScope.categoryList}" var="categoryList">
                                        <option value="${categoryList.id}"
                                            ${requestScope.oldValues.category == categoryList.id ? 'selected' : ''}>
                                                ${categoryList.name}
                                        </option>
                                    </c:forEach>
                                </select>
                                <c:if test="${not empty requestScope.errors.category}">
                                    <p class="error-message">${requestScope.errors.category}</p>
                                </c:if>
                            </div>
                            <div class="form-group">
                                <label for="status">Status<span class="require-field">*</span></label>
                                <select id="status" name="status" required>
                                    <option value="" disabled ${empty requestScope.oldValues.status ? 'selected' : ''} hidden>Active</option>
                                    <option value="active" ${requestScope.oldValues.status == 'active' ? 'selected' : ''}>Active</option>
                                    <option value="inactive" ${requestScope.oldValues.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                                </select>
                                <c:if test="${not empty requestScope.errors.status}">
                                    <p class="error-message">${requestScope.errors.status}</p>
                                </c:if>
                            </div>
                            <div class="form-group">
                                <label for="originalPrice">Original Price<span class="require-field">*</span></label>
                                <input type="text" id="originalPrice" name="originalPrice" placeholder="Original Price" value="${requestScope.oldValues.originalPrice}"/>
                                <c:if test="${not empty requestScope.errors.originalPrice}">
                                    <p class="error-message">${requestScope.errors.originalPrice}</p>
                                </c:if>
                            </div>
                            <div class="form-group">
                                <label for="salePrice">Sale Price<span class="require-field">*</span></label>
                                <input type="text" id="salePrice" name="salePrice" placeholder="Sale Price" value="${requestScope.oldValues.salePrice}"/>
                                <c:if test="${not empty requestScope.errors.salePrice}">
                                    <p class="error-message">${requestScope.errors.salePrice}</p>
                                </c:if>
                            </div>
                            <div class="form-group wide">
                                <label for="description">Description</label>
                                <textarea id="description" name="description" placeholder="Description" >${requestScope.oldValues.description}</textarea>
                                <c:if test="${not empty requestScope.errors.description}">
                                    <p class="error-message">${requestScope.errors.description}</p>
                                </c:if>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label for="stockQuantity">Stock Quantity<span class="require-field">*</span></label>
                                <input type="text" id="stockQuantity" name="stockQuantity" placeholder="Stock Quantity" value="${requestScope.oldValues.stockQuantity}"/>
                                <c:if test="${not empty requestScope.errors.stockQuantity}">
                                    <p class="error-message">${requestScope.errors.stockQuantity}</p>
                                </c:if>
                            </div>

                            <div class="form-group" style="padding-top: 24px;">
                                <label for="image">Image<span class="require-field">*</span></label>
                                <input type="file" id="image" name="image" multiple />
                                <c:if test="${not empty requestScope.errors.image}">
                                    <p class="error-message">${requestScope.errors.image}</p>
                                </c:if>
                            </div>


                            <div class="form-group" style="padding-top: 24px;">
                                <label>Preview Images</label>
                                <div id="preview-container" style="display: flex; gap: 10px; flex-wrap: wrap;"></div>
                            </div>
                        </div>

                    </div>
                    <div class="buttons">
                        <button type="submit" class="add">Add</button>
                        <button type="button" class="cancel" onclick="confirmCancel()">Cancel</button>
                    </div>
                </form>
            </div>
        </section>
    </main>
</div>
<script>
    document.getElementById('image').addEventListener('change', function (event) {
        const previewContainer = document.getElementById('preview-container');
        previewContainer.innerHTML = '';

        const files = event.target.files;
        if (!files.length) return;

        Array.from(files).forEach(file => {
            const reader = new FileReader();
            reader.onload = function (e) {
                const img = document.createElement('img');
                img.src = e.target.result;
                img.style.width = '100px';
                img.style.height = '100px';
                img.style.objectFit = 'cover';
                img.style.border = '1px solid #ccc';
                img.style.borderRadius = '8px';
                previewContainer.appendChild(img);
            };
            reader.readAsDataURL(file);
        });
    });

    function confirmCancel() {
        if (confirm("Are you sure you want to cancel? All unsaved changes will be lost.")) {
            window.location.href = 'productList';
        }
    }
</script>
</body>
</html>
