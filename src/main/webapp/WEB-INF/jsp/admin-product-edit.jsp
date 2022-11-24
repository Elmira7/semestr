<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>${requestScope.product.getName()}</title>
</head>
<body>
<a href="/admin/product">Назад</a>
<p>Редактировать товар</p>
    <form class="product-add-form" id = "product-add-form" action="/admin/product" method="post">
        <select name="parent_category" id="product-parent-select">
            <option value="" selected>Без категории</option>
            <c:forEach items="${categoryMap.keySet()}" var="category">
                <option value="${category.getName()}" <c:if test="${requestScope.product.getCategory().getParentName() == category.getName()}"> selected </c:if>>${category.getName()}</option>
            </c:forEach>
        </select>
        <select name="category" id="product-select">
            <option value="">Без подкатегории</option>
            <option value="${requestScope.product.getCategory().getName()}" selected>${requestScope.product.getCategory().getName()}</option>
        </select>
        Название <input type="text" name="name" value="${requestScope.product.getName()}" placeholder="Введите название">
        Описание <textarea name="description" placeholder="Введите описание">${requestScope.product.getDescription()}</textarea>
        <div>
            Характеристики <p id="feature-product-entity">
            <c:forEach items="${requestScope.product.getFeature()}" var="feature">
                <input id="feature-product" type="text" value="${feature.getKey()}" placeholder="Характеристика" onload="setFeatureName(this)">
                <input id="feature-value-product" type="text" value="${feature.getValue()}" onload="setValueName(this)" placeholder="Значение">

            </c:forEach>
        </p>
        </div>
        <button type="button" onclick="addFeature()">Добавить поле</button>
        Цена <input type="text" name = "price" value="${requestScope.product.getPrice()}" placeholder="Введите цену">
        Количество <input type="text" name = "count" value="${requestScope.product.getCount()}" placeholder="Введите количество">
        <input type="hidden" name="product_id" value="${requestScope.product.getId()}">
        <button type="submit" name="update" value="product">Изменить товар</button>
    </form>
</body>
<script>

    let featureCount = 1;

    document.getElementById("product-parent-select").addEventListener("change", function () {
        let currentList = document.getElementById("product-select")
        let selectList = document.createElement("select");
        selectList.setAttribute("id", "product-select");
        selectList.setAttribute("name", "category");

        <c:forEach items="${categoryString.keySet()}" var="categoryParent">
        let categ = '${categoryParent}';

        if (this.value == categ){
            let option = document.createElement("option");
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

    function addFeature(){
        let addElement = document.getElementById("feature-product-entity");
        if (featureCount < 10){
            featureCount++;
            let addElementClone = addElement.cloneNode(true);
            // addElementClone.childNodes[0].setAttribute("name", "feature" + featureCount);
            // addElementClone.childNodes[1].setAttribute("name", "feature_value" + featureCount);
            console.log(addElementClone.getElementById("feature-product"))
            addElementClone.childNodes[0].name( "feature" + featureCount);
            addElementClone.childNodes[1].name( "feature_value" + featureCount);
            addElement.parentNode.appendChild(addElementClone);
        }
    }

    function addFeature(){
        let addElement1 = document.getElementById("feature-product");
        let addElement2 = document.getElementById("feature-value-product");
        if (featureCount < 10){
            featureCount++;
            let fixDiv = document.createElement("div")
            let addElementClone1 = addElement1.cloneNode(true);
            let addElementClone2 = addElement2.cloneNode(true);
            addElementClone1.name = "feature" + featureCount;
            addElementClone2.name = "feature_value" + featureCount;
            addElement1.parentNode.appendChild(fixDiv);
            fixDiv.parentNode.appendChild(addElementClone1);
            fixDiv.parentNode.appendChild(addElementClone2);

        }
    }

    function setFeatureName(e) {
        e.name = "feature" + featureCount;
    }
    function setValueName(e) {
        e.name = "feature_value" + featureCount;
        featureCount++;
    }
</script>

</html>
