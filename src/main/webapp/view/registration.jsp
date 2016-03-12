<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registration</title>
    <style>
        form {
            width: 400px;
            margin: auto; /* Выравнивание формы по центру окна  */
        }
        .error_message{
            color: firebrick;
        }
    </style>
</head>
<body>

<form action="${pageContext.servletContext.contextPath}/user/registration" method="POST">
    <fieldset style="background-color: darkseagreen">
        <legend>
            Регистрация
        </legend>

        <p>Обязательные поля обозначены *зведочкой : </p>

        <c:if test="${not empty PASSWORDS_NOT_EQUALS}">
            <p class="error_message">Пароли должны совпадать!</p>
        </c:if>
        <c:if test="${not empty LOGIN_ALREADY_EXISTS}">
            <p class="error_message">Данный логин уже существует!</p>
        </c:if>

        <div style="margin: 0px auto; text-align: center">
            <label for="login">*Логин : </label><br />
            <input type="text" id="login" name="login" value="" placeholder="login"/><br /><br />
            <label for="login">Email : </label><br />
            <input type="text" id="email" name="email" value="" placeholder="your.email@mail.com"/><br /><br />
            <label for="passwordFirst">*Введите пароль : </label><br />
            <input type="password" id="passwordFirst" name="password" value="" placeholder="password"/><br /><br />
            <label for="passwordSecond">*Подтвердите пароль: </label><br />
            <input type="password" id="passwordSecond" name="password" value="" placeholder="password"/><br /><br />

            <input type="submit" value="Зарегистрироваться" />
            <input type="reset" value="Сбросить" />
        </div>


    </fieldset>

</form>
</body>
</html>
