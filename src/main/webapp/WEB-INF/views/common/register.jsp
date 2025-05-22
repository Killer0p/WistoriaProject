<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Register - Wistoria</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/messageModal.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/register.css">
</head>
<body>
	<!-- Message Modal -->
	<%@ include file="/WEB-INF/views/common/messageModal.jsp"%>

	<div class="register-container">
		<!-- SVG Section -->
		<div class="svg-section">
			<svg viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg">
                <circle cx="100" cy="100" r="80" fill="#3498db" />
                <path d="M100 40 V100 H160" stroke="#fff"
					stroke-width="10" fill="none" />
                <circle cx="100" cy="100" r="10" fill="#fff" />
            </svg>
		</div>

		<!-- Form Section -->
		<div class="form-section">
			<h1>Wistoria</h1>
			<h2>Create your account</h2>

			<form action="${pageContext.request.contextPath}/register"
				method="post">
				<label for="name">Full Name <input type="text" id="name"
					name="name" required placeholder="John Doe" aria-label="Full Name">
				</label> <label for="email">Email Address <input type="email"
					id="email" name="email" required placeholder="you@example.com"
					aria-label="Email Address">
				</label> <label for="password">Password <input type="password"
					id="password" name="password" required
					placeholder="Create a password" aria-label="Password">
				</label> <input type="submit" value="Create Account">
			</form>

			<div class="nav-links">
				<p>
					Already have an account? <a
						href="${pageContext.request.contextPath}/login">Sign In</a>
				</p>
			</div>
		</div>
	</div>
	<!-- JavaScript -->
	<script
		src="${pageContext.request.contextPath}/assets/js/messageModal.js"></script>
</body>
</html>