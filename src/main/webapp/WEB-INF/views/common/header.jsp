<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
    <div class="container">
        <!-- Checkbox for mobile menu toggle -->
        <input type="checkbox" id="menu-toggle-checkbox" class="menu-toggle-checkbox" hidden>
        <div class="header-wrapper">
            <!-- Logo -->
            <div class="logo">
                <a href="${pageContext.request.contextPath}/">Wis<span>toria</span></a>
            </div>

            <!-- Search Bar -->
            <div class="search-wrapper">
                <div class="search-bar">
                    <form action="${pageContext.request.contextPath}/search" method="get">
                        <input type="text" name="query" placeholder="Search watches...">
                        <button type="submit" name="search"><i class="fa fa-search"></i></button>
                    </form>
                </div>
            </div>

            <!-- Navigation -->
            <nav class="main-nav">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/contact">Contact Us</a></li>
                </ul>
            </nav>

            <!-- User Actions -->
            <div class="user-actions">
                <!-- Profile Dropdown -->
                <div class="profile-dropdown">
                    <button class="profile-btn" aria-label="User menu"><i class="fa fa-user-circle"></i></button>
                    <div class="dropdown-content">
                        <c:choose>
                            <c:when test="${empty sessionScope.user}">
                                <a href="${pageContext.request.contextPath}/login">Login</a>
                                <a href="${pageContext.request.contextPath}/register">Signup</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/profile">My Profile</a>
                                <a href="${pageContext.request.contextPath}/logout">Logout</a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Wishlist -->
                <a href="${pageContext.request.contextPath}/user/wishlist" class="wishlist-icon" title="Wishlist">
                    <i class="fa fa-heart"></i>
                </a>

                <!-- Cart -->
                <a href="${pageContext.request.contextPath}/user/cart" class="cart-icon" title="Cart">
                    <i class="fa fa-shopping-cart"></i>
                    <span class="cart-count"><c:out value="${sessionScope.cartCount}" default="0"/></span>
                </a>
            </div>

            <!-- Mobile Menu Toggle -->
            <label for="menu-toggle-checkbox" class="menu-toggle" aria-label="Toggle menu">
                <i class="fa fa-bars"></i>
            </label>
        </div>

        <!-- Mobile Menu -->
        <div class="mobile-menu">
            <div class="mobile-menu-header">
                <div class="logo">
                    <a href="${pageContext.request.contextPath}/">Wis<span>toria</span></a>
                </div>
                <label for="menu-toggle-checkbox" class="close-menu" aria-label="Close menu">
                    <i class="fa fa-times"></i>
                </label>
            </div>
            <div class="mobile-search">
                <div class="search-bar">
                    <form action="${pageContext.request.contextPath}/search" method="get">
                        <input type="text" name="query" placeholder="Search watches...">
                        <button type="submit" name="search"><i class="fa fa-search"></i></button>
                    </form>
                </div>
            </div>
            <nav class="mobile-nav">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/contact">Contact Us</a></li>
                </ul>
            </nav>
            <div class="mobile-actions">
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/login"><i class="fa fa-sign-in"></i> Login</a>
                        <a href="${pageContext.request.contextPath}/register"><i class="fa fa-user-plus"></i> Signup</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/profile"><i class="fa fa-user"></i> My Profile</a>
                        <a href="${pageContext.request.contextPath}/user/wishlist"><i class="fa fa-heart"></i> My Wishlist</a>
                        <a href="${pageContext.request.contextPath}/user/cart"><i class="fa fa-shopping-cart"></i> Shopping Cart</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Overlay -->
        <label for="menu-toggle-checkbox" class="overlay" aria-label="Close menu"></label>
    </div>
</header>