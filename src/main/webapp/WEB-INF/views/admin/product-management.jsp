<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Custom CSS for product management -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/product-management.css">

<h1 class="page-title">Products Management</h1>

<div class="card">
	<!-- Search Form -->
	<form action="${pageContext.request.contextPath}/admin/product"
		method="get" class="search-form">
		<input type="hidden" name="action" value="search"> <input
			type="text" class="search-input" placeholder="Search products..."
			name="searchTerm" value="${searchTerm}">
		<button class="search-btn" type="submit">Search</button>
	</form>

	<!-- Add New Watch Button -->
	<a id="addNewWatchBtn" class="btn btn-primary"
		href="${pageContext.request.contextPath}/admin/product?action=add">
		<i class="fas fa-plus"></i> Add New Watch
	</a>

	<!-- Product Table -->
	<div class="table-responsive">
		<table>
			<thead>
				<tr>
					<th>ID</th>
					<th>Image</th>
					<th>Watch Name</th>
					<th>Brand</th>
					<th>Price</th>
					<th>Stock</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${products}" var="product">
					<tr>
						<td>#${product.productId}</td>
						<td><img src="${product.imageUrl}" alt="${product.name}"
							class="product-img" width="60" height="60"></td>
						<td>${product.name}</td>
						<td>${product.brand}</td>
						<td>$${product.price}</td>
						<td><c:choose>
								<c:when test="${product.stockQuantity > 10}">
									<span class="stock-badge stock-in">In Stock
										(${product.stockQuantity})</span>
								</c:when>
								<c:when test="${product.stockQuantity > 0}">
									<span class="stock-badge stock-low">Low Stock
										(${product.stockQuantity})</span>
								</c:when>
								<c:otherwise>
									<span class="stock-badge stock-out">Out of Stock</span>
								</c:otherwise>
							</c:choose></td>
						<td class="actions">
							<!-- View Button -->
							<form action="${pageContext.request.contextPath}/admin/product"
								method="get" style="display: inline;">
								<input type="hidden" name="action" value="view"> <input
									type="hidden" name="id" value="${product.productId}">
								<button type="submit" class="btn btn-info">View</button>
							</form> <!-- Edit Button -->
							<form action="${pageContext.request.contextPath}/admin/product"
								method="get" style="display: inline;">
								<input type="hidden" name="action" value="edit"> <input
									type="hidden" name="id" value="${product.productId}">
								<button type="submit" class="btn btn-warning">Edit</button>
							</form> <!-- Delete Button -->
							<form action="${pageContext.request.contextPath}/admin/product"
								method="get" style="display: inline;"
								onsubmit="return confirm('Are you sure you want to delete this product?');">
								<input type="hidden" name="action" value="delete"> <input
									type="hidden" name="id" value="${product.productId}">
								<button type="submit" class="btn btn-danger">Delete</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<!-- Pagination Section -->
	<c:if test="${totalPages > 1}">
		<div class="pagination">
			<!-- First and Previous buttons -->
			<c:if test="${currentPage > 1}">
				<a
					href="${pageContext.request.contextPath}/admin/product?action=list&page=1"
					class="page-link">«</a>
				<a
					href="${pageContext.request.contextPath}/admin/product?action=list&page=${currentPage - 1}"
					class="page-link">‹</a>
			</c:if>

			<!-- Page numbers -->
			<c:set var="startPage"
				value="${(currentPage - 2) > 0 ? (currentPage - 2) : 1}" />
			<c:set var="endPage"
				value="${(startPage + 4) <= totalPages ? (startPage + 4) : totalPages}" />

			<c:forEach begin="${startPage}" end="${endPage}" var="i">
				<c:choose>
					<c:when test="${currentPage == i}">
						<span class="page-link active">${i}</span>
					</c:when>
					<c:otherwise>
						<a
							href="${pageContext.request.contextPath}/admin/product?action=list&page=${i}"
							class="page-link">${i}</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>

			<!-- Next and Last buttons -->
			<c:if test="${currentPage < totalPages}">
				<a
					href="${pageContext.request.contextPath}/admin/product?action=list&page=${currentPage + 1}"
					class="page-link">›</a>
				<a
					href="${pageContext.request.contextPath}/admin/product?action=list&page=${totalPages}"
					class="page-link">»</a>
			</c:if>
		</div>
	</c:if>

	<!-- No records message -->
	<c:if test="${empty products}">
		<div class="no-records">No products found matching your
			criteria.</div>
	</c:if>
</div>