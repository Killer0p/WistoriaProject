<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section class="newsletter">
    <div class="container">
        <div class="section-title">
            <h2>Stay Updated</h2>
            <p>Subscribe to our newsletter for exclusive offers and updates</p>
        </div>
        <!-- Form submits to a servlet for processing -->
        <form class="newsletter-form" action="${pageContext.request.contextPath}/subscribe" method="post">
            <input type="email" name="email" placeholder="Enter your email address" required>
            <button type="submit">Subscribe</button>
        </form>
    </div>
</section>