<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 6/28/2025
  Time: 1:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@ page session="true" %>
<%
    Object userObj = session.getAttribute("user");

    if (userObj != null) {
        // Nếu user là một object (ví dụ như GoogleUser)
        System.out.print("Xin chào, " + userObj.toString());
    } else {
        out.print("Chưa đăng nhập.");
    }
%>
</body>
</html>
