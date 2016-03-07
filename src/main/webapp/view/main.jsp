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

<form action="${pageContext.servletContext.contextPath}/" method="POST">
    <fieldset>
        <legend>
            Фильтр
        </legend>

        Категория: <br />
        <input type="checkbox" name="category" value="smartphones" /> Смартфоны<br />
        <input type="checkbox" name="category" value="tablets" /> Планшеты<br />
        <input type="checkbox" name="category" value="pc" /> ПК<br />
        <input type="checkbox" name="category" value="tv" /> Телевизоры<br /><br />

        Цена: <br />
        <input type="text" name="price_from" size="8" maxlength="8" value="0" /> <br />
        <input type="text" name="price_to" size="8" maxlength="8" value="1000" /> <br /><br />

        <input type="checkbox" name="is_in_stock" value="true" checked="checked" /> Есть в наличии<br />
        <input type="checkbox" name="is_in_stock" value="false" /> Нет в наличии<br /><br />

        Цвет: <br />
        <input type="checkbox" name="color" value="red" /> Красный<br />
        <input type="checkbox" name="color" value="green" /> Зеленый<br />
        <input type="checkbox" name="color" value="blue" /> Синий<br />
        <input type="checkbox" name="color" value="black" /> Черный<br />
        <input type="checkbox" name="color" value="white" /> Белый<br /><br />

        Сортировка: <br /><br />
        По названию:
        <div>
            <input type="radio" name="sort_order_by_name" value="asc" checked="checked" /> По возрастанию<br />
            <input type="radio" name="sort_order_by_name" value="desc" /> По убыванию<br />
        </div>
        По цене:
        <div>
            <input type="radio" name="sort_order_by_price" value="asc" checked="checked" /> По возрастанию<br />
            <input type="radio" name="sort_order_by_price" value="desc" /> По убыванию<br />
        </div>
        По новизне:
        <div>
            <input type="radio" name="sort_order_by_date" value="desc" checked="checked" /> От новых к старым<br />
            <input type="radio" name="sort_order_by_date" value="asc" /> От старых к новым<br />
        </div>

        <input type="submit" value="ОК" />
        <input type="reset" value="Сбросить" />
    </fieldset>
</form>

</body>
</html>
