<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Заказы</title>
    <link rel="stylesheet" href="../css/admin-order.css" type="text/css">
</head>
<body>
    <main>
        <form action="/admin/order" method="get">
            <select name="status">
                <option value="all" selected>Все заказы</option>
                <option value="pay">Ожидающие оплаты</option>
                <option value="collect">Ожидающие сборки</option>
                <option value="ready">Готовые</option>
            </select>
            <button type="submit" name="order" value="filter">Фильтр</button>
        </form>
        <div class="order-container">
            <c:forEach items="${orderMap.entrySet()}" var="order">
                <div class="order-holder">
                    <div class="order-number">
                        <p>Номер заказа: ${order.getKey().getNumber()}</p>
                        <p>Почта клиента: ${requestScope.userMap.get(order.getKey().getId()).getEmail()}</p>
                    </div>
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
                    <c:if test="${order.getKey().getStatus().equals('Ожидает сборки')}">
                        <form class="order-entity-form" action="/order" method="post">
                            <input type="hidden" name="id_order" value="${order.getKey().getId()}">
                            <button class="order-entity-button" name="order" value="collect">Собран</button>
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
<script src="../js/jquery-3.6.1.min.js"></script>
<script>
    <c:if test="${requestScope.errorMessage != null}">
    $('.error-block').hide().show(1000).fadeOut(6000)
    </c:if>
</script>
</html>
