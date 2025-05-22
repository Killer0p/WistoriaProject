<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${product.name}- Product Detail</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin-product-detail.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
	<div class="container">
		<!-- Display success message -->
		<c:if test="${not empty sessionScope.success}">
			<div class="alert alert-success">${sessionScope.success}</div>
			<c:remove var="success" scope="session" />
		</c:if>
		<!-- Display error message -->
		<c:if test="${not empty sessionScope.error}">
			<div class="alert alert-danger">${sessionScope.error}</div>
			<c:remove var="error" scope="session" />
		</c:if>

		<div class="product-detail">
			<div class="product-image-container">
				<img src="${product.imageUrl}" alt="${product.name}"
					class="product-image">
			</div>

			<div class="product-info">
				<div class="product-header">
					<h1 class="product-title">${product.name}</h1>
					<div class="product-brand">
						<i class="fas fa-trademark"></i> ${product.brand}
					</div>
					<div class="product-price">$${product.price}</div>

					<!-- Stock display with conditional styling -->
					<div
						class="stock-info ${product.stockQuantity > 10 ? 'in-stock' : (product.stockQuantity > 0 ? 'low-stock' : 'out-of-stock')}">
						<i
							class="fas ${product.stockQuantity > 10 ? 'fa-check-circle' : (product.stockQuantity > 0 ? 'fa-exclamation-circle' : 'fa-times-circle')}"></i>
						${product.stockQuantity > 10 ? 'In Stock' : (product.stockQuantity > 0 ? 'Low Stock: ' + product.stockQuantity + ' left' : 'Out of Stock')}
					</div>
				</div>

				<div class="product-description">
					<p>${product.description}</p>
				</div>

				<div class="product-attributes">
					<div class="attribute">
						<span class="attribute-label">Product ID</span> <span
							class="attribute-value">${product.productId}</span>
					</div>
					<div class="attribute">
						<span class="attribute-label">Category</span> <span
							class="attribute-value">${product.category}</span>
					</div>
					<div class="attribute">
						<span class="attribute-label">Case Material</span> <span
							class="attribute-value">${product.caseMaterial}</span>
					</div>
					<div class="attribute">
						<span class="attribute-label">Movement Type</span> <span
							class="attribute-value">${product.movementType}</span>
					</div>
					<div class="attribute">
						<span class="attribute-label">Water Resistance</span> <span
							class="attribute-value">${product.waterResistanceMeters}
							meters</span>
					</div>
					<div class="attribute">
						<span class="attribute-label">Warranty</span> <span
							class="attribute-value">${product.warrantyMonths} months</span>
					</div>
				</div>

				<div class="admin-actions">
					<a
						href="${pageContext.request.contextPath}/admin/product?action=edit&id=${product.productId}"
						class="btn btn-edit"> <i class="fas fa-edit"></i> Edit
					</a>
					<button class="btn btn-delete" onclick="openDeleteModal()">
						<i class="fas fa-trash-alt"></i> Delete
					</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Delete Confirmation Modal -->
	<div id="deleteModal" class="modal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Confirm Deletion</h5>
					<button type="button" class="close" onclick="closeDeleteModal()">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p>
						Are you sure you want to delete the product <strong>${product.name}</strong>?
					</p>
				</div>
				<div class="modal-footer">
					<form action="${pageContext.request.contextPath}/admin/product"
						method="get">
						<input type="hidden" name="action" value="delete"> <input
							type="hidden" name="id" value="${product.productId}">
						<button type="button" class="btn btn-secondary"
							onclick="closeDeleteModal()">Cancel</button>
						<button type="submit" class="btn btn-danger">Delete</button>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script>
		function openDeleteModal() {
			document.getElementById('deleteModal').style.display = 'block';
		}

		function closeDeleteModal() {
			document.getElementById('deleteModal').style.display = 'none';
		}

		// Close modal when clicking outside
		window.addEventListener('click', function(event) {
			const modal = document.getElementById('deleteModal');
			if (event.target === modal) {
				modal.style.display = 'none';
			}
		});
	</script>
</body>
</html>