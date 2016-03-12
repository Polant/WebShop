<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>My profile</title>
</head>
<body>

<jsp:include page="header.jsp"/>

<h2>Мой профиль</h2>

<form>
    <p>Логин: ${user.login}</p>
    <p>Email: ${user.email}</p>

    <a href="${pageContext.servletContext.contextPath}/user/profile/change">Изменить профиль</a>
</form>


</body>
</html>
