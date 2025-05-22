<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/contact-message.css">

<div class="admin-message-detail">
	<div class="card">
		<div
			class="card-header d-flex justify-content-between align-items-center">
			<h1>Message Details</h1>
			<div class="action-buttons">
				<a href="${pageContext.request.contextPath}/admin/contact-messages"
					class="btn btn-secondary"> <i class="fas fa-arrow-left"></i>
					Back to List
				</a> <a href="#"
					onclick="confirmDelete(${message.messageId}); return false;"
					class="btn btn-danger"> <i class="fas fa-trash"></i> Delete
					Message
				</a>
			</div>
		</div>

		<div class="card-body">
			<div class="row">
				<div class="col-md-6">
					<div class="message-info">
						<h3>Contact Information</h3>
						<table class="table">
							<tbody>
								<tr>
									<th>Message ID:</th>
									<td>${message.messageId}</td>
								</tr>
								<tr>
									<th>Name:</th>
									<td>${message.name}</td>
								</tr>
								<tr>
									<th>Email:</th>
									<td><a href="mailto:${message.email}" class="email-link">
											${message.email} </a></td>
								</tr>
								<tr>
									<th>User ID:</th>
									<td><c:choose>
											<c:when test="${message.userId > 0}">
												<a
													href="${pageContext.request.contextPath}/admin/users?action=view&id=${message.userId}">
													${message.userId} (View User) </a>
											</c:when>
											<c:otherwise>
                                                Not Registered
                                            </c:otherwise>
										</c:choose></td>
								</tr>
								<tr>
									<th>Date Submitted:</th>
									<td><fmt:parseDate value="${message.submittedAt}"
											pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" type="both" />
										<fmt:formatDate value="${parsedDate}"
											pattern="MMMM d, yyyy 'at' HH:mm:ss" /></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<div class="col-md-6">
					<div class="message-content">
						<h3>Message Content</h3>
						<div class="card">
							<div class="card-header">
								<strong>Subject:</strong> ${message.subject}
							</div>
							<div class="card-body">
								<p class="message-text">${message.message}</p>
							</div>
						</div>
					</div>

					<div class="reply-section mt-4">
						<h3>Quick Reply</h3>
						<div class="card">
							<div class="card-body">
								<form action="mailto:${message.email}" method="post"
									enctype="text/plain">
									<input type="hidden" name="subject"
										value="RE: ${message.subject}">
									<div class="form-group">
										<textarea class="form-control" rows="5"
											placeholder="Type your reply here..."></textarea>
									</div>
									<button type="submit" class="btn btn-primary">
										<i class="fas fa-paper-plane"></i> Send Reply
									</button>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Delete Confirmation Modal -->
	<div class="custom-modal" id="deleteModal" aria-hidden="true">
		<div class="custom-modal-content">
			<div class="custom-modal-header">
				<h5 class="custom-modal-title">Confirm Delete</h5>
				<button type="button" class="custom-modal-close"
					onclick="closeModal('deleteModal')">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="custom-modal-body">Are you sure you want to delete
				this message from ${message.name}? This action cannot be undone.</div>
			<div class="custom-modal-footer">
				<button type="button" class="btn btn-secondary"
					onclick="closeModal('deleteModal')">Cancel</button>
				<a
					href="${pageContext.request.contextPath}/admin/contact-messages?action=delete&id=${message.messageId}"
					class="btn btn-danger">Delete</a>
			</div>
		</div>
	</div>
</div>

<script>
    function confirmDelete(messageId) {
        document.getElementById('deleteModal').classList.add('show');
    }

    function closeModal(modalId) {
        document.getElementById(modalId).classList.remove('show');
    }
</script>