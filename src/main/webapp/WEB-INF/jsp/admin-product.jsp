<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <a href="/admin">Назад</a>
    <title>Редактор товаров</title>
    <link rel="stylesheet" href="../css/admin-product.css" type="text/css">
</head>
<body>
    <p>Добавить категорию</p>
    <form class="category-add-form" action="/admin/product" method="post">
        <select name="parent_name">
            <option value="" selected>Без категории родителя</option>
            <c:forEach items="${categoryMap.keySet()}" var="category">
                <option value="${category.getName()}">${category.getName()}</option>
            </c:forEach>
        </select>
        <input type="text" name="name" placeholder="Введите категорию">
        <button type="submit" name="add" value="category">Добавить категорию</button>
    </form>

    <p>Добавить товар</p>
    <form class="product-add-form" id = "product-add-form" action="/admin/product" method="post">
        <select name="parent_category" id="product-parent-select">
            <option value="" selected>Без категории</option>
            <c:forEach items="${categoryMap.keySet()}" var="category">
                <option value="${category.getName()}">${category.getName()}</option>
            </c:forEach>
        </select>
        <select name="category" id="product-select">
            <option value="" selected>Без подкатегории</option>
        </select>
        Название <input type="text" name="name" value="" placeholder="Введите название">
        Описание <textarea name="description" placeholder="Введите описание"></textarea>
        <div>
        Характеристики <p id="feature-product-entity">
            <input id="feature-product" type="text" name="feature1" placeholder="Характеристика">
            <input id="feature-value-product" type="text" name="feature_value1" placeholder="Значение">
        </p>
        </div>
        <button type="button" onclick="addFeature()">Добавить поле</button>
        Цена <input type="text" name = "price" placeholder="Введите цену">
        Количество <input type="text" name = "count" placeholder="Введите количество">
        <button type="submit" name="add" value="product">Добавить товар</button>
    </form>

    <h3>Товары</h3>

    <c:forEach items="${productList}" var="product">

            <div class = "product-entity-photo-holder" style="background-image: url('../images/${product.getPathImage()}')"></div>
            <c:out value="Название: ${product.getName()}"/>
        <form action="/admin/product" method="get">
            <input type="hidden" name="product" value="${product.getId()}">
            <button type="submit" name="update" value="product">Изменить</button>
        </form>
        <form action="/admin/product" method="post">
            <input type="hidden" name="product_id" value="${product.getId()}">
            <button type="submit" name="remove" value="product">Удалить</button>
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
    var featureCount = 1;

    <c:if test="${requestScope.errorMessage != null}">
        $('.error-block').hide().show(1000).fadeOut(6000)
    </c:if>

    document.getElementById("product-parent-select").addEventListener("change", function () {
        var currentList = document.getElementById("product-select")
        var selectList = document.createElement("select");
        selectList.setAttribute("id", "product-select");
        selectList.setAttribute("name", "category");

        <c:forEach items="${categoryString.keySet()}" var="categoryParent">
            var categ = '${categoryParent}';

            if (this.value == categ){
                var option = document.createElement("option");
                option.value = "";
                option.text = "Без подкатегории";
                selectList.appendChild(option);
                <c:forEach items="${categoryString.get(categoryParent)}" var="categoryChild">
                    categ = '${categoryChild}';
                    option = document.createElement("option");
                    option.value = categ;
                    option.text = categ;
                    selectList.appendChild(option);
                </c:forEach>
            }
        </c:forEach>

        currentList.parentNode.replaceChild(selectList, currentList);

    })

    /*function addFeature(){
        var addElement = document.getElementById("feature-product-entity");
        if (featureCount < 10){
            featureCount++;
            var addElementClone = addElement.cloneNode(true);
            // addElementClone.childNodes[0].setAttribute("name", "feature" + featureCount);
            // addElementClone.childNodes[1].setAttribute("name", "feature_value" + featureCount);
            console.log(addElementClone.getElementById("feature-product"))
            addElementClone.childNodes[0].name( "feature" + featureCount);
            addElementClone.childNodes[1].name( "feature_value" + featureCount);
            addElement.parentNode.appendChild(addElementClone);
        }
    }*/

    function addFeature(){
        var addElement1 = document.getElementById("feature-product");
        var addElement2 = document.getElementById("feature-value-product");
        if (featureCount < 10){
            featureCount++;
            var fixDiv = document.createElement("div")
            var addElementClone1 = addElement1.cloneNode(true);
            var addElementClone2 = addElement2.cloneNode(true);
            addElementClone1.name = "feature" + featureCount;
            addElementClone2.name = "feature_value" + featureCount;
            addElement1.parentNode.appendChild(fixDiv);
            fixDiv.parentNode.appendChild(addElementClone1);
            fixDiv.parentNode.appendChild(addElementClone2);

        }
    }
</script>
</html>
