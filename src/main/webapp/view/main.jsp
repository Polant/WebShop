<%--
  Created by IntelliJ IDEA.
  User: Antony
  Date: 07.03.2016
  Time: 17:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Web Shop</title>
</head>
<body>

<div id="filter_form" style="float:left;">

    <form action="${pageContext.servletContext.contextPath}/" method="POST">
        <fieldset style="background-color: limegreen;">
            <legend>
                Фильтр
            </legend>

            Категория: <br />

            <input type="checkbox" id="categorySmartphones" name="category" value="smartphones" />
            <label for="categorySmartphones">Смартфоны </label><br />
            <input type="checkbox" id="categoryTablets" name="category" value="tablets" />
            <label for="categoryTablets">Планшеты </label><br />
            <input type="checkbox" id="categoryPC" name="category" value="pc" />
            <label for="categoryPC">ПК </label><br />
            <input type="checkbox" id="categoryTV" name="category" value="tv" />
            <label for="categoryTV">Телевизоры</label><br /><br />


            Цена: <br />
            <label for="price_from">От </label>
            <input type="text" id="price_from" name="price_from" size="8" maxlength="8" value="0" /> <br />
            <label for="price_to">До </label>
            <input type="text" id="price_to" name="price_to" size="8" maxlength="8" value="1000" /> <br /><br />


            <input type="checkbox" id="is_in_stock_true" name="is_in_stock" value="true" checked="checked" />
            <label for="is_in_stock_true"> Есть в наличии </label><br />
            <input type="checkbox" id="is_in_stock_false" name="is_in_stock" value="false" />
            <label for="is_in_stock_false"> Нет в наличии</label><br /><br />

            Цвет: <br />
            <input type="checkbox" id="colorRed" name="color" value="red" />
            <label for="colorRed">Красный </label><br />
            <input type="checkbox" id="colorGreen" name="color" value="green" />
            <label for="colorGreen">Зеленый </label><br />
            <input type="checkbox" id="colorBlue" name="color" value="blue" />
            <label for="colorBlue">Синий </label><br />
            <input type="checkbox" id="colorBlack" name="color" value="black" />
            <label for="colorBlack">Черный </label><br />
            <input type="checkbox" id="colorWhite" name="color" value="white" />
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



</body>
</html>
