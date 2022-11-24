<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Корзина</title>
    <link rel="stylesheet" href="css/basket.css" type="text/css">
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
        <div class="basket-holder">
            <c:forEach items="${basketMap.entrySet()}" var="basket">
                <div class="basket-entity">
                    <div class="basket-photo-holder" style="background-image: url('/images/${basket.getKey().getPathImage()}')"></div>
                    <div class="basket-info-block">
                        <p class="basket-info-block-category"><c:if test="${basket.getKey().getCategory().getParentName() != null}">${basket.getKey().getCategory().getParentName()} → </c:if>
                        <c:if test="${basket.getKey().getCategory().getName() != null}">${basket.getKey().getCategory().getName()} → </c:if>
                            ${basket.getKey().getName()}
                        </p>
                        <form action="/basket" method="post">
                        <p>Количество: <input id="count-input" type="text" name="count" onchange="updateCount(this, ${basket.getKey().getCount()})" value="${basket.getValue().getCount()}">
                            <input type="hidden" name="user" value="${basket.getValue().getUserId()}">
                            <input type="hidden" name="product" value="${basket.getValue().getProductId()}">
                            <button type="submit" name="change" value="count">Потвердить</button>
                        </p>
                        </form>
                        <c:if test="${basket.getKey().getPrice() != requestScope.promotionMap.get(basket.getKey())}">
                            <p>Цена <s>${basket.getKey().getPrice() * basket.getValue().getCount()}</s> ${requestScope.promotionMap.get(basket.getKey()) * basket.getValue().getCount()} рублей</p>
                        </c:if>
                        <c:if test="${basket.getKey().getPrice() == requestScope.promotionMap.get(basket.getKey())}">
                            <p>Цена ${basket.getKey().getPrice() * basket.getValue().getCount()} рублей</p>
                        </c:if>
                    </div>
                    <div class="basket-remove">
                        <form class="basket-remove-form" action="/basket" method="post">
                            <input type="hidden" name="user" value="${basket.getValue().getUserId()}">
                            <input type="hidden" name="product" value="${basket.getValue().getProductId()}">
                            <button class="basket-remove-button" type="submit" name="remove" value="basket"></button>
                        </form>
                    </div>
                </div>
            </c:forEach>
            <div class="basket-order">
                Итого: ${requestScope.price} рублей
                <form action="/order" method="post">
                    <button class="basket-order-button" name="create" value="order">Заказать</button>
                </form>
            </div>
        </div>
        <c:if test="${requestScope.errorMessage != null}">
            <div class="error-block">
                    ${requestScope.errorMessage}
            </div>
        </c:if>
    </main>
</body>
<script src="js/jquery-3.6.1.min.js"></script>
<script src="js/basket.js"></script>
<script src="js/header.js"></script>

<script>
    function updateCount(element, count){
        if (element.value > count) {
            alert('Вы ввели количество товаров, превышаюйщиеся имеющиеся');
            element.value = count;
        }
    }

    <c:if test="${requestScope.errorMessage != null}">
        $('.error-block').hide().show(1000).fadeOut(6000)
    </c:if>
</script>
</html>
