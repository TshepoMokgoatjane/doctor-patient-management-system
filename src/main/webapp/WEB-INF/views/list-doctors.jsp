<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<%
		request.setAttribute("pageTitle", "List of Doctors");
	%>
	
	<jsp:include page="jsp/layout/header.jsp" />
		
		
			<!-- Page Header -->
			<div class="d-flex justify-content-between align-items-center mb-4">
				<h1 class="fw-bold">List of Doctors:</h1>
				
				<input 
						type="button" 
						value="Add Doctor"
						onclick="window.location.href='add-doctor-form.jsp'; return false;"
						class="btn btn-success"
					/>
			</div>
			
			<!-- Doctors Table Card -->
			<div class="card shadow-sm">
				<div class="card-body">
					<table class="table table-dark table-striped table-hover align-middle">
					
						<thead class="thead-dark">
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
								<c:when test="${not empty doctors}">						
									<c:forEach var="doctor" items="${doctors}">
									
										<c:url var="editLink" value="DoctorController">
											<c:param name="command" value="LOAD" />
											<c:param name="doctorId" value="${doctor.id}" />
										</c:url>
										
										<c:url var="deleteLink" value="DoctorController">
											<c:param name="command" value="DELETE" />
											<c:param name="doctorId" value="${doctor.id}" />
										</c:url>
									
										<tr>
											<td>${doctor.firstName}</td>
											<td>${doctor.lastName}</td>
											<td>${doctor.specialization}</td>
											<td>${doctor.email}</td>
											<td class="text-center">
												<a 
													href="${editLink}"
													class="btn btn-sm btn-primary">
													Edit
												</a>
												|
												<a href="${deleteLink}" 
													onclick="if (!(confirm('Are you sure you want to delete this Doctor from the list?'))) return false;"
													class="btn btn-sm btn-danger"
													>
												Delete
												</a>
											</td>
										</tr>				
									</c:forEach>
								</c:when>
								
								<c:otherwise>
									<tr>
										<td class="alert alert-warning text-center mb-0">
										Oh snap! No doctors found.
										</td>
									</tr>
								</c:otherwise>
								
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
			
	<jsp:include page="jsp/layout/footer.jsp" />	