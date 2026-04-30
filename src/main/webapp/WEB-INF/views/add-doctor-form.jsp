<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%
	request.setAttribute("pageTitle", "Add Doctor Form");
%>

<jsp:include page="jsp/layout/header.jsp" />

<!-- Page Header -->
<div class="d-flex justify-content-between align-items-center mb-4">
	<h1 class="fw-bold">Add New Doctor:</h1>
	
	<a href="${pageContext.request.contextPath}/DoctorController?command=LIST"
		class="btn btn-primary">
		Back to List
	</a>
</div>

<!-- Add New Doctor Form Table Card -->
<div class="card shadow-sm">
	<div class="card-body">
	
		<!-- Optional error message -->
		<c:if test="${not empty errors}">
			<div class="alert alert-danger">
				Oops! Please correct the errors below.
			</div>
		</c:if>
		
		<form action="${pageContext.request.contextPath}/DoctorController" method="POST" class="needs-validation" novalidate>
			
			<!-- Hidden Form Field with command=ADD -->
			<input type="hidden" name="command" value="ADD" />
			
            <!-- First Name -->
            <div class="mb-3">
                <label for="firstName" class="form-label">First Name</label>
                <input type="text"
                       class="form-control ${errors.firstName != null ? 'is-invalid' : ''}"
                       id="firstName"
                       name="firstName"
                       value="${param.firstName}"
                       required />
                 
                 <c:if test="${errors.firstName != null}">
                 	<div class="invalid-feedback">
                 		${errors.firstName}
                 	</div>
                 </c:if>
            </div>

            <!-- Last Name -->
            <div class="mb-3">
                <label for="lastName" class="form-label">Last Name</label>
                <input type="text"
                       class="form-control ${errors.lastName != null ? 'is-invalid' : ''}"
                       id="lastName"
                       name="lastName"
                       value="${param.lastName}"
                       required />
                       
                 <c:if test="${errors.lastName != null}">
                 	<div class="invalid-feedback">
                 		${errors.lastName}
                 	</div>
                 </c:if>
            </div>

            <!-- Specialization -->
            <div class="mb-3">
                <label for="specialization" class="form-label">Specialization</label>
                <input type="text"
                       class="form-control ${errors.specialization != null ? 'is-invalid' : ''}"
                       id="specialization"
                       name="specialization"
                       value="${param.specialization}"
                       required />
                       
                 <c:if test="${errors.specialization != null}">
                 	<div class="invalid-feedback">
                 		${errors.specialization}
                 	</div>
                 </c:if>
            </div>

            <!-- Email -->
            <div class="mb-3">
                <label for="email" class="form-label">E-mail Address</label>
                <input type="email"
                       class="form-control ${errors.email != null ? 'is-invalid' : ''}"
                       id="email"
                       name="email"
                       value="${param.email}"
                       required />
                      
                 <c:if test="${errors.email != null}">
                 	<div class="invalid-feedback">
                 		${errors.email}
                 	</div>
                 </c:if>
            </div>

            <!-- Actions -->
            <div class="text-end">
                <button type="submit" class="btn btn-success">
                    Save Doctor
                </button>
                <a href="${pageContext.request.contextPath}/DoctorController?command=LIST"
                   class="btn btn-outline-secondary ms-2">
                    Cancel
                </a>
            </div>
			
			
		</form>
	</div>
</div>

<jsp:include page="jsp/layout/footer.jsp" />