<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="msgModal" class="msgModal" style="display: none;">
    <div class="msgModal-content">
        <button class="msgModal-close" onclick="document.getElementById('msgModal').style.display='none'">×</button>
        <c:if test="${not empty success}">
            <div class="msgModal_success">
                <i class="fas fa-check-circle"></i> ${success}
            </div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${not empty error}">
            <div class="msgModal_error">
                <i class="fas fa-exclamation-circle"></i> ${error}
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="msgModal_error">
                <i class="fas fa-exclamation-circle"></i> ${errorMessage}
            </div>
            <c:remove var="errorMessage" scope="session"/>
        </c:if>
    </div>
</div>