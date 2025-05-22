<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<footer>
	<div class="container">
		<div class="footer-grid">
			<div class="footer-col">
				<h3>About Wistoria</h3>
				<p>Wistoria is a premium watch retailer dedicated to providing
					exceptional timepieces for watch enthusiasts. We curate collections
					from the finest brands worldwide.</p>
				<div class="social-links">
					<!-- Update with real URLs or keep as placeholders -->
					<a href="https://facebook.com/wistoria"><i
						class="fab fa-facebook-f"></i></a> <a
						href="https://twitter.com/wistoria"><i class="fab fa-twitter"></i></a>
					<a href="https://instagram.com/wistoria"><i
						class="fab fa-instagram"></i></a> <a
						href="https://pinterest.com/wistoria"><i
						class="fab fa-pinterest"></i></a>
				</div>
			</div>

			<div class="footer-col">
				<h3>Quick Links</h3>
				<ul class="footer-links">
					<li><a href="${pageContext.request.contextPath}/">Home</a></li>
					<li><a href="${pageContext.request.contextPath}/about">About
							Us</a></li>
					<li><a href="${pageContext.request.contextPath}/shop">Shop</a></li>
					<li><a href="${pageContext.request.contextPath}/collections">Collections</a></li>
					<li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
				</ul>
			</div>

			<div class="footer-col">
				<h3>Customer Service</h3>
				<ul class="footer-links">
					<li><a href="${pageContext.request.contextPath}/account">My
							Account</a></li>
					<li><a
						href="${pageContext.request.contextPath}/order-tracking">Order
							Tracking</a></li>
					<li><a href="${pageContext.request.contextPath}/wishlist">Wishlist</a></li>
					<li><a href="${pageContext.request.contextPath}/returns">Returns
							& Exchanges</a></li>
					<li><a href="${pageContext.request.contextPath}/faqs">FAQs</a></li>
				</ul>
			</div>

			<div class="footer-col">
				<h3>Contact Us</h3>
				<ul class="contact-info">
					<li><i class="fas fa-map-marker-alt"></i> <span>123
							Watch Street, Timepiece City, TC 12345</span></li>
					<li><i class="fas fa-phone"></i> <span>+1 (555)
							123-4567</span></li>
					<li><i class="fas fa-envelope"></i> <span>info@wistoria.com</span></li>
					<li><i class="fas fa-clock"></i> <span>Mon-Fri: 9AM to
							6PM</span></li>
				</ul>
			</div>
		</div>

		<div class="footer-bottom">
			<p>
				© ${java.time.Year.now().value} Wistoria. All Rights Reserved.
				Designed with <i class="fas fa-heart" style="color: #e74c3c;"></i>
				for watch lovers.
			</p>
		</div>
	</div>
</footer>