<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Главная</title>
    <link rel="stylesheet" href="css/homepage.css">
    <link rel="stylesheet" href="css/header.css">
</head>
<body>
    <header>
<%--        <div class="header-logo-picture">--%>
            <img class="header-logo-picture" src="css/wcvdhfLduEY.jpg">
<%--        </div>--%>
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
        <div class = "promotion-holder">
            <c:forEach items="${promotionSet.entrySet()}" var="promotion">
                <div class ="promotion-entity">
                    <div class="promotion-entity-header">${promotion.getKey().getName()}</div>
                    <div class="promotion-entity-product">
                        <c:forEach items="${promotion.getValue()}" var="product">
                            <form action="/product" method="get">
                                <button name="product_id" value="${product.getId()}" class="promotion-entity-product-entity">
                                    <div class="promotion-entity-product-entity-photo" style="background-image: url('/images/${product.getPathImage()}')"></div>
                                    <div class = "promotion-entity-product-entity-name">${product.getName()}</div>
                                    <div class = "promotion-entity-product-entity-price"><s>${product.getPrice()}</s> ${product.getPrice() - product.getPrice() * promotion.getKey().getDiscount() * 0.01}</div>
                                </button>
                            </form>
                        </c:forEach>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class ="promotion-entity">
            <div class="promotion-entity-header">Хиты продаж</div>
            <div class="promotion-entity-product">
                <c:forEach items="${topProduct}" var="product">
                    <form action="/product" method="get">
                        <button name="product_id" value="${product.getId()}" class="promotion-entity-product-entity">
                            <div class="promotion-entity-product-entity-photo" style="background-image: url('/images/${product.getPathImage()}')"></div>
                            <div class = "promotion-entity-product-entity-name">${product.getName()}</div>
                            <div class = "promotion-entity-product-entity-price"><s>${product.getPrice()}</s> ${product.getPrice()}</div>
                        </button>
                    </form>
                </c:forEach>
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
<script src="js/homepage.js"></script>
<script src="js/header.js"></script>
<script>
    <c:if test="${requestScope.errorMessage != null}">
    $('.error-block').hide().show(1000).fadeOut(6000)
    </c:if>
</script>
</html>
