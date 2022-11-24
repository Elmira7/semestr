<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>${requestScope.product.getName()}</title>
    <link rel="stylesheet" href="css/header.css" type="text/css">
    <link rel="stylesheet" href="css/product.css" type="text/css">
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

        <div class="category-info-block">
            <c:if test="${requestScope.product.getCategory().getParentName() != null}"><a href="/catalog?category=${requestScope.product.getCategory().getParentName()}">${requestScope.product.getCategory().getParentName()}</a> → </c:if>
            <c:if test="${requestScope.product.getCategory().getName() != null}"><a href="/catalog?category=${requestScope.product.getCategory().getName()}">${requestScope.product.getCategory().getName()}</a> → </c:if>
            ${requestScope.product.getName()}
        </div>
        <div class="product-entity">
            <div class="product-entity-block-photo">
                <div class="product-entity-photo-holder" style="background-image: url('/images/${requestScope.product.getPathImage()}')"></div>
            </div>
            <div class="product-entity-info-block">
                <p>Описание: ${requestScope.product.getDescription()}</p>
                <p>Характеристики:</p>
                <c:forEach items="${requestScope.product.getFeature()}" var="feature">
                    <p>${feature.getKey()}: ${feature.getValue()}</p>
                </c:forEach>
                <p>В наличии: ${requestScope.product.getCount()}</p>
                <c:if test="${!requestScope.promotionSet.isEmpty()}">
                    <p>Акции:</p>
                </c:if>
                <c:forEach items="${promotionSet}" var="promotion">
                   <p>${promotion.getName()}</p>
                </c:forEach>

                <c:if test="${requestScope.product.getPrice() != price}">
                    <p>Цена: <s>${requestScope.product.getPrice()}</s> <b>${price}</b> руб.</p>
                </c:if>
                <c:if test="${requestScope.product.getPrice() == price}">
                    <p>Цена: <b>${price}</b> руб.</p>
                </c:if>
                <form class = "product-entity-form-basket" action="/product" method="post">
                    <input type="hidden" name="product" value="${requestScope.product.getId()}">
                    <input type="hidden" name="user" value="${requestScope.user.getId()}">
                    <button class = "product-entity-button-basket" type="submit">Добавить в корзину</button>
                </form>
            </div>
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
