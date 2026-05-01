<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	request.setAttribute("pageTitle", "Admin Dashboard");
%>

<jsp:include page="/WEB-INF/views/jsp/layout/header.jsp" />

<div class="container">
	
	<h2 class="mb-4">Admin Dashboard:</h2>
	
	<div class="row g-4">
	
		<!-- Manage Doctors -->
		<div class="col-md-4">
			<div class="card h-100 shadow-sm">
				<div class="card-body">
					<h5 class="card-title">Manage Doctors</h5>
					<p class="card-text">
						Add, edit, or remove doctor records.
					</p>
					
					<a href="${pageContext.request.contextPath}/DoctorController?command=LIST">
						Go to Doctors
					</a>
				</div>
			</div>
		</div>
		
		<!-- Deleted Doctors -->
		<div class="col-md-4">
			<div class="card h-100 shadow-sm">
				<div class="card-body">
					<h5 class="card-title">Deleted Doctors</h5>
					<p class="card-text">
						View and restore soft-delete doctor records.
					</p>
					
					<a href="${pageContext.request.contextPath}/DoctorController?command=VIEW_DELETED">
						View Deleted
					</a>
				</div>
			</div>
		</div>
		
		<!-- Future expansion -->
		<div class="col-md-4">
			<div class="card h-100 shadow-sm">
				<div class="card-body">
					<h5 class="card-title">System Info</h5>
					<p class="card-text">
						Admin-only system or audit information.
					</p>
					<button class="btn btn-secondary">Coming Soon</button>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/jsp/layout/footer.jsp" />