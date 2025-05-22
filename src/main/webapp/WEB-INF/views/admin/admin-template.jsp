<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${pageTitle}</title>
<!-- Font Awesome -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<!-- Google Fonts for Inter -->
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap"
	rel="stylesheet">
<!-- Components CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/messageModal.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/sidebar.css">
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	font-family: 'Inter', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	line-height: 1.6;
	color: #1f2a44;
	background-color: #f5f5f7;
}

.template-container {
	display: flex;
	min-height: 100vh;
}

.template-main-content {
	flex: 1;
	margin-left: 250px;
	padding: 1.25rem;
	transition: all 0.3s ease;
	min-height: 100vh;
	width: calc(100% - 250px);
}

/* Responsive styles */
@media ( max-width : 992px) {
	.template-main-content {
		margin-left: 60px;
		width: calc(100% - 60px);
	}
	body.sidebar-collapsed .template-main-content {
		margin-left: 60px;
		width: calc(100% - 60px);
	}
}

@media ( max-width : 480px) {
	.template-main-content {
		margin-left: 0;
		margin-bottom: 60px;
		width: 100%;
	}
	body.sidebar-collapsed .template-main-content {
		margin-left: 0;
		margin-bottom: 60px;
		width: 100%;
	}
}
</style>
</head>
<body>
	<!-- Message Modal: Displays error/success messages -->
	<%@ include file="/WEB-INF/views/common/messageModal.jsp"%>
	<div class="template-container">
		<jsp:include page="/WEB-INF/views/admin/sidebar.jsp" />
		<div class="template-main-content">
			<jsp:include page="${pageContent}" />
		</div>
	</div>
	<!-- JavaScript -->
	<script
		src="${pageContext.request.contextPath}/assets/js/messageModal.js"></script>
</body>
</html>