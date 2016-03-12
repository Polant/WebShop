<%--
  Created by IntelliJ IDEA.
  User: Antony
  Date: 07.03.2016
  Time: 17:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Web Shop</title>
</head>
<body>

<jsp:include page="header.jsp"/>

<div id="filter_form" style="float:left;">

    <form action="${pageContext.servletContext.contextPath}/" method="POST">
        <fieldset style="background-color: limegreen;">
            <legend>
                Фильтр
            </legend>

            <label for="nameGoods">Название </label>
            <input type="text" id="nameGoods" name="name_goods" value="" /><br /><br />

            Категория: <br />

            <input type="checkbox" id="categorySmartphones" name="category" value="Смартфон" checked/>
            <label for="categorySmartphones">Смартфоны </label><br />
            <input type="checkbox" id="categoryTablets" name="category" value="Планшет" />
            <label for="categoryTablets">Планшеты </label><br />
            <input type="checkbox" id="categoryPC" name="category" value="ПК" />
            <label for="categoryPC">ПК </label><br />
            <input type="checkbox" id="categoryNotebooks" name="category" value="Ноутбук" />
            <label for="categoryNotebooks">Ноутбуки </label><br />
            <input type="checkbox" id="categoryTV" name="category" value="Телевизор" />
            <label for="categoryTV">Телевизоры</label><br /><br />


            Цена: <br />
            <label for="price_from">От </label>
            <input type="number" id="price_from" name="price_from" size="6" min="0" maxlength="6" value="0" /> <br />
            <label for="price_to">До </label>
            <input type="number" id="price_to" name="price_to" size="6" min="0" maxlength="6" value="10000" /> <br /><br />


            <input type="radio" id="is_in_stock_true" name="is_in_stock" value="in_stock" checked="checked" />
            <label for="is_in_stock_true">В наличии </label><br />
            <input type="radio" id="is_in_stock_false" name="is_in_stock" value="not_in_stock" />
            <label for="is_in_stock_false">Нет в наличии </label><br />
            <input type="radio" id="is_in_stock_show_all" name="is_in_stock" value="show_all" />
            <label for="is_in_stock_show_all">Показать все </label><br /><br />

            Цвет: <br />
            <input type="checkbox" id="colorRed" name="color" value="Красный" />
            <label for="colorRed">Красный </label><br />
            <input type="checkbox" id="colorGreen" name="color" value="Зеленый" />
            <label for="colorGreen">Зеленый </label><br />
            <input type="checkbox" id="colorBlue" name="color" value="Синий" />
            <label for="colorBlue">Синий </label><br />
            <input type="checkbox" id="colorBlack" name="color" value="Черный" checked/>
            <label for="colorBlack">Черный </label><br />
            <input type="checkbox" id="colorWhite" name="color" value="Белый" checked/>
            <label for="colorWhite">Белый </label><br /><br />

            Сортировка: <br /><br />
            По названию:
            <div>
                <input type="radio" id="sort_order_by_name_asc" name="sort_order_by_name" value="asc" checked="checked" />
                <label for="sort_order_by_name_asc">По возрастанию </label><br />
                <input type="radio" id="sort_order_by_name_desc" name="sort_order_by_name" value="desc" />
                <label for="sort_order_by_name_desc">По возрастанию </label><br />
            </div>
            По цене:
            <div>
                <input type="radio" id="sort_order_by_price_asc" name="sort_order_by_price" value="asc" checked="checked" />
                <label for="sort_order_by_price_asc">По возрастанию </label><br />
                <input type="radio" id="sort_order_by_price_desc" name="sort_order_by_price" value="desc" />
                <label for="sort_order_by_price_desc">По возрастанию </label><br />
            </div>
            По новизне:
            <div>
                <input type="radio" id="sort_order_by_date_desc" name="sort_order_by_date" value="desc" checked="checked" />
                <label for="sort_order_by_date_desc">От новых к старым </label><br />
                <input type="radio" id="sort_order_by_date_asc" name="sort_order_by_date" value="asc" />
                <label for="sort_order_by_date_asc">От старых к новым </label><br />
            </div>

            <input type="submit" value="ОК" />
            <input type="reset" value="Сбросить" />

        </fieldset>

    </form>

</div>

<table border="1">
    <tr>
        <td>Название товара</td>
        <td>Цена</td>
        <td>Категория</td>
        <td>Цвет</td>
        <td>Производитель</td>
        <td>На складе (шт.)</td>
        <c:if test="${not empty IS_ADMIN}">
            <td>Действие</td>
        </c:if>
    </tr>
    <c:forEach items="${goods}" var="good" varStatus="status">
        <tr>
            <td><a href="${pageContext.servletContext.contextPath}/good/show?good_id=${good.id}">${good.name}</a></td>
            <td>${good.price}</td>
            <td>${good.category}</td>
            <td>${good.color}</td>
            <td>${good.manufacturerName}</td>
            <td>${good.countLeft}</td>
            <c:if test="${not empty IS_ADMIN}">
                <%--При удалении товара, он не удаляется из базы, а лишь обнуляется его поле countLeft -> товара нет на складе--%>
                <td><a href="${pageContext.servletContext.contextPath}/admin/goods/delete?id=${good.id}">Убрать со склада</a> </td>
            </c:if>
        </tr>
    </c:forEach>
</table><br/><br/><br/>

<c:if test="${not empty IS_ADMIN}">
    <td><a href="${pageContext.servletContext.contextPath}/admin/goods/add">Добавить товар</a></td>
</c:if>

</body>
</html>
