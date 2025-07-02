<%-- Created by IntelliJ IDEA. User: LNV --%>
<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Update Order Details - #${order.orderId}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" />
</head>
<body class="bg-white p-6">
<h2 class="font-bold text-lg mb-6">
    Update Order Details - #${order.orderId}
</h2>

<c:if test="${not empty errors}">
    <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-6">
        <strong class="font-bold">Error!</strong>
        <ul class="list-disc ml-5 mt-2">
            <c:forEach var="error" items="${errors}">
                <li><c:out value="${error}"/></li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<form action="orders" method="POST" accept-charset="UTF-8">
    <input type="hidden" name="action" value="update"/>
    <input type="hidden" name="orderId" value="${order.orderId}"/>

    <div class="flex flex-wrap gap-x-20 gap-y-4 mb-6">
        <div class="flex flex-col space-y-2 w-72">
            <label for="receiverName" class="text-sm">Receiver Name:</label>
            <input id="receiverName" name="receiverName" type="text"
                   value="<c:out value='${order.receiveName}'/>"
                   class="border border-gray-400 rounded px-2 py-1 text-sm"/>
        </div>
        <div class="flex flex-col space-y-2 w-72">
            <label for="receiverPhone" class="text-sm">Receiver Phone:</label>
            <input id="receiverPhone" name="receiverPhone" type="text"
                   value="<c:out value='${order.receivePhone}'/>"
                   class="border border-gray-400 rounded px-2 py-1 text-sm"/>
        </div>
        <div class="flex flex-col space-y-2 w-72">
            <label for="receiverMail" class="text-sm">Receiver Mail:</label>
            <input id="receiverMail" name="receiverMail" type="email"
                   value="<c:out value='${order.receiveEmail}'/>"
                   class="border border-gray-400 rounded px-2 py-1 text-sm"/>
        </div>
    </div>

    <div class="mb-6">
        <p class="mb-2 font-semibold">Receiver Address:</p>
        <div class="flex flex-wrap gap-x-20 gap-y-4">
            <div class="flex flex-col space-y-1 w-72">
                <label for="province" class="text-sm pl-2">Province/City:</label>
                <select id="province" name="province"
                        class="border border-gray-400 rounded px-2 py-1 text-sm w-full">
                    <option value="">-- Select Province --</option>
                    <c:forEach var="p" items="${provinces}">
                        <option value="${p.settingId}"
                                <c:if test="${p.settingId == order.provinceId}">selected</c:if>>
                            <c:out value="${p.name}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="flex flex-col space-y-1 w-72">
                <label for="district" class="text-sm pl-2">District:</label>
                <select id="district" name="district"
                        class="border border-gray-400 rounded px-2 py-1 text-sm w-full">
                    <option value="">-- Select District --</option>
                </select>
            </div>
            <div class="flex flex-col space-y-1 w-72">
                <label for="ward" class="text-sm pl-2">Ward:</label>
                <select id="ward" name="ward"
                        class="border border-gray-400 rounded px-2 py-1 text-sm w-full">
                    <option value="">-- Select Ward --</option>
                </select>
            </div>
            <div class="flex flex-col space-y-1 w-72">
                <label for="street" class="text-sm pl-2">Street/House Number:</label>
                <input id="street" name="street" type="text"
                       value="<c:out value='${order.street}'/>"
                       class="border border-gray-400 rounded px-2 py-1 text-sm"/>
            </div>
        </div>
    </div>

    <h3 class="font-bold mb-2">Products in Order</h3>
    <table class="table-auto border-collapse border border-gray-300 w-full mb-6 text-sm">
        <thead>
        <tr class="bg-gray-200">
            <th class="border border-gray-300 font-semibold px-3 py-1 text-left">Product Name</th>
            <th class="border border-gray-300 font-semibold px-3 py-1 text-center w-48">Quantity</th>
            <th class="border border-gray-300 font-semibold px-3 py-1 text-right w-32">Price</th>
            <th class="border border-gray-300 font-semibold px-3 py-1 text-right w-40">Subtotal</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${order.items}">
            <tr>
                <td class="border border-gray-300 px-3 py-1">
                    <c:out value="${item.productName}"/>
                </td>
                <td class="border border-gray-300 px-3 py-1 text-center">
                    <input type="number" name="quantity_${item.productId}" value="${item.quantity}" min="1"
                           class="w-20 text-center border rounded"
                           <c:if test="${userRole == 'Sales'}">readonly</c:if>/>
                    <script>console.log(useRole)</script>
                </td>
                <td class="border border-gray-300 px-3 py-1 text-right">
                    <fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="$"/>
                </td>
                <td class="border border-gray-300 px-3 py-1 text-right">
                    <fmt:formatNumber value="${item.unitPrice * item.quantity}" type="currency" currencySymbol="$"/>
                </td>
            </tr>
        </c:forEach>
        <tr class="font-bold bg-gray-100">
            <td colspan="3" class="border border-gray-300 px-3 py-1">Total Price</td>
            <td class="border border-gray-300 px-3 py-1 text-right">
                <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="$"/>
            </td>
        </tr>
        </tbody>
    </table>

    <div class="flex flex-wrap gap-x-20 gap-y-4 items-start">
        <div class="flex items-center space-x-2 w-72">
            <label for="orderStatus" class="text-sm whitespace-nowrap">Order Status:</label>
            <select id="orderStatus" name="orderStatus"
                    class="border border-gray-400 rounded px-2 py-1 text-sm w-full"
            ${userRole == 'Customer' ? 'disabled="disabled"' : ''}>
                <option value="7" <c:if test="${order.statusId == 7}">selected</c:if>>Pending</option>
                <option value="9" <c:if test="${order.statusId == 9}">selected</c:if>>Confirmed</option>
                <option value="8" <c:if test="${order.statusId == 8}">selected</c:if>>Shipping</option>
                <option value="10831" <c:if test="${order.statusId == 10831}">selected</c:if>>Completed</option>
                <option value="10" <c:if test="${order.statusId == 10}">selected</c:if>>Cancelled</option>
            </select>
            <c:if test="${userRole == 'Customer'}">
                <input type="hidden" name="orderStatus" value="${order.statusId}" />
            </c:if>

        </div>
        <div class="flex flex-col w-72">
            <label for="orderNotes" class="text-sm mb-1">Order Notes:</label>
            <textarea id="orderNotes" name="orderNotes"
                      class="border border-gray-400 rounded resize-y h-32 p-2 w-full"><c:out value="${order.note}"/></textarea>
        </div>
    </div>

    <div class="flex gap-x-6 mt-8">
        <a href="orders?action=list"
           class="border border-gray-500 rounded px-6 py-2 text-sm bg-gray-100 hover:bg-gray-200">Cancel</a>
        <button type="submit"
                class="border border-blue-600 rounded px-6 py-2 text-sm text-white bg-blue-600 hover:bg-blue-700">
            Save Changes
        </button>
    </div>
</form>

<!-- Thêm thư viện jQuery nếu chưa có -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const provinceSelect = document.getElementById('province');
        const districtSelect = document.getElementById('district');
        const wardSelect = document.getElementById('ward');

        const initialDistrict = "${order.districtId}";
        const initialWard = "${order.wardId}";

        function fetchLocations(type, parentId, selectElement, selectedValue) {
            selectElement.innerHTML = '<option value="">-- Please wait... --</option>';

            $.ajax({
                url: 'api/locations',
                method: 'GET',
                data: {
                    type: type,
                    parentId: parentId
                },
                dataType: 'json',
                success: function (data) {
                    selectElement.innerHTML = `<option value="">-- Select ${type.slice(0, -1)} --</option>`;
                    data.forEach(loc => {
                        const option = document.createElement('option');
                        option.value = loc.id;
                        option.textContent = loc.name;
                        if (String(loc.id) === String(selectedValue)) {
                            option.selected = true;
                        }
                        selectElement.appendChild(option);
                    });
                    if (type === 'districts' && initialWard) {
                        fetchLocations('wards', selectedValue, wardSelect, initialWard);
                    }
                },
                error: function () {
                    selectElement.innerHTML = '<option value="">-- Error loading data --</option>';
                }
            });
        }

        provinceSelect.addEventListener('change', function () {
            if (this.value) {
                fetchLocations('districts', this.value, districtSelect, "");
                wardSelect.innerHTML = '<option value="">-- Select Ward --</option>';
            }
        });

        districtSelect.addEventListener('change', function () {
            if (this.value) {
                fetchLocations('wards', this.value, wardSelect, "");
            }
        });

        if (provinceSelect.value) {
            fetchLocations('districts', provinceSelect.value, districtSelect, initialDistrict);
        }

        function formatCurrency(value) {
            return "$" + value.toFixed(2);
        }

        function recalculateTotals() {
            let total = 0;

            $('tbody tr').each(function () {
                const qtyInput = $(this).find('input[type=number]');
                const unitPriceText = $(this).find('td:nth-child(3)').text().replace(/[^0-9.]/g, '');
                const unitPrice = parseFloat(unitPriceText) || 0;
                const qty = parseInt(qtyInput.val()) || 0;

                const subtotal = unitPrice * qty;
                $(this).find('td:nth-child(4)').text(formatCurrency(subtotal));

                total += subtotal;
            });

            // Update total price
            $('td:contains("Total Price")').next().text(formatCurrency(total));
        }

        // Bind change event to quantity inputs
        $('input[type=number]').on('input', function () {
            recalculateTotals();
        });
    });
</script>



</body>
</html>
