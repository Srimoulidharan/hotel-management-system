<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>

<body>

<button class="sidebar-toggle" id="sidebarToggle" type="button">☰</button>

<div class="app-shell" id="appShell">

    <aside class="sidebar">
        <div class="brand">
            <div class="brand-icon">H</div>
            <div>
                <strong>Hotel HMS</strong>
                <span>Admin Panel</span>
            </div>
        </div>

        <nav>
            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/rooms">Rooms</a>
            <a href="${pageContext.request.contextPath}/guests">Guests</a>
            <a href="${pageContext.request.contextPath}/bookings">Bookings</a>
        </nav>

        <div class="sidebar-footer">
            <span>Logged in as ${sessionScope.adminUser.fullName}</span>
            <a class="logout" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </aside>

    <main class="main-content">

        <c:if test="${not empty param.success}">
            <div class="alert success">${param.success}</div>
        </c:if>

        <c:if test="${not empty param.error}">
            <div class="alert error">${param.error}</div>
        </c:if>