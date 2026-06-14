<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Hotel Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="login-page">
    <div class="login-card">
        <div class="login-logo">H</div>
        <h1>Hotel Management System</h1>
        <p>Login to continue</p>
        <c:if test="${not empty error}">
            <div class="alert error">${error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <label>Username</label>
            <input type="text" name="username" value="admin" required>

            <label>Password</label>
            <input type="password" name="password" value="admin123" required>

            <button type="submit" class="btn primary full">Login</button>
        </form>
        <div class="demo-login">
            Demo: <strong>admin</strong> / <strong>admin123</strong>
        </div>
    </div>
</body>
</html>
