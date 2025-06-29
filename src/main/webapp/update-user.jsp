<form method="post" action="${pageContext.request.contextPath}/user?action=updateProfile">
    <input type="text" name="fullName" value="${user.fullName}" placeholder="Full Name" required />
    <input type="text" name="phone" value="${user.phone}" placeholder="Phone" />
    <input type="text" name="gender" value="${user.gender}" placeholder="Gender" />

    <!-- Address -->
    <input type="text" name="addressDetail" value="${user.address.addressDetail}" placeholder="Address Detail" />

    <button type="submit">Update</button>
</form>
