<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<section class="search-result-section">
    <div class="search-result-container">
        <h2>
            Search Results for "<c:out value='${searchQuery}'/>"
        </h2>

        <!-- Messages -->
        <c:if test="${not empty errorMessage}">
            <div class="message error">
                <i class="fas fa-exclamation-circle"></i>
                <c:out value="${errorMessage}"/>
            </div>
            <c:remove var="errorMessage" scope="session"/>
        </c:if>

        <c:choose>
            <c:when test="${empty results}">
                <div class="empty-results">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true">
                        <path fill="none" stroke="#6b7280" stroke-width="1.5" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                    </svg>
                    <p>No products found matching your query.</p>
                    <a href="${pageContext.request.contextPath}/user/home" class="cta-btn primary-btn">Back to Home</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="product-grid">
                    <c:forEach var="product" items="${results}">
                        <div class="product-card">
                            <img src="<c:out value='${product.imageUrl}'/>" alt="<c:out value='${product.name}'/>" class="product-image">
                            <div class="product-info">
                                <h3><c:out value="${product.name}"/></h3>
                                <p class="price">$<fmt:formatNumber value="${product.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/></p>
                                <p class="brand"><c:out value="${product.brand}"/></p>
                                <a href="${pageContext.request.contextPath}/product/detail?id=${product.productId}" class="cta-btn primary-btn">View Details</a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<!-- Page-specific CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/search-result.css">