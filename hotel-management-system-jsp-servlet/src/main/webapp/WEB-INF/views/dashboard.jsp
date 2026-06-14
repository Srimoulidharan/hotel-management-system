<%@ include file="partials/header.jsp" %>
<section class="page-header">
    <div>
        <h1>Dashboard</h1>
        <p>Overview of hotel operations</p>
    </div>
</section>

<section class="stats-grid">
    <div class="stat-card">
        <span>Total Rooms</span>
        <strong>${totalRooms}</strong>
    </div>
    <div class="stat-card">
        <span>Available Rooms</span>
        <strong>${availableRooms}</strong>
    </div>
    <div class="stat-card">
        <span>Total Guests</span>
        <strong>${totalGuests}</strong>
    </div>
    <div class="stat-card">
        <span>Active Bookings</span>
        <strong>${activeBookings}</strong>
    </div>
    <div class="stat-card revenue">
        <span>Total Revenue</span>
        <strong>&#8377; ${totalRevenue}</strong>    
    </div>
</section>

<section class="quick-actions">
    <h2>Quick Actions</h2>
    <div class="actions-grid">
        <a class="action-card" href="${pageContext.request.contextPath}/rooms">Manage Rooms</a>
        <a class="action-card" href="${pageContext.request.contextPath}/guests">Manage Guests</a>
        <a class="action-card" href="${pageContext.request.contextPath}/bookings">Create Booking</a>
    </div>
</section>
<%@ include file="partials/footer.jsp" %>
