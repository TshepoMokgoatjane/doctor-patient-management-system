<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%
	request.setAttribute("pageTitle", "System Info");
%>

<jsp:include page="/WEB-INF/views/jsp/layout/header.jsp" />

<!-- Page Header -->
<div class="d-flex justify-content-between align-items-center mb-4">
	<h3 class="fw-bold">System Information</h3>
	
	<a href="${pageContext.request.contextPath}/DoctorController?command=ADMIN_DASHBOARD"
		class="btn btn-primary">
		Back to Dashboard
	</a>
</div>

<!-- Statistics Cards -->
<div class="row g-4 mb-4">
	<div class="col-md-4">
		<div class="card shadow-sm border-start border-4 border-success">
			<div class="card-body">
				<div class="d-flex align-items-center">
					<div>
						<h6 class="text-muted mb-1">Active Doctors</h6>
						<h2 class="fw-bold mb-0">${activeDoctors}</h2>
					</div>
					<i class="bi bi-person-check ms-auto text-success" style="font-size: 2.5rem;"></i>
				</div>
			</div>
		</div>
	</div>
	
	<div class="col-md-4">
		<div class="card shadow-sm border-start border-4 border-danger">
			<div class="card-body">
				<div class="d-flex align-items-center">
					<div>
						<h6 class="text-muted mb-1">Deleted Doctors</h6>
						<h2 class="fw-bold mb-0">${deletedDoctors}</h2>
					</div>
					<i class="bi bi-person-x ms-auto text-danger" style="font-size: 2.5rem;"></i>
				</div>
			</div>
		</div>
	</div>
	
	<div class="col-md-4">
		<div class="card shadow-sm border-start border-4 border-primary">
			<div class="card-body">
				<div class="d-flex align-items-center">
					<div>
						<h6 class="text-muted mb-1">Registered Users</h6>
						<h2 class="fw-bold mb-0">${totalUsers}</h2>
					</div>
					<i class="bi bi-people ms-auto text-primary" style="font-size: 2.5rem;"></i>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- User Management Table -->
<div class="card shadow-sm mb-4">
	<div class="card-header bg-dark text-white">
		<h5 class="mb-0"><i class="bi bi-people-fill"></i> User Management</h5>
	</div>
	<div class="card-body">
		<table class="table table-striped table-hover align-middle">
			<thead>
				<tr>
					<th>ID</th>
					<th>Username</th>
					<th>Role</th>
					<th>Status</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="user" items="${users}">
					<tr>
						<td>${user.id}</td>
						<td><c:out value="${user.username}" /></td>
						<td>
							<c:choose>
								<c:when test="${user.admin}">
									<span class="badge bg-danger">ADMIN</span>
								</c:when>
								<c:otherwise>
									<span class="badge bg-secondary">USER</span>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<span class="badge bg-success">Active</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<!-- Application Info -->
<div class="card shadow-sm">
	<div class="card-header bg-dark text-white">
		<h5 class="mb-0"><i class="bi bi-gear-fill"></i> Application Environment</h5>
	</div>
	<div class="card-body">
		<table class="table table-bordered mb-0">
			<tbody>
				<tr>
					<th class="w-25 bg-light">Server</th>
					<td><c:out value="${serverInfo}" /></td>
				</tr>
				<tr>
					<th class="bg-light">Java Version</th>
					<td><c:out value="${javaVersion}" /></td>
				</tr>
				<tr>
					<th class="bg-light">Operating System</th>
					<td><c:out value="${osName}" /> (<c:out value="${osArch}" />)</td>
				</tr>
				<tr>
					<th class="bg-light">JVM Memory (Used / Max)</th>
					<td>${jvmMemoryUsed} MB / ${jvmMemoryMax} MB</td>
				</tr>
				<tr>
					<th class="bg-light">Database</th>
					<td>MySQL 8</td>
				</tr>
				<tr>
					<th class="bg-light">Build Tool</th>
					<td>Apache Maven</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>

<jsp:include page="/WEB-INF/views/jsp/layout/footer.jsp" />
