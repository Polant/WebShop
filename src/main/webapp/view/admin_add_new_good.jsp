<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add goods</title>
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
                    <select id="category" name="category" size="1">
                        <c:forEach items="${categoriesList}" var="category">
                            <option value="${category}" ${category == selectedCategory ? 'selected' : ''}>${category}</option>
                        </c:forEach>
                    </select><br /><br />

                    <label for="color">Цвет : </label><br />
                    <select id="color" name="color" size="1">
                        <c:forEach items="${colorsList}" var="color">
                            <option value="${color}" ${color == selectedColor ? 'selected' : ''}>${color}</option>
                        </c:forEach>
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
