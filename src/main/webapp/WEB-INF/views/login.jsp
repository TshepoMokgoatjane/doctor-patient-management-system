<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	request.setAttribute("pageTitle", "Login");
%>

<jsp:include page="jsp/layout/header.jsp" />


<div class="container">

	<!-- App Title -->
	<div class="text-center mb-4">
		<h2>Doctor Patient Management System</h2>
		<div class="text-muted">Please sign in to continue</div>
	</div>
	
	<div class="row justify-content-center">
		<div class="col-md-4">
		
			<!--  Logout Success Message -->
			<c:if test="${param.logout == 'true'}">
				<div class="alert alert-success text-center">
					You have been logged out successfully.
				</div>
			</c:if>
			
			<!-- Login Card -->
			<div class="card shadow-sm">
				<div class="card-body p-4">
				
					<h4 class="text-center mb-4 fw-semibold">Sign in</h4>
					
					<form action="LoginController" method="post">
					
						<c:if test="${not empty error}">
							<div class="alert alert-danger text-center">
								${error}
							</div>
						</c:if>
						
						<div class="mb-3">
							<input class="form-control" name="username" type="text" placeholder="Username" required />
						</div>
						
						<div class="mb-3">
							<input class="form-control" name="password" type="password" placeholder="Password" required />
						</div>
						
						<button class="btn btn-primary w-100">Login</button>					
					</form>
				</div>			
			</div>
		</div>
	</div>	
</div>

<jsp:include page="jsp/layout/footer.jsp" />