<%--
  Created by IntelliJ IDEA.
  User: Antony
  Date: 08.03.2016
  Time: 11:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Авторизация</title>
</head>
<body>


<div id="authorization_form" style="float:right;">

    <form action="${requestScope['javax.servlet.forward.request_uri']}?${pageContext.request.queryString}&newSession=true" method="POST">
        <fieldset style="background-color: goldenrod">
            <legend>
                Авторизация
            </legend>

            <label for="login">Логин: </label><br />
            <input type="text" id="login" value="" /><br />
            <label for="password">Пароль: </label><br />
            <input type="password" id="password" value="" /><br /><br />

            <input type="submit" value="Войти" />
            <input type="reset" value="Сбросить" />

        </fieldset>

    </form>

</div>

</body>
</html>
