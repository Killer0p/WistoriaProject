document.addEventListener('DOMContentLoaded', function() {
    // Target the message modal
    const msgModal = document.getElementById('msgModal');

    if (msgModal) {
        // Check if modal has content (success or error message)
        const hasContent = msgModal.querySelector('.msgModal_success, .msgModal_error');

        if (hasContent) {
            // Ensure modal is visible
            msgModal.style.display = 'block';

            // Auto-hide modal after 5 seconds
            const modalTimeout = setTimeout(() => {
                msgModal.style.display = 'none';
            }, 5000);

            // Clear timeout if manually closed
            const closeButton = msgModal.querySelector('.msgModal-close');
            if (closeButton) {
                closeButton.addEventListener('click', () => {
                    clearTimeout(modalTimeout);
                    msgModal.style.display = 'none';
                });
            }
        }
    }

    // Target inline messages (e.g., in home.jsp, product-detail.jsp)
    const inlineMessages = document.querySelectorAll('.message.success, .message.error');

    inlineMessages.forEach(message => {
        // Ensure message is visible
        message.style.display = 'flex';

        // Auto-hide message after 5 seconds
        setTimeout(() => {
            message.style.display = 'none';
        }, 5000);
    });
});