<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<jsp:include page="header.jsp" />

<h2>Моя корзина</h2>

<table border="1">
    <tr>
        <td>Название товара</td>
        <td>Цвет</td>
        <td>Производитель</td>
        <td>Цена за 1 шт.</td>
        <td>Количество</td>
        <td>Стоимость</td>
    </tr>
    <c:forEach items="${orderGoods}" var="good" varStatus="status">
        <tr>
            <td><a href="${pageContext.servletContext.contextPath}/good/show?good_id=${good.orderGood.id}">${good.orderGood.name}</a></td>
            <td>${good.orderGood.color}</td>
            <td>${good.orderGood.manufacturerName}</td>
            <td>${good.orderGood.price}</td>
            <td>${good.orderItem.quantity}</td>
            <td>${good.orderGood.price * good.orderItem.quantity}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
