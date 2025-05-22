<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><c:out value="${pageTitle}"
		default="Wistoria - Premium Watch Collection" /></title>
<!-- External CSS -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<!-- Base CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user-base.css">
<!-- Component CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/messageModal.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/header.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/newsletter.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/footer.css">
<!-- Page-specific CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/home.css">
</head>
<body>
	<div class="template-container">
		<!-- Header -->
		<%@ include file="/WEB-INF/views/common/header.jsp"%>

		<!-- Message Modal: Displays error/success messages -->
		<%@ include file="/WEB-INF/views/common/messageModal.jsp"%>

		<!-- Main Content -->
		<main class="main-content">
			<jsp:include
				page="${not empty pageContent ? pageContent : '/views/user/home.jsp'}" />
		</main>

		<!-- Newsletter -->
		<%@ include file="/WEB-INF/views/common/newsletter.jsp"%>

		<!-- Footer -->
		<%@ include file="/WEB-INF/views/common/footer.jsp"%>
	</div>

	<!-- JavaScript -->
	<script src="${pageContext.request.contextPath}/assets/js/messageModal.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/header.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/home.js"></script>
</body>
</html>