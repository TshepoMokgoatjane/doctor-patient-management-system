<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%
	request.setAttribute("pageTitle", "Deleted Doctors");
%>

<jsp:include page="/WEB-INF/views/jsp/layout/header.jsp" />

<!-- Page Header -->
<div class="d-flex justify-content-between align-items-center mb-4">
	<h3 class="fw-bold">Deleted Doctors:</h3>
	
	<a href="${pageContext.request.contextPath}/DoctorController?command=ADMIN_DASHBOARD"
		class="btn btn-primary">
		Back to Dashboard
	</a>
</div>

<!-- Success Message - RESTORED -->
<c:if test="${param.success == 'restored'}">
	<div id="restoredAlert" class="alert alert-success alert-dismissible fade show" role="alert">
		<strong>Success!</strong> Doctor restored successfully.
		<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
	</div>
</c:if>

<!-- Deleted Doctors Table -->
<div class="card shadow-sm">
	<div class="card-body">
		<table class="table table-dark table-striped table-hover align-middle">
			<thead class="table-dark">
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Specialization</th>
					<th>Email</th>
					<th class="text-center">Action</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty deletedDoctors}">
						<c:forEach var="doctor" items="${deletedDoctors}">
							<tr>
								<td><c:out value="${doctor.firstName}" /></td>
								<td><c:out value="${doctor.lastName}" /></td>
								<td><c:out value="${doctor.specialization}" /></td>
								<td><c:out value="${doctor.email}" /></td>
								<td class="text-center">
									<a href="${pageContext.request.contextPath}/DoctorController?command=RESTORE&doctorId=${doctor.id}"
										class="btn btn-sm btn-success">
										<i class="bi bi-arrow-counterclockwise"></i> Restore
									</a>
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="5" class="text-center">
								<div class="alert alert-info mb-0">
									No deleted doctors found.
								</div>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</div>

<script>
	setTimeout(() => {
		const alert = document.getElementById('restoredAlert');
		if (alert) {
			const bsAlert = new bootstrap.Alert(alert);
			bsAlert.close();
		}
	}, 5000);
</script>

<jsp:include page="/WEB-INF/views/jsp/layout/footer.jsp" />
