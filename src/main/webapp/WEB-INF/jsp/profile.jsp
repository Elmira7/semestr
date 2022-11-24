<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Личный кабинет</title>
    <link rel="stylesheet" href="css/header.css" type="text/css">
    <link rel="stylesheet" href="css/profile.css" type="text/css">
</head>
<body>
    <header>
        <div class="header-logo-picture"></div>
        <div class="header-company-name">Клуб органического земледелия</div>
        <a class="header-button" href="/">Главная</a>
        <p class="header-button" onclick="showCatalog()" >Каталог</p>
        <a class="header-button"  href="/">Акции</a>
        <a class="header-button"  href="/">Контакты</a>
        <a class="header-button"  href="/basket">Корзина</a>
        <c:if test="${sessionScope.login == null}"><a class="header-button" href="/login">Войти</a></c:if>
        <c:if test="${sessionScope.login != null}"><a class="header-button" href="/profile">Личный кабинет</a></c:if>
        <c:if test="${sessionScope.role != null && sessionScope.role.equals(\"admin\")}"><a class="header-button" href="/admin">Админ панель</a></c:if>
    </header>

    <div class="catalog_holder">
        <c:forEach items="${categoryMap}" var="category">
            <div class="catalog_entity">
                <form class="catalog_entity-header" action="/catalog" method="get">
                    <button type="submit" name="category" value="${category.getKey()}">${category.getKey()}</button>
                </form>
                <c:forEach items="${category.getValue()}" var="categories">
                    <form class="catalog_entity-category" action="/catalog" method="get">
                        <button type="submit" name="category" value="${categories}">${categories}</button>
                    </form>
                </c:forEach>
            </div>
        </c:forEach>
    </div>

    <main>
        <form action="/profile" method="post">
            <button type="submit" name="action" value="exit">Выйти</button>
        </form>
        <div class="order-container">
            <c:forEach items="${orderMap.entrySet()}" var="order">
                <div class="order-holder">
                    <p class="order-number">Номер заказа: ${order.getKey().getNumber()}</p>
                    <c:forEach items="${order.getValue()}" var="product">
                        <div class="product-entity">
                            <div class="product-entity-photo" style="background-image: url('/images/${product.getPathImage()}')"></div>
                            <div class="product-entity-info">
                                <p>${product.getName()}</p>
                                    <c:forEach items="${order.getKey().getOrderEntity()}" var="items">
                                        <c:if test="${product.getId() == items.getIdProduct()}"><p>${items.getCount()} штук</p> <p>${items.getPrice() * items.getCount()} руб.</p></c:if>
                                    </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                    <div class="order-status">
                        <p>Статус: ${order.getKey().getStatus()}</p>
                        <p>Общая стоимость: ${requestScope.priceMap.get(order.getKey())}</p>
                    </div>
                    <c:if test="${order.getKey().getStatus().equals('Ожидает оплаты')}">
                        <form class="order-entity-form" action="/order" method="post">
                            <input type="hidden" name="id_order" value="${order.getKey().getId()}">
                            <button class="order-entity-button" name="order" value="post">Оплатить</button>
                        </form>
                    </c:if>
                </div>
            </c:forEach>
        </div>
    </main>
    <c:if test="${requestScope.errorMessage != null}">
        <div class="error-block">
                ${requestScope.errorMessage}
        </div>
    </c:if>
</body>
<script src="js/jquery-3.6.1.min.js"></script>
<script src="js/header.js"></script>
<script>
    <c:if test="${requestScope.errorMessage != null}">
        $('.error-block').hide().show(1000).fadeOut(6000)
    </c:if>
</script>
</html>
