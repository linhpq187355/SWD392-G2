<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order List</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <script src="https://cdn.tailwindcss.com"></script>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
    />
</head>
<body class="bg-white text-black font-sans p-6">
<h2 class="text-2xl font-bold mb-4">Order List</h2>

<c:if test="${not empty message}">
    <div class="mb-4 text-red-600 font-semibold">
        ${message}
    </div>
</c:if>

<form class="flex flex-wrap gap-4 mb-4">
    <input
            type="text"
            placeholder="Search Orders"
            id="searchInput"
            class="border border-gray-400 rounded-md px-3 py-1 w-60 focus:outline-none focus:ring-1 focus:ring-gray-400"
    />
    <button
            type="submit"
            class="border border-gray-400 rounded-md px-6 py-1 bg-gradient-to-b from-gray-200 to-gray-100 hover:from-gray-100 hover:to-gray-200"
    >
        Search
    </button>
    <select
            id="statusFilter"
            class="border border-gray-400 rounded-md px-3 py-1 w-48 focus:outline-none focus:ring-1 focus:ring-gray-400"
    >
        <option value="all">All Status</option>
        <c:forEach var="s" items="${listStatus}">
            <option value="${s.settingId}">${s.name}</option>
        </c:forEach>
    </select>
</form>

<table class="w-full border-collapse border border-gray-300 text-left text-sm" id="orderTable">
    <thead>
    <tr class="bg-gray-300 select-none">
        <th class="border border-gray-300 font-semibold px-3 py-2 w-24">Order ID</th>
        <th class="border border-gray-300 font-semibold px-3 py-2 w-32 cursor-pointer flex items-center"
            data-sort-key="orderDate"
            id="orderDateHeader"
            tabindex="0"
            role="button"
            aria-label="Sort by Order Date">
            Order Date
            <i class="fas fa-sort-up ml-1" id="orderDateSortIcon"></i>
        </th>
        <th class="border border-gray-300 font-semibold px-3 py-2 w-28">Status</th>
        <th class="border border-gray-300 font-semibold px-3 py-2 w-24 cursor-pointer flex items-center"
            data-sort-key="total"
            id="totalHeader"
            tabindex="0"
            role="button"
            aria-label="Sort by Total">
            Total
            <i class="fas fa-sort-up ml-1" id="totalSortIcon"></i>
        </th>
        <c:if test="${userRole eq 'Staff'}">
            <th class="border border-gray-300 font-semibold px-3 py-2">
                Customer Name
            </th>
        </c:if>
        <th class="border border-gray-300 font-semibold px-3 py-2 w-40">Action</th>
    </tr>
    </thead>
    <tbody id="orderTableBody">
    <c:forEach var="order" items="${orders}">
        <tr class="border border-gray-300">
            <td class="border border-gray-300 px-3 py-2">#${order.orderId}</td>
            <td class="border border-gray-300 px-3 py-2" data-order-date="${order.createdAt}">${order.createdAt}</td>
            <td class="border border-gray-300 px-3 py-2">
                <c:forEach var="s" items="${listStatus}">
                    <c:if test="${s.settingId == order.statusId}">
                        ${s.name}
                    </c:if>
                </c:forEach>
            </td>

            <td class="border border-gray-300 px-3 py-2" data-total="${order.totalPrice}">$${order.totalPrice}</td>

            <c:if test="${userRole eq 'Staff'}">
                <td class="border border-gray-300 px-3 py-2">${order.receiveName}</td>
            </c:if>

            <td class="border border-gray-300 px-3 py-2 flex items-center gap-4 justify-center">
                <button type="button" class="text-gray-700 text-lg">
                    <i class="fas fa-search fa-file-alt"></i>
                </button>
                <a href="orders?action=edit&id=${order.orderId}" class="text-gray-700 text-lg border border-gray-700 rounded px-2 py-1 inline-block">
                    <i class="fas fa-edit"></i>
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<nav class="mt-6 flex items-center gap-2 select-none">
</nav>

<!-- Phần script giữ nguyên -->
<script>
    (() => {
        const tableBody = document.getElementById('orderTableBody');
        const orderDateHeader = document.getElementById('orderDateHeader');
        const totalHeader = document.getElementById('totalHeader');
        const orderDateSortIcon = document.getElementById('orderDateSortIcon');
        const totalSortIcon = document.getElementById('totalSortIcon');

        let currentSort = {
            key: 'orderDate',
            asc: true,
        };

        function clearSortIcons() {
            orderDateSortIcon.classList.remove('fa-sort-up', 'fa-sort-down');
            totalSortIcon.classList.remove('fa-sort-up', 'fa-sort-down');
        }

        function updateSortIcon(header, icon, asc) {
            clearSortIcons();
            if (asc) {
                icon.classList.add('fa-sort-up');
                icon.classList.remove('fa-sort-down');
            } else {
                icon.classList.add('fa-sort-down');
                icon.classList.remove('fa-sort-up');
            }
        }

        function sortTable(key, asc) {
            const rows = Array.from(tableBody.querySelectorAll('tr'));
            rows.sort((a, b) => {
                let aVal, bVal;
                if (key === 'orderDate') {
                    aVal = a.querySelector('td[data-order-date]').getAttribute('data-order-date');
                    bVal = b.querySelector('td[data-order-date]').getAttribute('data-order-date');
                    aVal = new Date(aVal);
                    bVal = new Date(bVal);
                } else if (key === 'total') {
                    aVal = parseFloat(a.querySelector('td[data-total]').getAttribute('data-total'));
                    bVal = parseFloat(b.querySelector('td[data-total]').getAttribute('data-total'));
                }
                if (aVal < bVal) return asc ? -1 : 1;
                if (aVal > bVal) return asc ? 1 : -1;
                return 0;
            });
            rows.forEach(row => tableBody.appendChild(row));
        }

        function handleSortClick(event) {
            const key = event.currentTarget.getAttribute('data-sort-key');
            if (currentSort.key === key) {
                currentSort.asc = !currentSort.asc;
            } else {
                currentSort.key = key;
                currentSort.asc = true;
            }
            sortTable(currentSort.key, currentSort.asc);
            if (currentSort.key === 'orderDate') {
                updateSortIcon(orderDateHeader, orderDateSortIcon, currentSort.asc);
            } else if (currentSort.key === 'total') {
                updateSortIcon(totalHeader, totalSortIcon, currentSort.asc);
            }
        }

        orderDateHeader.addEventListener('click', handleSortClick);
        totalHeader.addEventListener('click', handleSortClick);

        orderDateHeader.addEventListener('keydown', e => {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                orderDateHeader.click();
            }
        });
        totalHeader.addEventListener('keydown', e => {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                totalHeader.click();
            }
        });

        sortTable(currentSort.key, currentSort.asc);
        updateSortIcon(orderDateHeader, orderDateSortIcon, true);
    })();
    (() => {
        const tableBody = document.getElementById('orderTableBody');
        const paginationNav = document.getElementById('paginationNav');

        const rows = Array.from(tableBody.querySelectorAll('tr'));
        const rowsPerPage = 5; // Hoặc 10, tuỳ bạn

        let currentPage = 1;
        const totalPages = Math.ceil(rows.length / rowsPerPage);

        function renderPage(page) {
            currentPage = page;

            rows.forEach((row, index) => {
                row.style.display = (index >= (page - 1) * rowsPerPage && index < page * rowsPerPage) ? '' : 'none';
            });

            renderPagination();
        }

        function renderPagination() {
            paginationNav.innerHTML = '';

            if (currentPage > 1) {
                const prev = document.createElement('button');
                prev.textContent = '«';
                prev.className = "border border-gray-300 rounded px-3 py-1 text-purple-700 font-semibold";
                prev.addEventListener('click', () => renderPage(currentPage - 1));
                paginationNav.appendChild(prev);
            }

            for (let i = 1; i <= totalPages; i++) {
                const btn = document.createElement('button');
                btn.textContent = i;
                if (i === currentPage) {
                    btn.className = "border border-gray-800 bg-gray-800 rounded px-3 py-1 text-white font-semibold";
                } else {
                    btn.className = "border border-gray-300 rounded px-3 py-1 text-gray-700";
                    btn.addEventListener('click', () => renderPage(i));
                }
                paginationNav.appendChild(btn);
            }

            if (currentPage < totalPages) {
                const next = document.createElement('button');
                next.textContent = '»';
                next.className = "border border-gray-300 rounded px-3 py-1 text-purple-700 font-semibold";
                next.addEventListener('click', () => renderPage(currentPage + 1));
                paginationNav.appendChild(next);
            }
        }

        renderPage(1);
    })();
</script>
</body>
</html>
