<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<%
		request.setAttribute("pageTitle", "List of Doctors");
	%>
	
	<jsp:include page="jsp/layout/header.jsp" />
		
			<!-- Page Header -->
			<div class="d-flex justify-content-between align-items-center mb-4">
				<h3 class="fw-bold">List of Doctors:</h3>
				
				<c:if test="${sessionScope.loggedInUser.admin}">
					<a href="${pageContext.request.contextPath}/DoctorController?command=SHOW_ADD_DOCTOR_FORM" class="btn btn-success">
						Add Doctor
					</a>
				</c:if>
			</div>
			
			<!-- Search Feature Form -->
			<form method="GET" action="${pageContext.request.contextPath}/DoctorController" class="row g-2 mb-3">
				
				<input type="hidden" name="command" value="LIST" />
				<input type="hidden" name="sortField" value="${sortField}" />
				<input type="hidden" name="sortDir" value="${sortDir}" />
				
				<div class="row justify-content-center mb-4">
					<div class="col-md-8 ">
					
						<div class="input-group">
							
							<!-- Search button -->
					        <button class="btn btn-warning" type="submit">
					            Search
					        </button>
					
					        <!-- Search input -->
					        <input type="text"
					               name="searchTerm"
					               class="form-control"
					               placeholder="Search by name, email, or specialization..."
					               value="${param.searchTerm}" />
					
					        <!-- Clear button -->
					        <a href="${pageContext.request.contextPath}/DoctorController?command=LIST"
					           class="btn btn-outline-secondary">
					            Clear
					        </a>						
						</div>
					</div>
				</div>	
			</form>
			
			<!-- Success Message - ADDED -->
			<c:if test="${param.success == 'added'}">
				<div id="successAlert"
					class="alert alert-success alert-dismissible fade show"
					role="alert">
					<strong>Success!</strong> Doctor added successfully.
					
					<button type="button" 
						class="btn-close"
						data-bs-dismiss="alert"
						aria-label="Close"></button>
				</div>
			</c:if>
			<!-- Success Message - DELETED -->
			<c:if test="${param.success == 'deleted'}">
				<div id="deleteAlert"
					class="alert alert-danger alert-dismissible fade show">
					Doctor deleted successfully.
				</div>
			</c:if>
						
			<!-- Success Message - Updated -->
			<c:if test="${param.success == 'updated'}">
				<div id="updatedAlert" class="alert alert-success fade show">
					Doctor's record updated successfully.
				</div>
			</c:if>
			
			<!-- Doctors Table Card -->
			<div class="card shadow-sm">
				<div class="card-body">
					<table class="table table-dark table-striped table-hover align-middle">
					
						<thead class="table-dark">
							<tr>
								<th>
									<a href="${pageContext.request.contextPath}/DoctorController?command=LIST&page=${currentPage}&sortField=firstName&sortDir=${reverseSortDir}">
									
										First Name				
										
										<c:if test="${sortField == 'firstName'}">
											<i class="bi ${sortDir == 'asc' ? 'bi-chevron-up' : 'bi-chevron-down'}"></i>
										</c:if>					
									</a>
								</th>
								
								<th>
									<a href="${pageContext.request.contextPath}/DoctorController?command=LIST&page=${currentPage}&sortField=lastName&sortDir=${reverseSortDir}">
									
										Last Name
										
										<c:if test="${sortField == 'lastName'}">
											<i class="bi ${sortDir == 'asc' ? 'bi-chevron-up' : 'bi-chevron-down'}"></i>
										</c:if>	
									</a>
								</th>
								
								<th>
									<a href="${pageContext.request.contextPath}/DoctorController?command=LIST&page=${currentPage}&sortField=specialization&sortDir=${reverseSortDir}">
									
										Specialization
										
										<c:if test="${sortField == 'specialization'}">
											<i class="bi ${sortDir == 'asc' ? 'bi-chevron-up' : 'bi-chevron-down'}"></i>
										</c:if>	
									</a>
								</th>
								
								<th>
									<a href="${pageContext.request.contextPath}/DoctorController?command=LIST&page=${currentPage}&sortField=email&sortDir=${reverseSortDir}">
									
										Email
										
										<c:if test="${sortField == 'email'}">
											<i class="bi ${sortDir == 'asc' ? 'bi-chevron-up' : 'bi-chevron-down'}"></i>
										</c:if>	
									</a>
								</th>
								
								<c:if test="${sessionScope.loggedInUser.admin}">
									<th class="text-center">Action</th>
								</c:if>
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
											
											<c:if test="${sessionScope.loggedInUser.admin}">
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
														data-page="${currentPage}"
														data-sort-field="${sortField}"
														data-sort-dir="${sortDir}"
														>
														<i class="bi bi-trash"></i> Delete
													</a> 
												</td>												
											</c:if>
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
			
			<!--  Add Dynamic Pagination -->			
			<c:if test="${not empty doctors}">
				<c:if test="${totalPages > 1}">
					<div class="row">
						<div class="col">
							<nav class="mt-4 shadow-sm p-2 bg-white rounded">
							    <ul class="pagination pagination-sm justify-content-center">
							
									<!-- Previous -->
							        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
							            <a class="page-link"
							            	href="${pageContext.request.contextPath}/DoctorController?command=LIST&page=${currentPage - 1}">
							            	Previous
							            </a>
							        </li>
							
							        <!-- Page Numbers -->
							        <c:forEach begin="1" end="${totalPages}" var="i">  	    
							        
								        <c:url var="pageUrl" value="DoctorController">
								        	<c:param name="command" value="LIST" />
								        	<c:param name="page" value="${i}" />
								        	<c:param name="sortField" value="${sortField}" />
								        	<c:param name="sortDir" value="${sortDir}" />							        
								        </c:url>							        
							        
							        	<li class="page-item ${i == currentPage ? 'active' : '' }">
							        		<a class="page-link" href="${pageUrl}">
							        			${i}
							        		</a>
							        	</li>
							        </c:forEach>
							
									<!--  Next -->
							        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
							            <a class="page-link" href="DoctorController?command=LIST&page=${currentPage + 1}">
							                Next
							            </a>
							        </li>
							
							    </ul>
							</nav>
						</div>
					</div>
				</c:if>
			</c:if>
						
			
			
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
			        const page = button.getAttribute('data-page');
			        const sortField = button.getAttribute('data-sort-field');
			        const sortDir = button.getAttribute('data-sort-dir');
			
			        const doctorNameSpan = deleteDoctorModal.querySelector('#doctorName');
			        const confirmDeleteBtn = deleteDoctorModal.querySelector('#confirmDeleteBtn');
			
			        doctorNameSpan.textContent = doctorName;
			        
			        confirmDeleteBtn.href ='DoctorController?command=DELETE&doctorId=' + doctorId + 
			        		'&page=' + page + '&sortField=' + sortField + '&sortDir=' + sortDir;
			    });
			</script>
			
			<script>
				setTimeout(
					() => {
						const alert = document.getElementById('successAlert');
						if (alert) {
							const bsAlert = new bootstrap.Alert(alert);
							bsAlert.close();
						}
					}, 5000	// 5 seconds	
				);
				setTimeout(
						() => {
							const alert = document.getElementById('deleteAlert');
							if (alert) {
								const bsAlert = new bootstrap.Alert(alert);
								bsAlert.close();
							}
						}, 5000	// 5 seconds	
					);
				setTimeout(
						() => {
							const alert = document.getElementById('updatedAlert');
							if (alert) {
								const bsAlert = new bootstrap.Alert(alert);
								bsAlert.close();
							}
						}, 5000	// 5 seconds	
					);
			</script>
