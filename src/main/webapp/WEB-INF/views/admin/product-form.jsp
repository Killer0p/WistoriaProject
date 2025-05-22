<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${product != null ? 'Edit Watch' : 'Add New Watch'}</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/product-form.css">
</head>
<body>
	<div class="add-watch-page">
		<div class="form-container">
			<div class="form-header">
				<h2>${product != null ? 'Edit Watch' : 'Add New Watch'}</h2>
			</div>
			<div class="form-body">
				<!-- Display error message -->
				<c:if test="${not empty error}">
					<div class="alert alert-danger">${error}</div>
				</c:if>
				<!-- Display success message -->
				<c:if test="${not empty sessionScope.success}">
					<div class="alert alert-success">${sessionScope.success}</div>
					<c:remove var="success" scope="session" />
				</c:if>

				<form id="addWatchForm" method="post"
					action="${pageContext.request.contextPath}/admin/product"
					enctype="multipart/form-data">
					<input type="hidden" name="action"
						value="${product != null ? 'update' : 'add'}">
					<c:if test="${product != null}">
						<input type="hidden" name="productId" value="${product.productId}">
						<input type="hidden" name="existing_image_url"
							value="${product.imageUrl}">
					</c:if>

					<div class="form-group">
						<label for="watchName">Watch Name</label> <input type="text"
							id="watchName" name="name" class="form-control" required
							value="${product != null ? product.name : ''}">
					</div>

					<div class="form-row">
						<div class="form-group">
							<label for="brand">Brand</label> <input type="text" id="brand"
								name="brand" class="form-control" required
								value="${product != null ? product.brand : ''}">
						</div>
						<div class="form-group">
							<label for="price">Price ($)</label> <input type="number"
								id="price" name="price" class="form-control" min="0" step="0.01"
								required value="${product != null ? product.price : ''}">
						</div>
					</div>

					<div class="form-row">
						<div class="form-group">
							<label for="stock">Stock Quantity</label> <input type="number"
								id="stock" name="stock_quantity" class="form-control" min="0"
								required value="${product != null ? product.stockQuantity : ''}">
						</div>
						<div class="form-group">
							<label for="category">Category</label> <select id="category"
								name="category" class="form-control" required>
								<option value="">Select Category</option>
								<option value="luxury"
									${product != null && product.category == 'luxury' ? 'selected' : ''}>Luxury</option>
								<option value="sport"
									${product != null && product.category == 'sport' ? 'selected' : ''}>Sport</option>
								<option value="smart"
									${product != null && product.category == 'smart' ? 'selected' : ''}>Smart
									Watch</option>
								<option value="classic"
									${product != null && product.category == 'classic' ? 'selected' : ''}>Classic</option>
								<option value="dive"
									${product != null && product.category == 'dive' ? 'selected' : ''}>Dive
									Watch</option>
							</select>
						</div>
					</div>

					<div class="form-group">
						<label for="description">Description</label>
						<textarea id="description" name="description" class="form-control"
							rows="4" required>${product != null ? product.description : ''}</textarea>
					</div>

					<div class="form-row">
						<div class="form-group">
							<label for="material">Case Material</label> <select id="material"
								name="material" class="form-control" required>
								<option value="Stainless Steel"
									${product != null && product.caseMaterial == 'Stainless Steel' ? 'selected' : ''}>Stainless
									Steel</option>
								<option value="Titanium"
									${product != null && product.caseMaterial == 'Titanium' ? 'selected' : ''}>Titanium</option>
								<option value="Plastic"
									${product != null && product.caseMaterial == 'Plastic' ? 'selected' : ''}>Plastic</option>
								<option value="Ceramic"
									${product != null && product.caseMaterial == 'Ceramic' ? 'selected' : ''}>Ceramic</option>
								<option value="Carbon Fiber"
									${product != null && product.caseMaterial == 'Carbon Fiber' ? 'selected' : ''}>Carbon
									Fiber</option>
							</select>
						</div>
						<div class="form-group">
							<label for="movement">Movement Type</label> <select id="movement"
								name="movement" class="form-control" required>
								<option value="Automatic"
									${product != null && product.movementType == 'Automatic' ? 'selected' : ''}>Automatic</option>
								<option value="Quartz"
									${product != null && product.movementType == 'Quartz' ? 'selected' : ''}>Quartz</option>
								<option value="Mechanical"
									${product != null && product.movementType == 'Mechanical' ? 'selected' : ''}>Mechanical</option>
								<option value="Solar"
									${product != null && product.movementType == 'Solar' ? 'selected' : ''}>Solar</option>
								<option value="Smart"
									${product != null && product.movementType == 'Smart' ? 'selected' : ''}>Smart</option>
							</select>
						</div>
					</div>

					<div class="form-group">
						<label for="watchImage">Watch Image ${product == null ? '(Required)' : '(Optional)'}</label>
						<input type="file" id="watchImage" name="watchImage"
							class="form-control-file" accept="image/*"
							${product == null ? 'required' : ''}>
						<c:if test="${product != null && product.imageUrl != null}">
							<div class="image-preview">
								<img src="${product.imageUrl}" alt="Current Image"
									class="product-img">
							</div>
						</c:if>
					</div>

					<div class="form-row">
						<div class="form-group">
							<label for="waterResistance">Water Resistance (meters)</label> <input
								type="number" id="waterResistance" name="water_resistance"
								class="form-control" min="0"
								value="${product != null ? product.waterResistanceMeters : ''}">
						</div>
						<div class="form-group">
							<label for="warrantyPeriod">Warranty (months)</label> <input
								type="number" id="warrantyPeriod" name="warranty_months"
								class="form-control" min="0"
								value="${product != null ? product.warrantyMonths : ''}">
						</div>
					</div>

					<div class="form-actions">
						<a href="${pageContext.request.contextPath}/admin/product"
							class="btn btn-secondary">Cancel</a>
						<button type="submit" class="btn btn-primary">${product != null ? 'Update Watch' : 'Add Watch'}</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script
		src="${pageContext.request.contextPath}/assets/js/add-watch.js"></script>
</body>
</html>