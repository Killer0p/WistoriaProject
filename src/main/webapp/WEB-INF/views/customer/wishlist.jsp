<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Wishlist - Watch Store</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/wishlist.css">
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap"
	rel="stylesheet">
</head>
<body>
	<main>
		<section class="container">
			<div class="section-title">
				<h2>My Wishlist</h2>
				<p>View your favorite products</p>
			</div>

			<!-- Display Messages -->
			<c:if test="${not empty message}">
				<div class="message">
					<i class="fas fa-check-circle"></i> ${message}
				</div>
				<c:remove var="message" scope="session" />
			</c:if>
			<c:if test="${not empty errorMessage}">
				<div class="error-message">
					<i class="fas fa-exclamation-circle"></i> ${errorMessage}
				</div>
				<c:remove var="errorMessage" scope="session" />
			</c:if>

			<div class="wishlist-container">
				<c:choose>
					<c:when test="${empty wishlistItems}">
						<div class="empty-wishlist">
							<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
								aria-hidden="true">
                                <path fill="none" stroke="#6b7280"
									stroke-width="1.5"
									d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z" />
                            </svg>
							<p>Your wishlist is empty.</p>
							<a href="${pageContext.request.contextPath}/user/home"
								class="cta-btn primary-btn">Shop Now</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="wishlist-items">
							<c:forEach var="item" items="${wishlistItems}">
								<div class="wishlist-item">
									<img src="${item.product.imageUrl}" alt="${item.product.name}"
										class="item-image">
									<div class="item-details">
										<div class="item-info">
											<h4>${item.product.name}</h4>
											<p class="brand">${item.product.brand}</p>
											<p class="price">$${item.product.price}</p>
										</div>
										<div class="item-actions">
											<form
												action="${pageContext.request.contextPath}/user/wishlist"
												method="post" class="action-form">
												<input type="hidden" name="action" value="moveToCart">
												<input type="hidden" name="wishlistId"
													value="${item.wishlistId}">
												<button type="submit" class="cta-btn primary-btn">
													<i class="fas fa-cart-plus"></i> Add to Cart
												</button>
											</form>
											<form
												action="${pageContext.request.contextPath}/user/wishlist"
												method="post" class="action-form">
												<input type="hidden" name="action" value="remove"> <input
													type="hidden" name="wishlistId" value="${item.wishlistId}">
												<button type="submit" class="cta-btn secondary-btn">
													<i class="fas fa-trash"></i> Remove
												</button>
											</form>
										</div>
									</div>
								</div>
							</c:forEach>
						</div>
						<div class="wishlist-actions">
							<form action="${pageContext.request.contextPath}/user/wishlist"
								method="post">
								<input type="hidden" name="action" value="clear">
								<button type="submit" class="cta-btn secondary-btn">
									<i class="fas fa-trash-alt"></i> Clear Wishlist
								</button>
							</form>
							<a href="${pageContext.request.contextPath}/user/cart"
								class="cta-btn primary-btn">View Cart</a>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</section>
	</main>
</body>
</html>