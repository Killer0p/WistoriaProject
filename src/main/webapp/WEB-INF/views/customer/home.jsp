<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
// Set default limit for pagination
int limit = 10;
String paramLimit = request.getParameter("limit");
if (paramLimit != null) {
	try {
		limit = Integer.parseInt(paramLimit);
	} catch (NumberFormatException ignored) {
	}
}
request.setAttribute("limit", limit);
%>

<main>
	<!-- Hero Section -->
	<section class="hero">
		<div class="container">
			<h1>
				Welcome back,
				<c:out value="${sessionScope.user.name}" default="Friend" />
				!
			</h1>
			<p>Explore our exclusive collection of premium watches. Find the
				perfect timepiece that matches your style and personality.</p>
			<div class="cta-buttons">
				<a href="#recommendations" class="cta-btn primary-btn">View
					Recommendations</a> <a href="#browse" class="cta-btn secondary-btn">Browse
					All Products</a>
			</div>
		</div>
	</section>

	<!-- Recommended Products -->
	<section class="container" id="recommendations">
		<div class="section-title">
			<h2>Recommended For You</h2>
		</div>
		<div class="product-grid">
			<c:forEach var="product" items="${recommendedProducts}">
				<div class="product-card">
					<img src="${product.imageUrl}" alt="${product.name}"
						class="product-image">
					<div class="product-info">
						<h3>${product.name}</h3>
						<p class="price">$${product.price}</p>
						<p class="brand">${product.brand}</p>
						<a
							href="${pageContext.request.contextPath}/product/detail?id=${product.productId}"
							class="view-btn">View Details</a>
					</div>
				</div>
			</c:forEach>
			<c:if test="${empty recommendedProducts}">
				<p>No recommendations available right now.</p>
			</c:if>
		</div>
	</section>

	<!-- Browse All Products with Pagination -->
	<section class="container" id="browse">
		<div class="section-title">
			<h2>Browse All Products</h2>
		</div>
		<div class="product-grid">
			<c:set var="count" value="0" />
			<c:forEach var="product" items="${allProducts}">
				<c:if test="${count lt limit}">
					<div class="product-card">
						<img src="${product.imageUrl}" alt="${product.name}"
							class="product-image">
						<div class="product-info">
							<h3>${product.name}</h3>
							<p class="price">$${product.price}</p>
							<p class="brand">${product.brand}</p>
							<a
								href="${pageContext.request.contextPath}/product/detail?id=${product.productId}"
								class="view-btn">View Details</a>
						</div>
					</div>
					<c:set var="count" value="${count + 1}" />
				</c:if>
			</c:forEach>
			<c:if test="${empty allProducts}">
				<p>No products found.</p>
			</c:if>
		</div>

		<!-- Load More Button -->
		<c:if test="${fn:length(allProducts) > limit}">
			<div class="load-more-container">
				<a
					href="${pageContext.request.contextPath}/user/home?limit=${limit + 10}"
					class="cta-btn primary-btn">Load More</a>
			</div>
		</c:if>
	</section>

	<!-- Additional Content -->
	<section class="container">
		<div class="section-title">
			<h2>Why Choose Us?</h2>
		</div>
		<div class="features">
			<div class="feature">
				<h3>Authentic Brands</h3>
				<p>100% genuine watches from trusted manufacturers.</p>
			</div>
			<div class="feature">
				<h3>Fast Shipping</h3>
				<p>Swift, safe delivery to your doorstep.</p>
			</div>
			<div class="feature">
				<h3>Secure Payments</h3>
				<p>Encrypted transactions with full privacy.</p>
			</div>
		</div>
	</section>
</main>

<!-- Page-specific CSS and JS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/home.css">
<script
	src="${pageContext.request.contextPath}/assets/js/home.js"></script>