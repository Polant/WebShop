<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Orders</title>
    <style>
        table {
            width: 400px;
            margin: auto; /* Выравнивание формы по центру окна  */
        }
        td {
            text-align: center;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<h2 style="text-align: center">Зарегистрированные заказы</h2>

<table border="1">
    <tr>
        <td>Id заказа</td>
        <td>Id пользователя</td>
        <td>Дата оформления заказа</td>
        <td>Статус заказа</td>
        <td>Действие</td>
    </tr>

    <c:forEach items="${orders}" var="order" varStatus="status">

        <tr>
            <td>${order.id}</td>
            <td>${order.userId}</td>
            <td>${order.orderDate}</td>
            <td>${order.status}</td>
            <c:choose>
                <%--@elvariable id="JdbcStorage" type="com.polant.webshop.data.JdbcStorage"--%>
                <c:when test="${order.status == JdbcStorage.ORDER_REGISTERED ||  order.status == JdbcStorage.ORDER_REVOKED}">
                    <td><a href="${pageContext.servletContext.contextPath}/admin/orders/set_payed?id=${order.id}">Подтвердить оплату</a></td>
                </c:when>
                <c:when test="${order.status == JdbcStorage.ORDER_PAYED}">
                    <td><a href="${pageContext.servletContext.contextPath}/admin/orders/set_revoked?id=${order.id}">Отменить заказ</a></td>
                </c:when>
            </c:choose>

        </tr>

    </c:forEach>

</table>
</body>
</html>
