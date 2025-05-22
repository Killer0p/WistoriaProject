<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<section class="cart-section">
    <div class="container">
        <div class="section-title">
            <h2>Your Cart</h2>
            <p><c:out value="${itemCount}"/> item(s) in your cart</p>
        </div>

        <div class="cart-container">
            <!-- Cart Items -->
            <div class="cart-items">
                <c:choose>
                    <c:when test="${empty cartItems}">
                        <div class="empty-cart">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true">
                                <path fill="none" stroke="#6b7280" stroke-width="1.5" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"/>
                            </svg>
                            <p>Your cart is empty.</p>
                            <a href="${pageContext.request.contextPath}/user/home" class="cta-btn primary-btn">Shop Now</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${cartItems}">
                            <form action="${pageContext.request.contextPath}/user/cart" method="post">
                                <input type="hidden" name="action" value="update"/>
                                <input type="hidden" name="cartItemId" value="${item.cartItemId}"/>
                                <div class="cart-item">
                                    <img src="<c:out value='${item.product.imageUrl}'/>" alt="<c:out value='${item.product.name}'/>" class="item-image">
                                    <div class="item-details">
                                        <div class="item-info">
                                            <h3><c:out value="${item.product.name}"/></h3>
                                            <p class="brand"><c:out value="${item.product.brand}"/></p>
                                            <p class="price">$<fmt:formatNumber value="${item.product.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/></p>
                                        </div>
                                        <div class="quantity-control">
                                            <label for="quantity-${item.cartItemId}">Quantity:</label>
                                            <input type="number" id="quantity-${item.cartItemId}" name="quantity"
                                                   value="${item.quantity}" min="1" max="${item.product.stockQuantity}"
                                                   aria-label="Set quantity for ${item.product.name}">
                                        </div>
                                        <p class="subtotal">Subtotal: $<fmt:formatNumber value="${item.quantity * item.product.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/></p>
                                        <button type="submit" class="item-update-btn" aria-label="Update quantity for ${item.product.name}">Update</button>
                                        <a href="${pageContext.request.contextPath}/user/cart?action=remove&cartItemId=${item.cartItemId}"
                                           class="remove-btn" aria-label="Remove ${item.product.name} from cart">Remove</a>
                                    </div>
                                </div>
                            </form>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Order Summary -->
            <c:if test="${not empty cartItems}">
                <div class="order-summary">
                    <h3>Order Summary</h3>
                    <div class="summary-details">
                        <p>Subtotal <span>$<fmt:formatNumber value="${cartTotal}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span></p>
                        <p>Shipping <span>Calculated at checkout</span></p>
                        <p class="total">Total <span>$<fmt:formatNumber value="${cartTotal}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span></p>
                    </div>
                    <a href="${pageContext.request.contextPath}/user/checkout" class="checkout-btn cta-btn primary-btn">Proceed to Checkout</a>
                    <a href="${pageContext.request.contextPath}/user/cart?action=clear" class="cta-btn secondary-btn">Clear Cart</a>
                </div>
            </c:if>
        </div>
    </div>
</section>

<!-- Page-specific CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cart.css">