<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>My profile</title>
</head>
<body>

<jsp:include page="header.jsp"/>

<h2 style="text-align: center">Мой кабинет</h2>

<form action="${pageContext.servletContext.contextPath}/user/profile/change" method="post">

    <input type="hidden" name="user" value="${user}" />

    <table border="1" bgcolor="#f5deb3">
        <tr>
            <td>Логин</td>
            <td>${user.login}</td>
        </tr>
        <tr>
            <td>Email</td>
            <td>${user.email}</td>
        </tr>

    </table><br/><br/>

    <input type="submit" value="Изменить профиль" />
</form>


</body>
</html>
