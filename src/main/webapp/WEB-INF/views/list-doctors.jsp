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
												<a href="${editLink}"
													class="btn btn-sm btn-primary me-1">
													<i class="bi bi-pencil-square"></i> Edit
												</a> 
												<a href="#"													
													class="btn btn-sm btn-danger"
													data-bs-toggle="modal"
													data-bs-target="#deleteDoctorModal"
													data-doctor-id="${doctor.id}"
													data-doctor-name="${doctor.firstName} ${doctor.lastName}"
													>
													<i class="bi bi-trash"></i> Delete
												</a>
											</td>
										</tr>				
									</c:forEach>
								</c:when>
								
								<c:otherwise>
									<tr>
										<td colspan="5" class="text-center">
											<div class="alert alert-warning mb-0">
												Oh snap! No doctors found.
											</div>
										</td>
									</tr>
								</c:otherwise>
								
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
			
			<!--  Add Static Pagination -->
			<div class="row">
				<div class="col">
					<nav class="mt-4 shadow-sm p-2 bg-white rounded">
					    <ul class="pagination pagination-sm justify-content-center">
					
					        <li class="page-item disabled">
					            <a class="page-link">Previous</a>
					        </li>
					
					        <li class="page-item active">
					            <a class="page-link" href="DoctorController?command=LIST&page=1">1</a>
					        </li>
					
					        <li class="page-item">
					            <a class="page-link" href="DoctorController?command=LIST&page=2">2</a>
					        </li>
					
					        <li class="page-item">
					            <a class="page-link" href="DoctorController?command=LIST&page=3">3</a>
					        </li>
					
					        <li class="page-item">
					            <a class="page-link" href="DoctorController?command=LIST&page=2">
					                Next
					            </a>
					        </li>
					
					    </ul>
					</nav>
				</div>
			</div>
						
			
			<!-- Add Bootstrap Modal - Delete Confirmation Modal -->
			<div class="modal fade" id="deleteDoctorModal" tabindex="-1"
			     aria-labelledby="deleteDoctorModalLabel" aria-hidden="true">
			    
			    <div class="modal-dialog modal-dialog-centered">
			        <div class="modal-content">
			
			            <div class="modal-header bg-danger text-white">
			                <h5 class="modal-title" id="deleteDoctorModalLabel">
			                    Confirm Deletion
			                </h5>
			                <button type="button" class="btn-close btn-close-white"
			                        data-bs-dismiss="modal" aria-label="Close"></button>
			            </div>
			
			            <div class="modal-body">
			                Are you sure you want to delete
			                <strong id="doctorName"></strong>?
			                <br />
			                This action cannot be undone.
			            </div>
			
			            <div class="modal-footer">
			                <button type="button" class="btn btn-secondary"
			                        data-bs-dismiss="modal">
			                    Cancel
			                </button>
			
			                <a id="confirmDeleteBtn" class="btn btn-danger">
			                    Delete
			                </a>
			            </div>
			
			        </div>
			    </div>
			
	<jsp:include page="jsp/layout/footer.jsp" />	
			</div>
			
			<script>
			    const deleteDoctorModal = document.getElementById('deleteDoctorModal');
			
			    deleteDoctorModal.addEventListener('show.bs.modal', function (event) {
			        const button = event.relatedTarget;
			
			        const doctorId = button.getAttribute('data-doctor-id');
			        const doctorName = button.getAttribute('data-doctor-name');
			
			        const doctorNameSpan = deleteDoctorModal.querySelector('#doctorName');
			        const confirmDeleteBtn = deleteDoctorModal.querySelector('#confirmDeleteBtn');
			
			        doctorNameSpan.textContent = doctorName;
			        confirmDeleteBtn.href ='DoctorController?command=DELETE&doctorId=' + doctorId;
			    });
			</script>
