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

<jsp:include page="header.jsp"/>

<h2>Мой профиль</h2>

<form action="${pageContext.servletContext.contextPath}}/user/profile/change" method="POST">
    <fieldset style="background-color: darkseagreen">
        <legend>
            Редактирование
        </legend>
        <c:if test="${not empty EMPTY_PASSWORD}">
            <p class="error_message">Пароль не может быть пустым!</p>
        </c:if>
        <c:if test="${not empty PASSWORDS_NOT_EQUALS}">
            <p class="error_message">Пароли должны совпадать!</p>
        </c:if>
        <c:if test="${not empty EMAIL_NOT_VALID}">
            <p class="error_message">Почта имеет неверный формат!</p>
        </c:if>

        <div style="margin: 0px auto; text-align: center">
            <label for="email">Email : </label><br />
            <input type="text" id="email" name="email" value="${email}" placeholder="your.email@mail.com"/><br /><br />
            <label for="passwordOld">Введите старый пароль : </label><br />
            <input type="password" id="passwordOld" name="password_old" value="${password}" placeholder="old password"/><br /><br />
            <label for="passwordNewFirst">Введите новый пароль: </label><br />
            <input type="password" id="passwordNewFirst" name="password_new" value="${password}" placeholder="new password"/><br /><br />
            <label for="passwordNewSecond">Подтвердите новый пароль: </label><br />
            <input type="password" id="passwordNewSecond" name="password_new" value="${password}" placeholder="new password"/><br /><br />

            <input type="submit" value="Зарегистрироваться" />
            <input type="reset" value="Сбросить" />
        </div>

    </fieldset>

</form>
</body>
</html>
