<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/order-management.css">

<h1 class="page-title">Orders Management</h1>

<!-- Display error message -->
<c:if test="${not empty errorMessage}">
	<div class="alert alert-danger">${errorMessage}</div>
</c:if>

<div class="card">
	<form action="${pageContext.request.contextPath}/admin/order"
		method="get" class="filters">
		<select name="status" class="filter-select">
			<option value="">Filter by Status</option>
			<option value="pending" ${status == 'pending' ? 'selected' : ''}>Pending</option>
			<option value="paid" ${status == 'paid' ? 'selected' : ''}>Paid</option>
			<option value="shipped" ${status == 'shipped' ? 'selected' : ''}>Shipped</option>
			<option value="cancelled" ${status == 'cancelled' ? 'selected' : ''}>Cancelled</option>
		</select> <select name="sortBy" class="filter-select">
			<option value="">Sort By</option>
			<option value="newest" ${sortBy == 'newest' ? 'selected' : ''}>Newest
				First</option>
			<option value="oldest" ${sortBy == 'oldest' ? 'selected' : ''}>Oldest
				First</option>
			<option value="highest" ${sortBy == 'highest' ? 'selected' : ''}>Highest
				Amount</option>
			<option value="lowest" ${sortBy == 'lowest' ? 'selected' : ''}>Lowest
				Amount</option>
		</select> <input type="date" name="dateFrom" class="filter-select"
			placeholder="Date From" value="${dateFrom}"> <input
			type="date" name="dateTo" class="filter-select" placeholder="Date To"
			value="${dateTo}">
		<button type="submit" class="btn btn-primary">Apply Filters</button>
	</form>

	<table>
		<thead>
			<tr>
				<th>Order ID</th>
				<th>Customer</th>
				<th>Date</th>
				<th>Amount</th>
				<th>Status</th>
				<th>Actions</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${orders}" var="order">
				<tr>
					<td>#${order.orderId}</td>
					<td><c:out
							value="${order.user.name != null ? order.user.name : 'Unknown User'}" /></td>
					<td><fmt:parseDate value="${order.orderDate}"
							pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" type="both" /> <fmt:formatDate
							value="${parsedDate}" pattern="MMM dd, yyyy" /></td>
					<td>$<fmt:formatNumber value="${order.totalAmount}"
							pattern="#,##0.00" /></td>
					<td><span
						class="status-badge status-${order.status.toLowerCase()}">${order.status}</span></td>
					<td class="actions">
						<form action="${pageContext.request.contextPath}/admin/order"
							method="get" style="display: inline;">
							<input type="hidden" name="orderId" value="${order.orderId}">
							<input type="hidden" name="page" value="${currentPage}">
							<input type="hidden" name="status" value="${status}"> <input
								type="hidden" name="sortBy" value="${sortBy}"> <select
								name="updateStatus" onchange="this.form.submit()"
								class="status-select">
								<option value="">Update Status</option>
								<option value="pending"
									${order.status == 'pending' ? 'disabled' : ''}>Pending</option>
								<option value="paid" ${order.status == 'paid' ? 'disabled' : ''}>Paid</option>
								<option value="shipped"
									${order.status == 'shipped' ? 'disabled' : ''}>Shipped</option>
								<option value="cancelled"
									${order.status == 'cancelled' ? 'disabled' : ''}>Cancelled</option>
							</select>
						</form>
					</td>
				</tr>
				<tr class="order-details-row">
					<td colspan="6">
						<div class="order-details" id="details-${order.orderId}">
							<h4>Order Items</h4>
							<c:choose>
								<c:when test="${not empty order.orderDetails}">
									<c:forEach items="${order.orderDetails}" var="detail">
										<div class="detail-item">
											<c:choose>
												<c:when test="${not empty detail.product.imageUrl}">
													<img
														src="${pageContext.request.contextPath}${detail.product.imageUrl}"
														alt="${detail.product.name}" class="detail-img">
												</c:when>
												<c:otherwise>
													<img src="https://via.placeholder.com/50"
														alt="${detail.product.name != null ? detail.product.name : 'Unknown Product'}"
														class="detail-img">
												</c:otherwise>
											</c:choose>
											<div class="detail-info">
												<div class="detail-name">${detail.product.name != null ? detail.product.name : 'Unknown Product'}</div>
												<div class="detail-price">
													$
													<fmt:formatNumber value="${detail.price}"
														pattern="#,##0.00" />
												</div>
												<div class="detail-qty">Qty: ${detail.quantity}</div>
											</div>
										</div>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<p>No items found for this order.</p>
								</c:otherwise>
							</c:choose>
							<h4 style="margin-top: 15px;">Shipping Information</h4>
							<p>
								Address:
								<c:out
									value="${order.shippingInfo.address != null ? order.shippingInfo.address : 'N/A'}" />
							</p>
							<p>
								Contact:
								<c:out
									value="${order.user.phoneNumber != null ? order.user.phoneNumber : 'N/A'}" />
							</p>
							<p>
								Email:
								<c:out
									value="${order.user.email != null ? order.user.email : 'N/A'}" />
							</p>
							<p>
								Shipping Method:
								<c:out
									value="${order.shippingInfo.shippingMethod != null ? order.shippingInfo.shippingMethod : 'N/A'}" />
							</p>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="pagination">
		<c:if test="${currentPage > 1}">
			<a
				href="${pageContext.request.contextPath}/admin/order?page=1&status=${status}&sortBy=${sortBy}&dateFrom=${dateFrom}&dateTo=${dateTo}"
				class="page-link">«</a>
			<a
				href="${pageContext.request.contextPath}/admin/order?page=${currentPage - 1}&status=${status}&sortBy=${sortBy}&dateFrom=${dateFrom}&dateTo=${dateTo}"
				class="page-link">‹</a>
		</c:if>
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
						href="${pageContext.request.contextPath}/admin/order?page=${i}&status=${status}&sortBy=${sortBy}&dateFrom=${dateFrom}&dateTo=${dateTo}"
						class="page-link">${i}</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:if test="${currentPage < totalPages}">
			<a
				href="${pageContext.request.contextPath}/admin/order?page=${currentPage + 1}&status=${status}&sortBy=${sortBy}&dateFrom=${dateFrom}&dateTo=${dateTo}"
				class="page-link">›</a>
			<a
				href="${pageContext.request.contextPath}/admin/order?page=${totalPages}&status=${status}&sortBy=${sortBy}&dateFrom=${dateFrom}&dateTo=${dateTo}"
				class="page-link">»</a>
		</c:if>
	</div>

	<c:if test="${empty orders}">
		<div class="no-records">No orders found matching your criteria.</div>
	</c:if>
</div>

<script
	src="${pageContext.request.contextPath}/assets/js/order-management.js"></script>