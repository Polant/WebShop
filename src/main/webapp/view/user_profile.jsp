<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>My profile</title>
</head>
<body>

<jsp:include page="header.jsp"/>

<h2>Мой профиль</h2>

<form action="${pageContext.servletContext.contextPath}/user/profile/change" method="post">

    <input type="hidden" name="user" value="${user}" />

    <p>Логин: ${user.login}</p>
    <p>Email: ${user.email}</p>

    <input type="submit" value="Изменить профиль" />
</form>


</body>
</html>
