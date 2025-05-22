<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Order Confirmation - Watch Store</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/order-confirmation.css">
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap"
	rel="stylesheet">
</head>
<body>
	<main>
		<section class="container">
			<div class="section-title">
				<h2>Order Confirmation</h2>
				<p>Thank you for your purchase!</p>
			</div>

			<!-- Display Messages -->
			<c:if test="${not empty success}">
				<div class="message success">
					<i class="fas fa-check-circle"></i>
					<c:out value="${success}" />
				</div>
				<c:remove var="success" scope="session" />
			</c:if>
			<c:if test="${not empty errorMessage}">
				<div class="message error">
					<i class="fas fa-exclamation-circle"></i>
					<c:out value="${errorMessage}" />
				</div>
				<c:remove var="errorMessage" scope="session" />
			</c:if>

			<c:choose>
				<c:when test="${not empty order}">
					<!-- Calculate shipping cost and subtotal -->
					<c:set var="shippingCost"
						value="${shippingInfo.shippingMethod == 'standard' ? 5.0 : shippingInfo.shippingMethod == 'express' ? 15.0 : 0.0}" />
					<c:set var="subtotal" value="${order.totalAmount - shippingCost}" />

					<div class="confirmation-container">
						<div class="order-details">
							<h3>
								Order #
								<c:out value="${order.orderId}" />
							</h3>
							<p>
								<strong>Order Date:</strong>
								<fmt:formatDate value="${orderDateAsDate}"
									pattern="MMM dd, yyyy" />
							</p>
							<p>
								<strong>Status:</strong>
								<c:out value="${order.status}" />
							</p>
							<h4>Items</h4>
							<c:choose>
								<c:when test="${not empty order.orderDetails}">
									<c:forEach var="detail" items="${order.orderDetails}">
										<div class="order-item">
											<img
												src="${not empty detail.product.imageUrl ? detail.product.imageUrl : 'https://via.placeholder.com/100'}"
												alt="${detail.product.name != null ? detail.product.name : 'Unknown Product'}"
												class="item-image">
											<div class="item-details">
												<div class="item-info">
													<h4>
														<c:out
															value="${detail.product.name != null ? detail.product.name : 'Unknown Product'}" />
													</h4>
													<p class="brand">
														<c:out
															value="${detail.product.brand != null ? detail.product.brand : 'N/A'}" />
													</p>
													<p class="price">
														$
														<fmt:formatNumber value="${detail.price}"
															pattern="#,##0.00" />
													</p>
												</div>
												<p class="quantity">
													Quantity:
													<c:out value="${detail.quantity}" />
												</p>
												<p class="subtotal">
													Subtotal: $
													<fmt:formatNumber value="${detail.quantity * detail.price}"
														pattern="#,##0.00" />
												</p>
											</div>
										</div>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<p>No items found for this order.</p>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="order-summary">
							<h3>Order Summary</h3>
							<div class="summary-details">
								<p>
									Subtotal <span>$<fmt:formatNumber value="${subtotal}"
											pattern="#,##0.00" /></span>
								</p>
								<p>
									Shipping <span>$<fmt:formatNumber
											value="${shippingCost}" pattern="#,##0.00" /></span>
								</p>
								<p class="total">
									Total <span>$<fmt:formatNumber
											value="${order.totalAmount}" pattern="#,##0.00" /></span>
								</p>
							</div>
							<h3>Shipping Information</h3>
							<p>
								<strong>Address:</strong>
								<c:out
									value="${shippingInfo.address != null ? shippingInfo.address : 'N/A'}" />
							</p>
							<p>
								<strong>Contact:</strong>
								<c:out
									value="${shippingInfo.contact != null ? shippingInfo.contact : 'N/A'}" />
							</p>
							<p>
								<strong>Method:</strong>
								<c:out
									value="${shippingInfo.shippingMethod != null ? shippingInfo.shippingMethod : 'N/A'}" />
							</p>
							<a href="${pageContext.request.contextPath}/user/home"
								class="cta-btn primary-btn">Continue Shopping</a>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="empty-confirmation">
						<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
							aria-hidden="true">
                            <path fill="none" stroke="#6b7280"
								stroke-width="1.5"
								d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                            <circle cx="8" cy="8" r="1" fill="#6b7280" />
                            <circle cx="16" cy="8" r="1" fill="#6b7280" />
                        </svg>
						<p>Order not found.</p>
						<a href="${pageContext.request.contextPath}/user/home"
							class="cta-btn primary-btn">Shop Now</a>
					</div>
				</c:otherwise>
			</c:choose>
		</section>
	</main>
</body>
</html>