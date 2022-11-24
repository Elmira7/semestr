<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Вход</title>
    <link rel="stylesheet" href="css/login.css" type="text/css">
</head>
<body>
    <form class = "login-form" action="/login" method="post">
        <input type="text" name="login" placeholder="Введите логин">
        <input type="password" name="password" placeholder="Введите пароль">
        <button type="submit" name="action" value="login">Войти</button>
    </form>
    <form class = "login-form" action="/login" method="post">
        <input type="text" name="login" placeholder="Введите логин">
        <input type="email" name="mail" placeholder="Введите почту">
        <input type="password" name="password" placeholder="Введите пароль">
        <input type="password" name="password_repeat" placeholder="Повторите пароль">
        <button type="submit" name="action" value="registration">Регистрация</button>
    </form>
    <c:if test="${requestScope.errorMessage != null}">
        <div class="error-block">
                ${requestScope.errorMessage}
        </div>
    </c:if>
</body>
<script src="js/jquery-3.6.1.min.js"></script>
<script>
    <c:if test="${requestScope.errorMessage != null}">
        $('.error-block').hide().show(1000).fadeOut(6000)
    </c:if>
</script>
</html>
