<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Авторизация</title>
</head>
<body>

<div id="header_menu" style="float: left;">
    <table bgcolor="#32cd32" border="2">
        <tr>
            <td><a href="${pageContext.servletContext.contextPath}">Главная страница</a></td>
            <td><a href="${pageContext.servletContext.contextPath}/user/profile">Мой кабинет</a></td>
            <td><a href="${pageContext.servletContext.contextPath}/user/orders">Мои заказы</a></td>
            <td><a href="${pageContext.servletContext.contextPath}/user/basket">Моя корзина</a></td>
        </tr>
    </table>
</div><br/><br/>


<div id="authorization_form" style="float:right;">

    <form action="${pageContext.servletContext.contextPath}/user/login" method="POST">
        <fieldset style="background-color: goldenrod">
            <legend>
                Авторизация
            </legend>

            <%--Сохраняю текущий URL, чтобы после авторзации или вызода из системы вернуться к нему--%>
            <input type="hidden" name="lastURL"
                   value="${requestScope['javax.servlet.forward.request_uri']}?${pageContext.request.queryString}" />

            <c:if test="${not empty login}">
                <p>Здравствуйте, ${login}</p><br />
                <input type="hidden" name="log_out" value="true">
                <input type="submit" value="Выйти" />
            </c:if>
            <c:if test="${empty login}">
                <label for="login">Логин: </label><br />
                <input type="text" id="login" name="login" value="" /><br />
                <label for="password">Пароль: </label><br />
                <input type="password" id="password" name="password" value="" /><br /><br />

                <input type="submit" value="Войти" />
                <input type="reset" value="Сбросить" />

                <a href="${pageContext.servletContext.contextPath}/user/registration">Регистрация</a>
            </c:if>

        </fieldset>

    </form>

</div>

</body>
</html>
