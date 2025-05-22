<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login - Wistoria</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/messageModal.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body>
	<!-- Message Modal -->
	<%@ include file="/WEB-INF/views/common/messageModal.jsp"%>

	<div class="login-container">
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
			<h2>Welcome back! Please log in</h2>

			<form action="${pageContext.request.contextPath}/login" method="post">
				<label for="email">Email Address <input type="email"
					id="email" name="email" required placeholder="you@example.com"
					value="${not empty userCookieEmail ? userCookieEmail : ''}"
					aria-label="Email Address">
				</label> <label for="password">Password <input type="password"
					id="password" name="password" required
					placeholder="Enter your password" aria-label="Password">
				</label> <label class="checkbox-label"> <input type="checkbox"
					name="rememberMe" id="rememberMe"> Remember me
				</label> <input type="submit" value="Login">
			</form>

			<div class="nav-links">
				<p>
					Don't have an account? <a
						href="${pageContext.request.contextPath}/register">Register
						here</a>
				</p>
			</div>
		</div>
	</div>
	<!-- JavaScript -->
	<script
		src="${pageContext.request.contextPath}/assets/js/messageModal.js"></script>
</body>
</html>