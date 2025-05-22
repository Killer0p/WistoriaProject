<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Wistoria - Premium Watch Collection</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/messageModal.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/index.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/header.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/footer.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/newsletter.css">
</head>
<body>
	<!-- Header -->
	<%@ include file="WEB-INF/views/common/header.jsp"%>

	<!-- Message Modal: Include error/success message display -->
	<%@ include file="WEB-INF/views/common/messageModal.jsp"%>

	<!-- Hero Section -->
	<section class="hero">
		<div class="container">
			<div class="hero-content">
				<h1>Discover Timeless Elegance</h1>
				<p>Explore our exclusive collection of premium watches. Find the
					perfect timepiece that matches your style and personality.</p>
				<div class="hero-buttons">
					<a href="${pageContext.request.contextPath}/register" class="btn">Register
						Now</a> <a href="${pageContext.request.contextPath}/login"
						class="btn btn-outline">Login</a>
				</div>
			</div>
		</div>
		<div class="hero-image"></div>
	</section>

	<!-- Categories Section -->
	<section class="categories">
		<div class="container">
			<div class="section-title">
				<h2>Browse Categories</h2>
				<p>Discover our wide range of watch collections</p>
			</div>
			<div class="categories-grid">
				<c:forEach var="i" begin="1" end="4">
					<div class="category-card">
						<div class="category-image">
							<!-- Use local asset instead of external URL -->
							<img
								src="${pageContext.request.contextPath}/assets/images/category${i}.jpeg"
								alt="Category Image">
						</div>
						<div class="category-content">
							<c:choose>
								<c:when test="${i == 1}">
									<h3>Luxury Watches</h3>
									<a href="${pageContext.request.contextPath}/collections/luxury"
										class="btn btn-outline">View Collection</a>
								</c:when>
								<c:when test="${i == 2}">
									<h3>Sports Watches</h3>
									<a href="${pageContext.request.contextPath}/collections/sports"
										class="btn btn-outline">View Collection</a>
								</c:when>
								<c:when test="${i == 3}">
									<h3>Smart Watches</h3>
									<a href="${pageContext.request.contextPath}/collections/smart"
										class="btn btn-outline">View Collection</a>
								</c:when>
								<c:when test="${i == 4}">
									<h3>Classic Watches</h3>
									<a
										href="${pageContext.request.contextPath}/collections/classic"
										class="btn btn-outline">View Collection</a>
								</c:when>
							</c:choose>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</section>

	<!-- Promo Banner -->
	<section class="promo-banner">
		<div class="container">
			<div class="promo-content">
				<h2>Special Anniversary Sale</h2>
				<p>Celebrate our 5th anniversary with exclusive discounts on
					premium watches. Limited time offer!</p>
				<div class="countdown">
					<div class="countdown-item">
						<div class="countdown-number" id="days">03</div>
						<div class="countdown-label">Days</div>
					</div>
					<div class="countdown-item">
						<div class="countdown-number" id="hours">12</div>
						<div class="countdown-label">Hours</div>
					</div>
					<div class="countdown-item">
						<div class="countdown-number" id="minutes">45</div>
						<div class="countdown-label">Minutes</div>
					</div>
					<div class="countdown-item">
						<div class="countdown-number" id="seconds">22</div>
						<div class="countdown-label">Seconds</div>
					</div>
				</div>
				<a href="${pageContext.request.contextPath}/sale" class="btn">Shop
					the Sale</a>
			</div>
		</div>
	</section>

	<!-- Testimonials -->
	<section class="testimonials">
		<div class="container">
			<div class="section-title">
				<h2>What Our Customers Say</h2>
				<p>Hear from our satisfied customers</p>
			</div>
			<div class="testimonials-grid">
				<c:forEach var="i" begin="1" end="3">
					<div class="testimonial-card">
						<div class="testimonial-content">
							<p>
								<c:choose>
									<c:when test="${i == 1}">
                                        The craftsmanship and attention to detail in my Wistoria watch is exceptional. I've received countless compliments, and the customer service was outstanding!
                                    </c:when>
									<c:when test="${i == 2}">
                                        I was looking for a premium watch that wouldn't break the bank. Wistoria delivered with style, precision, and qualityhty that exceeded my expectations.
                                    </c:when>
									<c:when test="${i == 3}">
                                        From browsing to unboxing, my experience with Wistoria was flawless. The watch arrived beautifully packaged and looks even better in person than online.
                                    </c:when>
								</c:choose>
							</p>
						</div>
						<div class="testimonial-author">
							<div class="author-image">
								<!-- Use local asset instead of external URL -->
								<img
									src="${pageContext.request.contextPath}/assets/images/author${i}.jpeg"
									alt="Author Image">
							</div>
							<div class="author-info">
								<h4>
									<c:choose>
										<c:when test="${i == 1}">Michael Johnson</c:when>
										<c:when test="${i == 2}">Sarah Williams</c:when>
										<c:when test="${i == 3}">Robert Chen</c:when>
									</c:choose>
								</h4>
								<p>Verified Customer</p>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</section>

	<!-- Newsletter -->
	<%@ include file="WEB-INF/views/common/newsletter.jsp"%>
	<!-- Footer -->
	<%@ include file="WEB-INF/views/common/footer.jsp"%>
	
	<!-- JavaScript -->
	<script src="${pageContext.request.contextPath}/assets/js/messageModal.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/index.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/header.js"></script>
</body>
</html>