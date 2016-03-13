<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${good.name}</title>
</head>
<body>

<jsp:include page="header.jsp"/>

<h2 style="align-content: center">${good.name}</h2>

<form action="${pageContext.servletContext.contextPath}/user/basket" method="post">

    <input type="hidden" name="good_id" value="${good.id}" />
    <%--<input type="hidden" name="good_name" value="${good.name}" />--%>
    <%--<input type="hidden" name="good_description" value="${good.description}" />--%>
    <%--<input type="hidden" name="good_price" value="${good.price}" />--%>
    <%--<input type="hidden" name="good_category" value="${good.category}" />--%>
    <%--<input type="hidden" name="good_color" value="${good.color}" />--%>
    <%--&lt;%&ndash;<input type="hidden" name="good_provider_id" value="${good.providerId}" />&ndash;%&gt;--%>
    <%--<input type="hidden" name="good_manufacturer_name" value="${good.manufacturerName}" />--%>
    <%--<input type="hidden" name="good_manufacturing_date" value="${good.manufacturedDate}" />--%>
    <%--<input type="hidden" name="good_delivery_date" value="${good.deliveryDate}" />--%>
    <%--<input type="hidden" name="good_count_left" value="${good.countLeft}" />--%>

    <table border="1" bgcolor="#f5deb3">
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

    <c:choose>
        <c:when test="${not empty IS_ADMIN}">
            <a href="${pageContext.servletContext.contextPath}/admin/goods/edit?good_id=${good.id}">Редактировать товар</a>
        </c:when>
        <c:otherwise>
            <label for="quantity">Количество: </label>
            <c:choose>
                <c:when test="${good.countLeft > 0}">
                    <input type="number" id="quantity" name="quantity" min="0" max="${good.countLeft}" value="1">
                    <input type="submit" value="Добавить в корзину" />
                </c:when>
                <c:otherwise>
                    <input type="number" id="quantity" name="quantity" value="0" disabled>
                    <input type="submit" value="Добавить в корзину" disabled/>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>

</form>


</body>
</html>
