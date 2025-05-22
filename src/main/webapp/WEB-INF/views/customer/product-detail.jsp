<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<section class="product-detail-section">
	<div class="container">
		<!-- Messages -->
		<c:if test="${not empty message}">
			<div class="message success">
				<i class="fas fa-check-circle"></i>
				<c:out value="${message}" />
			</div>
			<c:remove var="message" scope="session" />
		</c:if>
		<c:if test="${not empty errorMessage}">
			<div class="message error">
				<i class="fas fa-exclamation-circle"></i>
				<c:out value="${errorMessage}" />
			</div>
			<c:remove var="errorMessage" scope="session" />
		</c:if>

		<c:choose>
			<c:when test="${not empty product}">
				<div class="product-detail-container">
					<div class="product-detail-image">
						<img
							src="<c:out value='${product.imageUrl != null ? product.imageUrl : \"/assets/images/placeholder.jpg\"}'/>"
							alt="<c:out value='${product.name != null ? product.name : \"Product\"}'/>"
							id="product-detail-main-image">
					</div>
					<div class="product-detail-info">
						<h1 class="product-detail-title">
							<c:out value="${product.name != null ? product.name : 'N/A'}" />
						</h1>
						<p class="product-detail-brand">
							<c:out value="${product.brand != null ? product.brand : 'N/A'}" />
						</p>
						<p class="product-detail-price">
							$
							<fmt:formatNumber
								value="${product.price != null ? product.price : 0}"
								type="number" minFractionDigits="2" maxFractionDigits="2" />
						</p>
						<p class="product-detail-stock-status">
							<c:choose>
								<c:when test="${product.inStock}">
									<span class="product-detail-in-stock"><i
										class="fas fa-check-circle"></i> In Stock</span>
								</c:when>
								<c:otherwise>
									<span class="product-detail-out-of-stock"><i
										class="fas fa-times-circle"></i> Out of Stock</span>
								</c:otherwise>
							</c:choose>
						</p>
						<p class="product-detail-description">
							<c:out
								value="${product.description != null ? product.description : 'No description available'}" />
						</p>
						<ul class="product-detail-specifications">
							<li><strong>Category:</strong> <c:out
									value="${product.category != null ? product.category : 'N/A'}" /></li>
							<li><strong>Case Material:</strong> <c:out
									value="${product.caseMaterial != null ? product.caseMaterial : 'N/A'}" /></li>
							<li><strong>Movement Type:</strong> <c:out
									value="${product.movementType != null ? product.movementType : 'N/A'}" /></li>
							<li><strong>Water Resistance:</strong> <c:out
									value="${product.waterResistanceMeters != null ? product.waterResistanceMeters : 'N/A'}" />
								meters</li>
							<li><strong>Warranty:</strong> <c:out
									value="${product.warrantyMonths != null ? product.warrantyMonths : 'N/A'}" />
								months</li>
						</ul>
						<div class="product-detail-actions">
							<form action="${pageContext.request.contextPath}/user/cart"
								method="get" class="product-detail-cart-form">
								<input type="hidden" name="action" value="add"> <input
									type="hidden" name="productId"
									value="<c:out value='${product.productId}'/>">
								<div class="quantity-control">
									<label for="product-detail-quantity">Quantity:</label> <input
										type="number" id="product-detail-quantity" name="quantity"
										value="1" min="1"
										max="${product.stockQuantity != null ? product.stockQuantity : 1}"
										<c:if test="${!product.inStock}">disabled</c:if>>
								</div>
								<button type="submit" class="cta-btn primary-btn"
									<c:if test="${!product.inStock}">disabled</c:if>>
									<i class="fas fa-cart-plus"></i> Add to Cart
								</button>
							</form>
							<form action="${pageContext.request.contextPath}/user/wishlist"
								method="post" class="product-detail-wishlist-form">
								<input type="hidden" name="action"
									value="${isProductInWishlist ? 'remove' : 'add'}"> <input
									type="hidden" name="productId"
									value="<c:out value='${product.productId}'/>">
								<button type="submit" class="cta-btn wishlist-btn"
									<c:if test="${empty sessionScope.user}">disabled title="Please log in to manage your wishlist"</c:if>
									aria-label="${isProductInWishlist ? 'Remove from wishlist' : 'Add to wishlist'}">
									<i
										class="${isProductInWishlist ? 'fas fa-heart' : 'far fa-heart'}"></i>
									${isProductInWishlist ? 'Remove' : 'Wishlist'}
								</button>
							</form>
						</div>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="empty-product">
					<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
						aria-hidden="true">
                        <path fill="none" stroke="#6b7280"
							stroke-width="1.5"
							d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                        <circle cx="8" cy="8" r="1" fill="#6b7280" />
                        <circle cx="16" cy="8" r="1" fill="#6b7280" />
                    </svg>
					<p>Product not found.</p>
					<a href="${pageContext.request.contextPath}/user/home"
						class="cta-btn primary-btn">Shop Now</a>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</section>

<!-- Page-specific CSS and JS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/product-detail.css">
<script>
    // Quantity validation
    const quantityInput = document.getElementById('product-detail-quantity');
    if (quantityInput) {
        quantityInput.addEventListener('input', () => {
            const max = parseInt(quantityInput.max);
            const min = parseInt(quantityInput.min);
            let value = parseInt(quantityInput.value);

            if (value > max) {
                quantityInput.value = max;
            } else if (value < min || isNaN(value)) {
                quantityInput.value = min;
            }
        });
    }

    // Debug form submission
    const wishlistForm = document.querySelector('.product-detail-wishlist-form');
    if (wishlistForm) {
        wishlistForm.addEventListener('submit', (e) => {
            console.log('Wishlist form submitted:', {
                action: wishlistForm.querySelector('input[name="action"]').value,
                productId: wishlistForm.querySelector('input[name="productId"]').value,
                csrfToken: wishlistForm.querySelector('input[name="csrfToken"]').value
            });
        });
    }
</script>