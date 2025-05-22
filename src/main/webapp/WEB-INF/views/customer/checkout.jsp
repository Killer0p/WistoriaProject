<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - Watch Store</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/checkout.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <main>
        <section class="container">
            <div class="section-title">
                <h2>Checkout</h2>
                <p>Review your order and enter shipping details</p>
            </div>

            <c:if test="${not empty success}">
                <div class="message success"><i class="fas fa-check-circle"></i> <c:out value="${success}"/></div>
                <c:remove var="success" scope="session"/>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="message error"><i class="fas fa-exclamation-circle"></i> <c:out value="${errorMessage}"/></div>
                <c:remove var="errorMessage" scope="session"/>
            </c:if>

            <div class="checkout-container">
                <c:choose>
                    <c:when test="${empty cartItems}">
                        <div class="empty-checkout">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true">
                                <path fill="none" stroke="#6b7280" stroke-width="1.5" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"/>
                                <circle cx="8" cy="8" r="1" fill="#6b7280"/>
                                <circle cx="16" cy="8" r="1" fill="#6b7280"/>
                            </svg>
                            <p>Your cart is empty.</p>
                            <a href="${pageContext.request.contextPath}/user/home" class="cta-btn primary-btn">Shop Now</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="checkout-items">
                            <h3>Order Items</h3>
                            <c:forEach var="item" items="${cartItems}">
                                <div class="checkout-item">
                                    <img src="${item.product.imageUrl}" alt="${item.product.name}" class="item-image">
                                    <div class="item-details">
                                        <div class="item-info">
                                            <h4><c:out value="${item.product.name}"/></h4>
                                            <p class="brand"><c:out value="${item.product.brand}"/></p>
                                            <p class="price">$<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/></p>
                                        </div>
                                        <p class="quantity">Quantity: <c:out value="${item.quantity}"/></p>
                                        <p class="subtotal">Subtotal: $<fmt:formatNumber value="${item.quantity * item.product.price}" pattern="#,##0.00"/></p>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <div class="checkout-form-summary">
                            <h3>Shipping Information</h3>
                            <form action="${pageContext.request.contextPath}/user/checkout" method="post" class="checkout-form">
                                <input type="hidden" name="action" value="confirm">
                                <div class="form-group">
                                    <label for="address">Shipping Address</label>
                                    <input type="text" id="address" name="address" required>
                                </div>
                                <div class="form-group">
                                    <label for="contact">Contact Number</label>
                                    <input type="tel" id="contact" name="contact" required>
                                </div>
                                <div class="form-group">
                                    <label for="shippingMethod">Shipping Method</label>
                                    <select id="shippingMethod" name="shippingMethod" required>
                                        <option value="">Select a method</option>
                                        <option value="standard">Standard Shipping ($5.00)</option>
                                        <option value="express">Express Delivery ($15.00)</option>
                                    </select>
                                </div>
                                <div class="order-summary">
                                    <h3>Order Summary</h3>
                                    <div class="summary-details">
                                        <p>Subtotal <span>$<fmt:formatNumber value="${cartSubtotal}" pattern="#,##0.00"/></span></p>
                                        <p>Shipping <span id="shipping-cost">Select a method</span></p>
                                        <p class="total">Total <span id="total-cost">$<fmt:formatNumber value="${cartSubtotal}" pattern="#,##0.00"/></span></p>
                                    </div>
                                    <button type="submit" class="checkout-btn cta-btn primary-btn">
                                        <i class="fas fa-check"></i> Place Order
                                    </button>
                                </div>
                            </form>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </main>

    <!-- JavaScript for dynamic shipping cost -->
    <script>
        const shippingMethodSelect = document.getElementById('shippingMethod');
        const shippingCostSpan = document.getElementById('shipping-cost');
        const totalCostSpan = document.getElementById('total-cost');
        const subtotal = parseFloat('${cartSubtotal}');

        shippingMethodSelect.addEventListener('change', () => {
            const method = shippingMethodSelect.value;
            let shippingCost = 0;
            if (method === 'standard') {
                shippingCost = 5.00;
                shippingCostSpan.textContent = '$5.00';
            } else if (method === 'express') {
                shippingCost = 15.00;
                shippingCostSpan.textContent = '$15.00';
            } else {
                shippingCostSpan.textContent = 'Select a method';
            }
            totalCostSpan.textContent = '$' + (subtotal + shippingCost).toFixed(2);
        });
    </script>
</body>
</html>