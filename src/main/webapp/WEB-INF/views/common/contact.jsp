<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/contact.css">
<!-- Add Font Awesome for icons -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<div class="contact-page-wrapper">
	<h1 class="page-title">Contact Us</h1>

	<div class="card">
		<div class="contact-container">
			<!-- Contact Information -->
			<div class="contact-section info-section">
				<h3>Get In Touch</h3>
				<div class="contact-info">
					<div class="info-item">
						<i class="fas fa-map-marker-alt"></i>
						<div>
							<h4>Our Location</h4>
							<p>
								123 Business Avenue, Suite 500<br>San Francisco, CA 94107
							</p>
						</div>
					</div>
					<div class="info-item">
						<i class="fas fa-phone"></i>
						<div>
							<h4>Phone Number</h4>
							<p>+1 (555) 123-4567</p>
						</div>
					</div>
					<div class="info-item">
						<i class="fas fa-envelope"></i>
						<div>
							<h4>Email Address</h4>
							<p>support@wistoriawatches.com</p>
						</div>
					</div>
					<div class="info-item">
						<i class="fas fa-clock"></i>
						<div>
							<h4>Business Hours</h4>
							<p>
								Monday - Friday: 9AM - 5PM<br>Saturday: 10AM - 2PM
							</p>
						</div>
					</div>
				</div>
				<div class="social-links">
					<a href="#" class="social-icon" aria-label="Facebook"> <i
						class="fab fa-facebook-f"></i>
					</a> <a href="#" class="social-icon" aria-label="Twitter"> <i
						class="fab fa-twitter"></i>
					</a> <a href="#" class="social-icon" aria-label="LinkedIn"> <i
						class="fab fa-linkedin-in"></i>
					</a> <a href="#" class="social-icon" aria-label="Instagram"> <i
						class="fab fa-instagram"></i>
					</a>
				</div>
			</div>

			<!-- Contact Form -->
			<div class="contact-section form-section">
				<h3>Send Us a Message</h3>
				<p class="form-description">Have a question about our watches or
					need assistance? Fill out the form below and our team will get back
					to you as soon as possible.</p>

				<form action="${pageContext.request.contextPath}/contact"
					method="post" id="contactForm">
					<div class="form-group">
						<label for="name">Full Name <span class="required">*</span></label>
						<input type="text" id="name" name="name"
							placeholder="Your full name"
							value="<c:out value='${param.name}'/>"
							<c:if test="${not empty user}">value="<c:out value='${user.name}'/>"</c:if>
							required>
					</div>

					<div class="form-group">
						<label for="email">Email Address <span class="required">*</span></label>
						<input type="email" id="email" name="email"
							placeholder="your.email@example.com"
							value="<c:out value='${param.email}'/>"
							<c:if test="${not empty user}">value="<c:out value='${user.email}'/>"</c:if>
							required>
					</div>

					<div class="form-group">
						<label for="subject">Subject <span class="required">*</span></label>
						<input type="text" id="subject" name="subject"
							placeholder="What is your message about?"
							value="<c:out value='${param.subject}'/>" required>
					</div>

					<div class="form-group">
						<label for="message">Message <span class="required">*</span></label>
						<textarea id="message" name="message" rows="5"
							placeholder="Please write your message here..." required><c:out
								value='${param.message}' /></textarea>
					</div>

					<button type="submit" class="btn btn-primary">
						<i class="fas fa-paper-plane"></i> Send Message
					</button>
				</form>
			</div>
		</div>
	</div>
</div>

<script>
    // Enhanced client-side validation for contact form
    document.getElementById('contactForm').addEventListener('submit', function(e) {
        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const subject = document.getElementById('subject').value;
        const message = document.getElementById('message').value;
        let hasError = false;
        
        // Clear previous errors
        document.querySelectorAll('.form-error').forEach(el => el.remove());
        
        // Check empty fields
        if (!name.trim() || !email.trim() || !subject.trim() || !message.trim()) {
            e.preventDefault();
            
            // Show error for each empty field
            if (!name.trim()) addError('name', 'Please enter your name');
            if (!email.trim()) addError('email', 'Please enter your email address');
            if (!subject.trim()) addError('subject', 'Please enter a subject');
            if (!message.trim()) addError('message', 'Please enter your message');
            
            hasError = true;
        }
        
        // Email validation
        const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
        if (email.trim() && !email.match(emailRegex)) {
            e.preventDefault();
            addError('email', 'Please enter a valid email address');
            hasError = true;
        }
        
        // Prevent page jumping if there are errors
        if (hasError) {
            // Focus the first field with error
            const firstErrorField = document.querySelector('.form-error');
            if (firstErrorField) {
                firstErrorField.previousElementSibling.focus();
            }
        }
    });
    
    // Helper function to add error messages
    function addError(fieldId, message) {
        const field = document.getElementById(fieldId);
        const errorDiv = document.createElement('div');
        errorDiv.className = 'form-error';
        errorDiv.style.color = 'var(--danger-color)';
        errorDiv.style.fontSize = '0.85rem';
        errorDiv.style.marginTop = '0.5rem';
        errorDiv.textContent = message;
        field.parentNode.appendChild(errorDiv);
        
        // Highlight the field
        field.style.borderColor = 'var(--danger-color)';
        
        // Remove highlight on input
        field.addEventListener('input', function() {
            this.style.borderColor = '';
            const error = this.parentNode.querySelector('.form-error');
            if (error) error.remove();
        });
    }
    
    // Add visual feedback when form is valid
    const formInputs = document.querySelectorAll('#contactForm input, #contactForm textarea');
    formInputs.forEach(input => {
        input.addEventListener('input', function() {
            if (this.validity.valid) {
                this.style.borderColor = 'var(--success-color)';
            }
        });
    });
</script>