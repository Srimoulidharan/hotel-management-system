<%@ include file="partials/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section class="page-header">
    <div>
        <h1>Rooms</h1>
        <p>Add, update and delete hotel rooms</p>
    </div>
</section>

<section class="panel">
    <h2>Add Room</h2>
    <form class="form-grid" action="${pageContext.request.contextPath}/rooms" method="post">
        <input type="hidden" name="action" value="create">
        <div>
            <label>Room Number</label>
            <input type="text" name="roomNumber" placeholder="101" required>
        </div>
        <div>
            <label>Room Type</label>
            <select name="roomType" required>
                <option>Single</option>
                <option>Double</option>
                <option>Deluxe</option>
                <option>Suite</option>
            </select>
        </div>
        <div>
            <label>Price / Night</label>
            <input type="number" name="pricePerNight" min="1" step="0.01" placeholder="2500" required>
        </div>
        <div>
            <label>Status</label>
            <select name="status" required>
                <option value="AVAILABLE">AVAILABLE</option>
                <option value="OCCUPIED">OCCUPIED</option>
                <option value="MAINTENANCE">MAINTENANCE</option>
            </select>
        </div>
        <div class="full-width">
            <label>Description</label>
            <input type="text" name="description" placeholder="Room details">
        </div>
        <button class="btn primary" type="submit">Add Room</button>
    </form>
</section>

<section class="panel">
    <h2>Room List</h2>
    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>No.</th>
                <th>Type</th>
                <th>Price</th>
                <th>Status</th>
                <th>Description</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${rooms}" var="room">
                <tr>
                    <form action="${pageContext.request.contextPath}/rooms" method="post">
                        <input type="hidden" name="id" value="${room.id}">
                        <td><input class="table-input" name="roomNumber" value="${room.roomNumber}" required></td>
                        <td>
                            <select class="table-input" name="roomType">
                                <option ${room.roomType == 'Single' ? 'selected' : ''}>Single</option>
                                <option ${room.roomType == 'Double' ? 'selected' : ''}>Double</option>
                                <option ${room.roomType == 'Deluxe' ? 'selected' : ''}>Deluxe</option>
                                <option ${room.roomType == 'Suite' ? 'selected' : ''}>Suite</option>
                            </select>
                        </td>
                        <td><input class="table-input" type="number" step="0.01" name="pricePerNight" value="${room.pricePerNight}" required></td>
                        <td>
                            <select class="table-input" name="status">
                                <option value="AVAILABLE" ${room.status == 'AVAILABLE' ? 'selected' : ''}>AVAILABLE</option>
                                <option value="OCCUPIED" ${room.status == 'OCCUPIED' ? 'selected' : ''}>OCCUPIED</option>
                                <option value="MAINTENANCE" ${room.status == 'MAINTENANCE' ? 'selected' : ''}>MAINTENANCE</option>
                            </select>
                        </td>
                        <td><input class="table-input" name="description" value="${room.description}"></td>
                        <td class="actions">
                            <button class="btn small" type="submit" name="action" value="update">Save</button>
                            <button class="btn small danger" type="submit" name="action" value="delete" onclick="return confirm('Delete this room?')">Delete</button>
                        </td>
                    </form>
                </tr>
            </c:forEach>
            <c:if test="${empty rooms}">
                <tr><td colspan="6" class="empty">No rooms found.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>
<%@ include file="partials/footer.jsp" %>
