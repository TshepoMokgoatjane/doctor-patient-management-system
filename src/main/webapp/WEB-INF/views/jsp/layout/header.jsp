<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
	<head>
		<title>${pageTitle}</title>
		
		<!-- Add Bootstrap CDN to JSP -->
		<link
		    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
		    rel="stylesheet"
		    crossorigin="anonymous">
		    
		<!-- Add Bootstrap Icons -->
		<link rel="stylesheet"
      		href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
      		
      	<style type="text/css">
      	th a {
      		color: inherit;
      		cursor: pointer;
      		text-decoration: none;
      	}</style>
		    
	</head>
		
	<body class="bg-light">
		
		<div class="container mt-4">
		
		<div class="mt-4 mb-4">
		    <div class="d-flex align-items-center">
		
				<c:if test="${not empty sessionScope.loggedInUser}">
		        <!-- Left: App Title -->
		        <h1 class="mb-1">
		            <a class="text-decoration-none text-dark"
		               href="${pageContext.request.contextPath}/DoctorController?command=LIST">Doctor Patience Management System
		            </a>
		        </h1>
		        </c:if>
		
		        <!-- Right: User info + Logout -->
		        <div class="ms-auto d-flex align-items-center gap-2">
		            <c:if test="${not empty sessionScope.loggedInUser}">
		                <span class="text-muted small">
		                    Logged in as
		                    <strong>${sessionScope.loggedInUser.username}</strong>
		                </span>
		
		                <a href="${pageContext.request.contextPath}/LogoutController"
		                   class="btn btn-outline-danger btn-sm">
		                    Logout
		                </a>
		            </c:if>
		        </div>
		    </div>
		</div>