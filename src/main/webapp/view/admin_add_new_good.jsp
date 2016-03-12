<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add goods</title>
    <style>
        .error_message{
            color: firebrick;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<h2>Добавление товара</h2>

<div id="add_goods_form" style="float:left;">

    <form action="${pageContext.servletContext.contextPath}/admin/goods/add" method="post">

        <fieldset style="background-color: darkseagreen">

            <div style="margin: 0px auto; text-align: center">

                <div style="float:left;">

                    <label for="name">Название : </label><br />
                    <input type="text" id="name" name="name" value="${name}" placeholder="Samsung Galaxy S6"/><br /><br />

                    <label for="description">Описание : </label><br />
                    <textarea id="description" name="description" value="${description}" cols="22" rows="5" placeholder="Качественный смартфон..."></textarea><br /><br />

                    <label for="price">Цена : </label><br />
                    <input type="number" id="price" name="price" value="${price}" min="1" placeholder="700$"/><br /><br />

                    <label for="category">Категория : </label><br />
                    <%--<input type="text" id="category" name="category" value="${category}" placeholder="Смартфон"/><br /><br />--%>

                    <select id="category" name="category" size="1">
                        <option selected="selected" value="Смартфон">Смартфоны</option>
                        <option value="Планшет">Планшеты</option>
                        <option value="ПК">ПК</option>
                        <option value="Ноутбук">Ноутбуки</option>
                        <option value="Телевизор">Телевизоры</option>
                    </select><br /><br />

                    <label for="color">Цвет : </label><br />
                    <%--<input type="text" id="color" name="color" value="${color}" placeholder="Черный"/><br /><br />--%>

                    <select id="color" name="color" size="1">
                        <option value="Красный">Красный</option>
                        <option value="Зеленый">Зеленый</option>
                        <option value="Синий">Синий</option>
                        <option selected="selected" value="Черный">Черный</option>
                        <option value="Белый">Белый</option>
                    </select><br /><br />

                </div>

                <div style="float:left;">

                    <label for="manufacturer_name">Производитель : </label><br />
                    <input type="text" id="manufacturer_name" name="manufacturer_name" value="${manufacturer_name}" placeholder="Samsung"/><br /><br />

                    <label for="manufacturing_date">Дата производства : </label><br />
                    <c:if test="${empty manufacturing_date}">
                        <input type="date" id="manufacturing_date" name="manufacturing_date" min="2000-01-01" value="2000-01-01"/><br /><br />
                    </c:if>
                    <c:if test="${not empty manufacturing_date}">
                        <input type="date" id="manufacturing_date" name="manufacturing_date" min="2000-01-01" value="${manufacturing_date}"/><br /><br />
                    </c:if>


                    <label for="delivery_date">Дата поставки : </label><br />
                    <c:if test="${empty delivery_date}">
                        <input type="date" id="delivery_date" name="delivery_date" min="2000-01-01" value="2000-01-01"/><br /><br />
                    </c:if>
                    <c:if test="${not empty delivery_date}">
                        <input type="date" id="delivery_date" name="delivery_date" min="2000-01-01" value="${delivery_date}"/><br /><br />
                    </c:if>

                    <label for="count_left">Количество на складе : </label><br />
                    <input type="number" id="count_left" name="count_left" min="1" value="${count_left}" placeholder="100"/><br /><br />

                </div>

            </div>

        </fieldset><br/>

        <input type="submit" value="Добавить товар" />
        <input type="reset" value="Сбросить" />

    </form>
</div>

</body>
</html>
