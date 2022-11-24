<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавить фото</title>
</head>
<body>
    <form class = "photo-form" action="/save/image" method="post" enctype="multipart/form-data">
        <div class="photo-form">
                <input type="file" id = "file" name="file" size="60" accept=".jpg, .jpeg, .png">
            <button id = "file-submit" type="submit" name="action" value="load_photo">Добавить фото</button>
        </div>
    </form>
    <form class = "skip-form" action="/admin/product">
        <button type="submit">Пропустить</button>
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
