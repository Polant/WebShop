<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${good.name}</title>
</head>
<body>

<jsp:include page="header.jsp"/>

<h2 style="align-content: center">${good.name}</h2>

<form action="${pageContext.servletContext.contextPath}/good/show" method="post">

    <input type="hidden" name="good_id" value="${good.id}" />

    <table border="1" bgcolor="#f5deb3">
        <%--<tr>--%>
            <%--<td>Название товара</td>--%>
            <%--<td>${good.name}</td>--%>
        <%--</tr>--%>
        <tr>
            <td>Описание</td>
            <td>${good.description}</td>
        </tr>
        <tr>
            <td>Цена</td>
            <td>${good.price}$</td>
        </tr>
        <tr>
            <td>Категория</td>
            <td>${good.category}</td>
        </tr>
        <tr>
            <td>Цвет</td>
            <td>${good.color}</td>
        </tr>
        <%--<tr>--%>
            <%--<td>Поставщик</td>--%>
            <%--<td>${good.providerId}</td>--%>
        <%--</tr>--%>
        <tr>
            <td>Производитель</td>
            <td>${good.manufacturerName}</td>
        </tr>
        <tr>
            <td>Дата производства</td>
            <td>${good.manufacturedDate}</td>
        </tr>
        <tr>
            <td>Дата поставки</td>
            <td>${good.deliveryDate}</td>
        </tr>
        <tr>
            <td>Осталось в наличии</td>
            <td>${good.countLeft} шт.</td>
        </tr>

    </table><br/><br/>

    <label for="quantity">Количество: </label>
    <c:choose>
        <c:when test="${good.countLeft > 0}">
            <input type="text" id="quantity" name="quantity" value="1">
            <input type="submit" value="Купить" />
        </c:when>
        <c:otherwise>
            <input type="text" id="quantity" name="quantity" value="0" disabled>
            <input type="submit" value="Купить" disabled/>
        </c:otherwise>
    </c:choose>

</form>


</body>
</html>
