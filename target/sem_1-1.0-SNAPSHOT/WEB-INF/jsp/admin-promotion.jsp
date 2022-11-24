<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Редактор акций</title>
    <link rel="stylesheet" href="../css/admin-promotion.css" type="text/css">
</head>
<body>
    <a href="/admin">Назад</a>
    <p>Добавить акцию</p>
    <form action="/admin/promotion" method="post">
        Имя<input type="text" name="name" placeholder="Введите имя">
        Описание<textarea placeholder="Введите описание" name="description"></textarea>
        <input type="date" name="date_begin" placeholder="Дата начала">
        <input type="date" name="date_end" placeholder="Дата конца">
        Размер скидки<input type="text" name="discount" placeholder="Введите размер скидки">
        <button type="submit" name="add" value="promotion">Добавить акцию</button>
    </form>

    <c:forEach items="${promotionProduct.entrySet()}" var="promotion">
        ${promotion.getKey().getName()}
        <form action="/admin/promotion" method="post">
            Добавить товар <select name="product">
                <c:forEach items="${productSet}" var="product">
                    <option value="${product.getId()}">${product.getCategory().getName()} ${product.getName()}</option>
                </c:forEach>
            </select>
            <input type="hidden" name="promotion" value="${promotion.getKey().getId()}">
            <button type="submit" name="add" value="product">Добавить продукт к акции</button>
        </form>
        <c:forEach items="${promotion.getValue()}" var="promotionProduct">
            <a href="/product?product_id=${promotionProduct.getId()}">${promotionProduct.getName()}</a>
        </c:forEach>
        <form action="/admin/promotion" method="post">
            <input type="hidden" name="id_promotion" value="${promotion.getKey().getId()}">
            <button type="submit" name="promotion" value="remove">Удалить акцию</button>
        </form>
    </c:forEach>

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
