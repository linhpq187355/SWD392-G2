<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 6/24/2025
  Time: 6:52 PM
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
<div id="toast" class="toast-popup">Product added successfully!</div>
<div id="toast2" class="toast-popup">Product updated successfully!</div>
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
      <div class="container1" role="main" aria-label="Product List">
        <h2>Product List</h2>
        <div class="top-controls">
          <div class="search-wrapper">
            <input name="search" type="search" placeholder="Search" aria-label="Search products" />
            <i class="fas fa-search" aria-hidden="true"></i>
          </div>
          <button class="btn-add" type="button" aria-label="Add Product" onclick="window.location.href='addProduct'">
            <i class="fas fa-plus" aria-hidden="true"></i> Add Product
          </button>
          <div class="select-wrapper" role="group" aria-label="Filter products">
            <select aria-label="Filter by Status" name="status">
              <option value="">Status</option>
              <option value="true">Active</option>
              <option value="false">Inactive</option>
            </select>
            <select aria-label="Filter by Category" name="category" style="width: 100px;">
              <option value="">Category</option>
              <c:forEach items="${requestScope.categoryList}" var="categoryList">
                  <option value="${categoryList.name}">${categoryList.name}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <table role="table" aria-label="Product list table">
          <thead>
          <tr>
            <th scope="col">Product Name</th>
            <th scope="col">Category</th>
            <th scope="col" class="sortable" data-field="stock" aria-sort="none" tabindex="0" aria-label="Stock sortable column">
              Stock
              <i class="fas fa-sort-up" aria-hidden="true"></i>
              <i class="fas fa-sort-down" aria-hidden="true"></i>
            </th>
            <th scope="col" class="sortable" data-field="originalPrice" aria-sort="none" tabindex="0" aria-label="OriginalPrice sortable column">
              Original Price
              <i class="fas fa-sort-up" aria-hidden="true"></i>
              <i class="fas fa-sort-down" aria-hidden="true"></i>
            </th>
            <th scope="col" class="sortable" data-field="salePrice" aria-sort="none" tabindex="0" aria-label="SalePrice sortable column">
              Sale Price
              <i class="fas fa-sort-up" aria-hidden="true"></i>
              <i class="fas fa-sort-down" aria-hidden="true"></i>
            </th>
            <th scope="col">Status</th>
            <th scope="col">Action</th>
          </tr>
          </thead>
          <tbody id="product-body">

          </tbody>
        </table>
        <nav class="pagination" aria-label="Pagination Navigation">

        </nav>
      </div>
    </section>
  </main>
</div>
<script>
  let allProducts = [];
  let filteredProducts = [];
  let currentPage = 1;
  const pageSize = 10;

  window.addEventListener('DOMContentLoaded', () => {
    fetch('productList', {
      headers: {
        'Accept': 'application/json'
      }
    })
            .then(res => res.json())
            .then(data => {
              allProducts = data;
              applyFilters();
            });

    document.querySelectorAll('input[name="search"], select[name="status"], select[name="category"]').forEach(el => {
      el.addEventListener('input', () => {
        currentPage = 1;
        applyFilters();
      });
    });
  });

  function applyFilters() {
    const search = document.querySelector('input[name="search"]').value.toLowerCase();
    const statusValue = document.querySelector('select[name="status"]').value;
    const category = document.querySelector('select[name="category"]').value;
    const status = statusValue === "true" ? true :
            statusValue === "false" ? false :
                    null;

    filteredProducts = allProducts.filter(p => {
      return (!search || p.name.toLowerCase().includes(search))
              && (status === null || p.isActive === status)
              && (!category || p.category.name === category);
    });
    sortProducts();
    renderPage();
  }

  let sortField = "";
  let sortDir = "asc";
  function sortProducts() {
    if (!sortField) return;
    filteredProducts.sort((a, b) => {
      let valA = a[sortField];
      let valB = b[sortField];
      if (typeof valA === 'string') {
        valA = valA.toLowerCase();
        valB = valB.toLowerCase();
      }
      return sortDir === 'asc' ? valA > valB ? 1 : -1 : valA < valB ? 1 : -1;
    });
  }

  // Sự kiện click để sort
  document.querySelectorAll('.sortable').forEach(th => {
    th.addEventListener('click', () => {
      const field = th.dataset.field;
      if (sortField === field) {
        sortDir = sortDir === 'asc' ? 'desc' : 'asc';
      } else {
        sortField = field;
        sortDir = 'asc';
      }
      applyFilters();
    });
  });

  function renderPage() {
    const start = (currentPage - 1) * pageSize;
    const end = start + pageSize;
    const pageData = filteredProducts.slice(start, end);

    const tbody = document.getElementById('product-body');
    tbody.innerHTML = pageData.map(p => `
    <tr>
      <td>` + p.name + `</td>
      <td>` + p.category.name + `</td>
      <td>` + p.stock + `</td>
      <td>` + p.originalPrice + `</td>
      <td>` + p.salePrice + `</td>
      <td>` + (p.isActive ? 'Active' : 'Inactive') + `</td>
      <td class="action">
            <a href="productUpdate?id=` + p.id +`" aria-label="Edit ` + p.name +`">
                <button type="button">
                    <i class="fas fa-edit"></i>
                </button>
            </a>
          </td>
    </tr>
`).join('');
    renderPagination();
  }

  // Phân trang
  function renderPagination() {
    const totalPages = Math.ceil(filteredProducts.length / pageSize);
    const container = document.querySelector('.pagination');
    container.innerHTML = '';

    // Nút Previous
    const prevBtn = document.createElement('button');
    prevBtn.innerHTML = `<i class="fas fa-arrow-left"></i> Previous`;
    prevBtn.disabled = currentPage === 1;
    prevBtn.addEventListener('click', () => {
      if (currentPage > 1) {
        currentPage--;
        renderPage();
      }
    });
    container.appendChild(prevBtn);

    // Hiển thị một số trang (ví dụ 1 ... 4 5 [6] 7 8 ... totalPages nếu nhiều)
    const maxVisiblePages = 5;
    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = startPage + maxVisiblePages - 1;
    if (endPage > totalPages) {
      endPage = totalPages;
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    if (startPage > 1) {
      createPageButton(1);
      if (startPage > 2) {
        createEllipsis();
      }
    }

    for (let i = startPage; i <= endPage; i++) {
      createPageButton(i);
    }

    if (endPage < totalPages) {
      if (endPage < totalPages - 1) {
        createEllipsis();
      }
      createPageButton(totalPages);
    }

    // Nút Next
    const nextBtn = document.createElement('button');
    nextBtn.innerHTML = `Next <i class="fas fa-arrow-right"></i>`;
    nextBtn.disabled = currentPage === totalPages;
    nextBtn.addEventListener('click', () => {
      if (currentPage < totalPages) {
        currentPage++;
        renderPage();
      }
    });
    container.appendChild(nextBtn);

    // --- Helper functions ---
    function createPageButton(i) {
      const btn = document.createElement('button');
      btn.textContent = i;
      btn.className = currentPage === i ? 'active' : '';
      if (currentPage === i) {
        btn.setAttribute('aria-current', 'page');
      }
      btn.addEventListener('click', () => {
        currentPage = i;
        renderPage();
      });
      container.appendChild(btn);
    }

    function createEllipsis() {
      const ellipsis = document.createElement('button');
      ellipsis.textContent = '...';
      ellipsis.disabled = true;
      container.appendChild(ellipsis);
    }
  }
</script>
<c:if test="${param.message == 'addSuccess'}">
  <script>
    window.addEventListener('DOMContentLoaded', () => {
      const toast = document.getElementById('toast');
      toast.classList.add('show');
      setTimeout(() => {
        toast.classList.remove('show');
      }, 3000); // toast tự ẩn sau 3 giây
    });
  </script>
</c:if>
<c:if test="${param.message == 'updateSuccess'}">
  <script>
    window.addEventListener('DOMContentLoaded', () => {
      const toast = document.getElementById('toast2');
      toast.classList.add('show');
      setTimeout(() => {
        toast.classList.remove('show');
      }, 3000); // toast tự ẩn sau 3 giây
    });
  </script>
</c:if>
</body>
</html>
