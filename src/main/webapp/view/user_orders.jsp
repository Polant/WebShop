<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>My orders</title>
</head>
<body>

<jsp:include page="header.jsp" />

<h2>Мои заказы</h2><br/><br/>

<table border="1">
    <tr>
        <td>№ заказа</td>
        <td>Дата заказа</td>
        <td>Статус заказа</td>
    </tr>
    <c:forEach items="${orders}" var="order" varStatus="status">
        <tr>
            <td><a href="${pageContext.servletContext.contextPath}/user/orders/selected_order?order_id=${order.id}">Заказ №${order.id}</a></td>
            <td>${order.orderDate}</td>
            <td>${order.status}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
