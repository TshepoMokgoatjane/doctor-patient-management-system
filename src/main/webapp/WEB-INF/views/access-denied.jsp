<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%
	request.setAttribute("pageTitle", "Access Denied");
%>
	
	
<jsp:include page="jsp/layout/header.jsp" />

<!-- Page Header -->
<div class="d-flex justify-content-between align-items-center mb-4">

	<h1 class="fw-bold">Oops! Access Denied:</h1>
	
	<a href="${pageContext.request.contextPath}/DoctorController?command=LIST">
		Return to Home
	</a>
</div>

<jsp:include page="jsp/layout/footer.jsp" />