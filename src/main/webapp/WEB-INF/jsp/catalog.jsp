<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Каталог</title>
    <link rel="stylesheet" href="css/catalog.css" type="text/css">
    <link rel="stylesheet" href="css/header.css" type="text/css">
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
            <c:if test="${requestScope.category != null}">
                <c:if test="${requestScope.category.getParentName() != null}"><a href="/catalog?category=${requestScope.category.getParentName()}">${requestScope.category.getParentName()}</a> → </c:if>
                <c:if test="${requestScope.category.getName() != null}"><a href="/catalog?category=${requestScope.category.getName()}">${requestScope.category.getName()}</a></c:if>
            </c:if>
        </div>

        <div class="catalog-container">
            <c:forEach items="${productSet}" var="product">
                <div class = "product-holder">
                    <form class="product-form" action="/product" method="get">
                        <button class="product-block" type="submit" name="product_id" value="${product.getId()}">
                            <div class="product-block-photo" style="background-image: url('/images/${product.getPathImage()}')"></div>
                            <div class="product-block-info">
                                    ${product.getName()}
                            </div>
                        </button>
                    </form>
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
<script src="js/catalog.js"></script>
<script src="js/header.js"></script>
<script>
    <c:if test="${requestScope.errorMessage != null}">
        $('.error-block').hide().show(1000).fadeOut(6000)
    </c:if>
</script>
</html>
