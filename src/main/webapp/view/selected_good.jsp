<%--
  Created by IntelliJ IDEA.
  User: Antony
  Date: 08.03.2016
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${good.name}</title>
</head>
<body>


<h2 style="align-content: center">${good.name}</h2>

<jsp:include page="header.jsp"/>

<table border="1">
    <tr>
        <td>Название товара</td>
        <td>${good.name}</td>
    </tr>
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

<%--<form>--%>
    <%--<label for="name">Название </label>--%>
    <%--<input type="text" id="name" name="category" /><br />--%>
    <%--<label for="description">Описание </label>--%>
    <%--<input type="text" id="description" value="tablets" /><br />--%>
    <%--<label for="price">Цена </label>--%>
    <%--<input type="text" id="price" value="pc" /><br />--%>
    <%--<label for="category">Категория</label>--%>
    <%--<input type="text" id="category" value="tv" /><br />--%>
<%--</form>--%>


</table>


</body>
</html>
