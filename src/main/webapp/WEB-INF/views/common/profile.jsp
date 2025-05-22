<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/profile.css">

<main class="profile-main">
	<h1 class="page-title">My Profile</h1>

	<div class="card">
		<div class="profile-container">
			<!-- Profile Information Form -->
			<div class="profile-section">
				<h3>Profile Information</h3>
				<form action="${pageContext.request.contextPath}/profile"
					method="post" id="profileForm">
					<input type="hidden" name="action" value="updateProfile">
					<div class="form-group">
						<label for="name">Full Name</label> <input type="text" id="name"
							name="name" value="<c:out value='${user.name}' />" required>
					</div>
					<div class="form-group">
						<label for="email">Email Address</label> <input type="email"
							id="email" value="<c:out value='${user.email}' />" disabled>
					</div>
					<div class="form-group">
						<label for="phoneNumber">Phone Number</label> <input type="tel"
							id="phoneNumber" name="phoneNumber"
							value="<c:out value='${user.phoneNumber}' />"
							pattern="^\+?[1-9]\d{1,14}$" placeholder="+1234567890">
					</div>
					<div class="form-group">
						<label for="role">Role</label> <input type="text" id="role"
							value="<c:out value='${user.role}' />" disabled>
					</div>
					<button type="submit" class="btn btn-primary">Update
						Profile</button>
				</form>
			</div>

			<!-- Password Update Form -->
			<div class="profile-section">
				<h3>Change Password</h3>
				<form action="${pageContext.request.contextPath}/profile"
					method="post" id="passwordForm">
					<input type="hidden" name="action" value="updatePassword">
					<div class="form-group">
						<label for="currentPassword">Current Password</label> <input
							type="password" id="currentPassword" name="currentPassword"
							required>
					</div>
					<div class="form-group">
						<label for="newPassword">New Password</label> <input
							type="password" id="newPassword" name="newPassword" required
							minlength="8">
					</div>
					<div class="form-group">
						<label for="confirmPassword">Confirm New Password</label> <input
							type="password" id="confirmPassword" name="confirmPassword"
							required minlength="8">
					</div>
					<button type="submit" class="btn btn-primary">Update
						Password</button>
				</form>
			</div>
		</div>
	</div>
</main>

<script>
	// Client-side validation for profile form
	document
			.getElementById('profileForm')
			.addEventListener(
					'submit',
					function(e) {
						const name = document.getElementById('name').value;
						const phoneNumber = document
								.getElementById('phoneNumber').value;
						if (!name.trim()) {
							e.preventDefault();
							alert('Name is required.');
							return;
						}
						if (phoneNumber
								&& !phoneNumber.match(/^\+?[1-9]\d{1,14}$/)) {
							e.preventDefault();
							alert('Phone number must be in a valid international format (e.g., +1234567890).');
							return;
						}
					});

	// Client-side validation for password form
	document
			.getElementById('passwordForm')
			.addEventListener(
					'submit',
					function(e) {
						const newPassword = document
								.getElementById('newPassword').value;
						const confirmPassword = document
								.getElementById('confirmPassword').value;
						if (newPassword.length < 8) {
							e.preventDefault();
							alert('New password must be at least 8 characters long.');
							return;
						}
						if (newPassword !== confirmPassword) {
							e.preventDefault();
							alert('New password and confirmation do not match.');
							return;
						}
					});
</script>