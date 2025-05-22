<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/contact-message.css">

<div class="admin-contact-messages">
    <h1>Contact Messages</h1>
    
    <!-- Success and Error Messages -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">
            <p>${sessionScope.success}</p>
            <c:remove var="success" scope="session" />
        </div>
    </c:if>
    
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">
            <p>${sessionScope.error}</p>
            <c:remove var="error" scope="session" />
        </div>
    </c:if>
    
    <c:if test="${not empty requestScope.error}">
        <div class="alert alert-danger">
            <p>${requestScope.error}</p>
        </div>
    </c:if>

    <!-- Message Counter -->
    <div class="message-counter">
        <span>${totalMessages} Total Messages</span>
    </div>

    <!-- Messages Table -->
    <div class="table-responsive">
        <table class="table message-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Subject</th>
                    <th>Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty messages}">
                        <c:forEach var="message" items="${messages}">
                            <tr>
                                <td>${message.messageId}</td>
                                <td>${message.name}</td>
                                <td>
                                    <a href="mailto:${message.email}" class="email-link">
                                        ${message.email}
                                    </a>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${fn:length(message.subject) > 40}">
                                            ${fn:substring(message.subject, 0, 40)}...
                                        </c:when>
                                        <c:otherwise>
                                            ${message.subject}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <fmt:parseDate value="${message.submittedAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" type="both" />
                                    <fmt:formatDate value="${parsedDate}" pattern="MMM d, yyyy HH:mm" />
                                </td>
                                <td class="action-buttons">
                                    <a href="${pageContext.request.contextPath}/admin/contact-messages?action=view&id=${message.messageId}" 
                                       class="btn btn-sm btn-primary">
                                        <i class="fas fa-eye"></i> View
                                    </a>
                                    <a href="#" onclick="confirmDelete(${message.messageId}); return false;" 
                                       class="btn btn-sm btn-danger">
                                        <i class="fas fa-trash"></i> Delete
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" class="text-center">No messages found</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <!-- Pagination -->
    <c:if test="${totalPages > 1}">
        <div class="pagination-container">
            <ul class="pagination">
                <c:if test="${currentPage > 1}">
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}/admin/contact-messages?page=${currentPage - 1}">
                            <i class="fas fa-chevron-left"></i> Previous
                        </a>
                    </li>
                </c:if>
                
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/admin/contact-messages?page=${i}">${i}</a>
                    </li>
                </c:forEach>
                
                <c:if test="${currentPage < totalPages}">
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}/admin/contact-messages?page=${currentPage + 1}">
                            Next <i class="fas fa-chevron-right"></i>
                        </a>
                    </li>
                </c:if>
            </ul>
        </div>
    </c:if>

    <!-- Delete Confirmation Modal -->
    <div class="custom-modal" id="deleteModal" aria-hidden="true">
        <div class="custom-modal-content">
            <div class="custom-modal-header">
                <h5 class="custom-modal-title">Confirm Delete</h5>
                <button type="button" class="custom-modal-close" onclick="closeModal('deleteModal')">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="custom-modal-body">
                Are you sure you want to delete this message? This action cannot be undone.
            </div>
            <div class="custom-modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal('deleteModal')">Cancel</button>
                <a href="#" id="confirmDeleteBtn" class="btn btn-danger">Delete</a>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmDelete(messageId) {
        var deleteUrl = '${pageContext.request.contextPath}/admin/contact-messages?action=delete&id=' + messageId;
        document.getElementById('confirmDeleteBtn').href = deleteUrl;
        document.getElementById('deleteModal').classList.add('show');
    }

    function closeModal(modalId) {
        document.getElementById(modalId).classList.remove('show');
    }
</script>