<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Basket</title>
</head>
<body>

<jsp:include page="header.jsp" />

<h2>Моя корзина</h2>

<h3>Заказ № ${order.id}</h3>

<table border="1">
    <tr>
        <td>Название товара</td>
        <td>Цвет</td>
        <td>Производитель</td>
        <td>Цена за 1 шт.</td>
        <td>Количество</td>
        <td>Стоимость</td>
    </tr>
    <c:set var="orderSum" value="0"/>
    <c:forEach items="${orderGoods}" var="good" varStatus="status">
        <tr>
            <td><a href="${pageContext.servletContext.contextPath}/good/show?good_id=${good.orderGood.id}">${good.orderGood.name}</a></td>
            <td>${good.orderGood.color}</td>
            <td>${good.orderGood.manufacturerName}</td>
            <td>${good.orderGood.price}$</td>
            <td>${good.orderItem.quantity}</td>
            <td>${good.orderGood.price * good.orderItem.quantity}</td>
            <c:set var="orderSum" value="${orderSum + good.orderGood.price * good.orderItem.quantity}"/>
        </tr>
    </c:forEach>
</table>

<h3>Статус заказа: ${order.status}</h3>
<h3>Дата заказа: ${order.orderDate}</h3>
<h3>К оплате: ${orderSum}$</h3>

<form action="${pageContext.servletContext.contextPath}/user/basket" method="post">
    <input type="hidden" name="order_id" value="${order.id}">
    <c:if test="${!IS_PAYED}">
        <%--Оплата, если еще не оплачено--%>
        <input type="hidden" name="pay_for_order" value="true">
        <input type="submit" value="Оплатить заказ"/>
    </c:if>
    <c:if test="${IS_PAYED}">
        <%--Отмена оплаты, если уже оплачено--%>
        <input type="hidden" name="cancel_pay_for_order" value="true">
        <input type="submit" value="Отменить оплату"/>
    </c:if>
</form>

</body>
</html>
