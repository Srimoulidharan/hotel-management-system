<%@ include file="partials/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section class="page-header">
    <div>
        <h1>Bookings</h1>
        <p>Reserve, check in, check out and cancel bookings</p>
    </div>
</section>

<section class="panel">
    <h2>Create Booking</h2>

    <c:choose>
        <c:when test="${empty guests}">
            <div class="alert error">Add at least one guest before creating a booking.</div>
        </c:when>

        <c:when test="${empty rooms}">
            <div class="alert error">No bookable rooms found. Add an available room first.</div>
        </c:when>

        <c:otherwise>
            <form class="form-grid" action="${pageContext.request.contextPath}/bookings" method="post">
                <input type="hidden" name="action" value="create">

                <div>
                    <label>Guest</label>
                    <select name="guestId" required>
                        <c:forEach items="${guests}" var="guest">
                            <option value="${guest.id}">
                                ${guest.fullName} - ${guest.phone}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label>Room</label>
                    <select name="roomId" required>
                        <c:forEach items="${rooms}" var="room">
                            <option value="${room.id}">
                                ${room.roomNumber} - ${room.roomType} - &#8377;${room.pricePerNight}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label>Check-in Date</label>
                    <input type="date" name="checkIn" required>
                </div>

                <div>
                    <label>Check-out Date</label>
                    <input type="date" name="checkOut" required>
                </div>

                <button class="btn primary" type="submit">Create Booking</button>
            </form>
        </c:otherwise>
    </c:choose>
</section>

<section class="panel">
    <h2>Booking List</h2>

    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Guest</th>
                <th>Room</th>
                <th>Check-in</th>
                <th>Check-out</th>
                <th>Total</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach items="${bookings}" var="booking">
                <tr>
                    <td>#${booking.id}</td>

                    <td>
                        ${booking.guestName}
                        <br>
                        <small>${booking.guestPhone}</small>
                    </td>

                    <td>
                        ${booking.roomNumber}
                        <br>
                        <small>${booking.roomType}</small>
                    </td>

                    <td>${booking.checkIn}</td>
                    <td>${booking.checkOut}</td>
                    <td>&#8377;${booking.totalAmount}</td>

                    <td>
                        <span class="status-pill ${booking.status}">
                            ${booking.status}
                        </span>
                    </td>

                    <td class="actions stacked">
                        <c:if test="${booking.status == 'RESERVED'}">
                            <form action="${pageContext.request.contextPath}/bookings" method="post">
                                <input type="hidden" name="id" value="${booking.id}">

                                <button class="btn small" type="submit" name="action" value="checkin">
                                    Check In
                                </button>

                                <button class="btn small warning" type="submit" name="action" value="cancel">
                                    Cancel
                                </button>
                            </form>
                        </c:if>

                        <c:if test="${booking.status == 'CHECKED_IN'}">
                            <form action="${pageContext.request.contextPath}/bookings" method="post">
                                <input type="hidden" name="id" value="${booking.id}">

                                <button class="btn small" type="submit" name="action" value="checkout">
                                    Check Out
                                </button>
                            </form>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/bookings" method="post">
                            <input type="hidden" name="id" value="${booking.id}">

                            <button class="btn small danger"
                                    type="submit"
                                    name="action"
                                    value="delete"
                                    onclick="return confirm('Delete this booking?')">
                                Delete
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty bookings}">
                <tr>
                    <td colspan="8" class="empty">No bookings found.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>

<%@ include file="partials/footer.jsp" %>