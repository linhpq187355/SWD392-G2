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

<%-- Thêm id và ngăn form gửi đi --%>
<form class="flex flex-wrap gap-4 mb-4" id="filterForm" onsubmit="return false;">
    <input
            type="text"
            name="search"
            placeholder="Search by Order ID"
            id="searchInput"
            class="border border-gray-400 rounded-md px-3 py-1 w-60 focus:outline-none focus:ring-1 focus:ring-gray-400"
    />
    <button
            type="button" <%-- Đổi thành type="button" để không submit form --%>
            id="searchButton"
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
            data-sort-key="orderDate" id="orderDateHeader">
            Order Date <i class="fas fa-sort-up ml-1" id="orderDateSortIcon"></i>
        </th>
        <th class="border border-gray-300 font-semibold px-3 py-2 w-28">Status</th>
        <th class="border border-gray-300 font-semibold px-3 py-2 w-24 cursor-pointer flex items-center"
            data-sort-key="total" id="totalHeader">
            Total <i class="fas fa-sort-up ml-1" id="totalSortIcon"></i>
        </th>
        <c:if test="${userRole eq 'Sales'}">
            <th class="border border-gray-300 font-semibold px-3 py-2">Customer Name</th>
        </c:if>
        <th class="border border-gray-300 font-semibold px-3 py-2 w-40">Action</th>
    </tr>
    </thead>
    <tbody id="orderTableBody">
    <c:forEach var="order" items="${orders}">
        <%-- Thêm data-status-id để JS có thể lọc --%>
        <tr class="border border-gray-300" data-status-id="${order.statusId}">
            <td class="border border-gray-300 px-3 py-2">#${order.orderId}</td>
            <td class="border border-gray-300 px-3 py-2" data-order-date="${order.createdAt}">${order.createdAt}</td>
            <td class="border border-gray-300 px-3 py-2">
                <c:forEach var="s" items="${listStatus}">
                    <c:if test="${s.settingId == order.statusId}">${s.name}</c:if>
                </c:forEach>
            </td>
            <td class="border border-gray-300 px-3 py-2" data-total="${order.totalPrice}">$${order.totalPrice}</td>
            <c:if test="${userRole eq 'Sales'}">
                <td class="border border-gray-300 px-3 py-2">${order.receiveName}</td>
            </c:if>
            <td class="border border-gray-300 px-3 py-2 flex items-center gap-4 justify-center">
                <button type="button" class="text-gray-700 text-lg">
                    <i class="fas fa-file-alt"></i>
                </button>
                <a href="orders?action=edit&id=${order.orderId}" class="text-gray-700 text-lg border border-gray-700 rounded px-2 py-1 inline-block">
                    <i class="fas fa-edit"></i>
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<nav class="mt-6 flex items-center gap-2 select-none" id="paginationNav">
</nav>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        // === KHAI BÁO BIẾN VÀ TRẠNG THÁI ===
        const tableBody = document.getElementById('orderTableBody');
        const searchInput = document.getElementById('searchInput');
        const statusFilter = document.getElementById('statusFilter');
        const paginationNav = document.getElementById('paginationNav');
        const orderDateHeader = document.getElementById('orderDateHeader');
        const totalHeader = document.getElementById('totalHeader');
        const orderDateSortIcon = document.getElementById('orderDateSortIcon');
        const totalSortIcon = document.getElementById('totalSortIcon');

        const allRows = Array.from(tableBody.querySelectorAll('tr')); // Lưu tất cả các dòng gốc
        const rowsPerPage = 5;

        // Trạng thái hiện tại của bảng
        let currentPage = 1;
        let currentSort = { key: 'orderDate', asc: true };

        // === CÁC HÀM XỬ LÝ ===

        /**
         * Hàm trung tâm: Cập nhật toàn bộ giao diện bảng dựa trên các bộ lọc,
         * sắp xếp và trang hiện tại.
         */
        function updateTableView() {
            // 1. Lọc dữ liệu
            const searchTerm = searchInput.value.toLowerCase().replace('#', '');
            const selectedStatus = statusFilter.value;

            const filteredRows = allRows.filter(row => {
                const orderId = row.cells[0].textContent.toLowerCase().replace('#', '');
                const statusId = row.dataset.statusId;

                const matchesSearch = searchTerm === '' || orderId.includes(searchTerm);
                const matchesStatus = selectedStatus === 'all' || statusId === selectedStatus;

                return matchesSearch && matchesStatus;
            });

            // 2. Sắp xếp dữ liệu đã lọc
            sortRows(filteredRows, currentSort.key, currentSort.asc);

            // 3. Hiển thị và phân trang
            renderTablePage(filteredRows, currentPage);
            renderPagination(filteredRows.length);
        }

        /**
         * Hiển thị các dòng cho trang hiện tại
         */
        function renderTablePage(rows, page) {
            tableBody.innerHTML = ''; // Xóa nội dung cũ
            if (rows.length === 0) {
                const noResultRow = `<tr><td colspan="6" class="text-center py-4">No orders found.</td></tr>`;
                tableBody.innerHTML = noResultRow;
                return;
            }

            const start = (page - 1) * rowsPerPage;
            const end = start + rowsPerPage;
            const pageRows = rows.slice(start, end);

            pageRows.forEach(row => tableBody.appendChild(row));
        }

        /**
         * Tạo các nút phân trang
         */
        function renderPagination(totalRows) {
            paginationNav.innerHTML = '';
            const totalPages = Math.ceil(totalRows / rowsPerPage);

            if (totalPages <= 1) return;

            // Nút Previous
            if (currentPage > 1) {
                paginationNav.appendChild(createPaginationButton('«', () => {
                    currentPage--;
                    updateTableView();
                }));
            }

            // Các nút số trang
            for (let i = 1; i <= totalPages; i++) {
                const pageNum = i;
                const btn = createPaginationButton(pageNum, () => {
                    currentPage = pageNum;
                    updateTableView();
                }, i === currentPage);
                paginationNav.appendChild(btn);
            }

            // Nút Next
            if (currentPage < totalPages) {
                paginationNav.appendChild(createPaginationButton('»', () => {
                    currentPage++;
                    updateTableView();
                }));
            }
        }

        function createPaginationButton(text, onClick, isActive = false) {
            const btn = document.createElement('button');
            btn.textContent = text;
            if (isActive) {
                btn.className = "border border-gray-800 bg-gray-800 rounded px-3 py-1 text-white font-semibold";
            } else {
                btn.className = "border border-gray-300 rounded px-3 py-1 text-gray-700 hover:bg-gray-100";
                btn.addEventListener('click', onClick);
            }
            return btn;
        }

        /**
         * Sắp xếp một mảng các dòng (tr)
         */
        function sortRows(rows, key, asc) {
            rows.sort((a, b) => {
                let aVal, bVal;
                if (key === 'orderDate') {
                    aVal = new Date(a.querySelector('td[data-order-date]').dataset.orderDate);
                    bVal = new Date(b.querySelector('td[data-order-date]').dataset.orderDate);
                } else if (key === 'total') {
                    aVal = parseFloat(a.querySelector('td[data-total]').dataset.total);
                    bVal = parseFloat(b.querySelector('td[data-total]').dataset.total);
                }
                if (aVal < bVal) return asc ? -1 : 1;
                if (aVal > bVal) return asc ? 1 : -1;
                return 0;
            });
        }

        function updateSortIcons() {
            orderDateSortIcon.className = 'fas fa-sort ml-1';
            totalSortIcon.className = 'fas fa-sort ml-1';

            const iconElement = currentSort.key === 'orderDate' ? orderDateSortIcon : totalSortIcon;
            iconElement.className = `fas ${currentSort.asc ? 'fa-sort-up' : 'fa-sort-down'} ml-1`;
        }


        // === GẮN SỰ KIỆN ===

        // Sự kiện cho bộ lọc
        searchInput.addEventListener('input', () => {
            currentPage = 1; // Quay về trang đầu khi tìm kiếm
            updateTableView();
        });

        statusFilter.addEventListener('change', () => {
            currentPage = 1; // Quay về trang đầu khi lọc
            updateTableView();
        });

        // Sự kiện cho sắp xếp
        function createSortHandler(key) {
            return () => {
                if (currentSort.key === key) {
                    currentSort.asc = !currentSort.asc;
                } else {
                    currentSort.key = key;
                    currentSort.asc = true;
                }
                currentPage = 1;
                updateSortIcons();
                updateTableView();
            }
        }

        orderDateHeader.addEventListener('click', createSortHandler('orderDate'));
        totalHeader.addEventListener('click', createSortHandler('total'));

        // === KHỞI TẠO BAN ĐẦU ===
        updateSortIcons();
        updateTableView();
    });
</script>
</body>
</html>