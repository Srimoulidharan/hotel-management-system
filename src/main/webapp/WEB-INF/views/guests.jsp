<%@ include file="partials/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section class="page-header">
    <div>
        <h1>Guests</h1>
        <p>Maintain guest information</p>
    </div>
</section>

<section class="panel">
    <h2>Add Guest</h2>
    <form class="form-grid" action="${pageContext.request.contextPath}/guests" method="post">
        <input type="hidden" name="action" value="create">
        <div>
            <label>Full Name</label>
            <input type="text" name="fullName" required>
        </div>
        <div>
            <label>Email</label>
            <input type="email" name="email">
        </div>
        <div>
            <label>Phone</label>
            <input type="text" name="phone" required>
        </div>
        <div>
            <label>ID Proof</label>
            <input type="text" name="idProof" placeholder="Aadhaar / Passport / Driving License">
        </div>
        <div class="full-width">
            <label>Address</label>
            <input type="text" name="address">
        </div>
        <button class="btn primary" type="submit">Add Guest</button>
    </form>
</section>

<section class="panel">
    <h2>Guest List</h2>
    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>ID Proof</th>
                <th>Address</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${guests}" var="guest">
                <tr>
                    <form action="${pageContext.request.contextPath}/guests" method="post">
                        <input type="hidden" name="id" value="${guest.id}">
                        <td><input class="table-input" name="fullName" value="${guest.fullName}" required></td>
                        <td><input class="table-input" type="email" name="email" value="${guest.email}"></td>
                        <td><input class="table-input" name="phone" value="${guest.phone}" required></td>
                        <td><input class="table-input" name="idProof" value="${guest.idProof}"></td>
                        <td><input class="table-input" name="address" value="${guest.address}"></td>
                        <td class="actions">
                            <button class="btn small" type="submit" name="action" value="update">Save</button>
                            <button class="btn small danger" type="submit" name="action" value="delete" onclick="return confirm('Delete this guest?')">Delete</button>
                        </td>
                    </form>
                </tr>
            </c:forEach>
            <c:if test="${empty guests}">
                <tr><td colspan="6" class="empty">No guests found.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>
<%@ include file="partials/footer.jsp" %>
