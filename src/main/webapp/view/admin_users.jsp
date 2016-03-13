<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Users</title>
    <style>
        table {
            width: 400px;
            margin: auto; /* Выравнивание формы по центру окна  */
        }
        td {
            text-align: center;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<h2 style="text-align: center">Зарегистрированные пользователи сайта</h2>

<table border="1">
    <tr>
        <td>Id пользователя</td>
        <td>Login</td>
        <td>Электронная почта</td>
        <td>Статус</td>
        <td>Действие</td>
    </tr>
    <c:forEach items="${users}" var="user" varStatus="status">
        <tr>
            <td>${user.id}</td>
            <td>${user.login}</td>
            <td>${user.email}</td>
            <td>${user.isBanned ? 'Заблокирован' : 'Активен'}</td>
            <c:choose>
                <c:when test="${user.isBanned}">
                    <td><a href="${pageContext.servletContext.contextPath}/admin/users/unlock?id=${user.id}">Разблокировать</a></td>
                </c:when>
                <c:otherwise>
                    <td><a href="${pageContext.servletContext.contextPath}/admin/users/lock?id=${user.id}">Заблокировать</a></td>
                </c:otherwise>
            </c:choose>
        </tr>
    </c:forEach>
</table>
</body>
</html>
