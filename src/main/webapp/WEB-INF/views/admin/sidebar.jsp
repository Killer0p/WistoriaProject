<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="sidebar" id="sidebar">
	<div class="sidebar-header">
		<h2>
			<i class="fas fa-chart-bar"></i> <span class="menu-text">Admin
				Panel</span>
		</h2>
		<!-- Chevron toggle button -->
		<button id="sidebar-toggle" class="chevron-toggle">
			<i class="fas fa-chevron-right"></i>
		</button>
	</div>
	<div class="sidebar-menu">
		<ul>
			<li class="${activeNavItem == 'dashboard' ? 'active' : ''}"><a
				href="${pageContext.request.contextPath}/admin/dashboard"
				title="Dashboard"> <i class="fas fa-th-large"></i> <span
					class="menu-text">Dashboard</span>
			</a></li>
			<li class="${activeNavItem == 'product' ? 'active' : ''}"><a
				href="${pageContext.request.contextPath}/admin/product"
				title="Products"> <i class="fas fa-box"></i> <span
					class="menu-text">Products</span>
			</a></li>
			<li class="${activeNavItem == 'order' ? 'active' : ''}"><a
				href="${pageContext.request.contextPath}/admin/order" title="Orders">
					<i class="fas fa-clipboard-list"></i> <span class="menu-text">Orders</span>
			</a></li>
			<li class="${activeNavItem == 'profile' ? 'active' : ''}"><a
				href="${pageContext.request.contextPath}/admin/contact-messages" title="Contact Messages">
					<i class="fas fa-clipboard-list"></i> <span class="menu-text">Contact Messages</span>
			</a></li>
			<li class="${activeNavItem == 'profile' ? 'active' : ''}"><a
				href="${pageContext.request.contextPath}/profile" title="Profile">
					<i class="fas fa-clipboard-list"></i> <span class="menu-text">Profile</span>
			</a></li>
			<li
				class="sidebar-bottom ${activeNavItem == 'logout' ? 'active' : ''}">
				<a href="${pageContext.request.contextPath}/logout" title="Logout">
					<i class="fas fa-sign-out-alt"></i> <span class="menu-text">Logout</span>
			</a>
			</li>
		</ul>
	</div>
</div>

<script>
	document.addEventListener('DOMContentLoaded', function() {
		// Toggle sidebar on chevron button click
		const sidebarToggle = document.getElementById('sidebar-toggle');
		const sidebar = document.getElementById('sidebar');
		const chevronIcon = sidebarToggle.querySelector('i');

		if (sidebarToggle && sidebar && chevronIcon) {
			sidebarToggle.addEventListener('click', function() {
				sidebar.classList.toggle('collapsed');
				document.body.classList.toggle('sidebar-collapsed');
				// Switch chevron direction
				if (sidebar.classList.contains('collapsed')) {
					chevronIcon.classList.remove('fa-chevron-left');
					chevronIcon.classList.add('fa-chevron-right');
				} else {
					chevronIcon.classList.remove('fa-chevron-right');
					chevronIcon.classList.add('fa-chevron-left');
				}
			});
		}

		// Handle responsiveness
		function handleResize() {
			if (window.innerWidth <= 480) {
				// Mobile view: Bottom navigation
				sidebar.classList.add('mobile');
				sidebar.classList.remove('collapsed');
				document.body.classList.remove('sidebar-collapsed');
				sidebarToggle.style.display = 'none'; // Hide chevron
			} else if (window.innerWidth <= 992) {
				// Tablet view: Collapsed (icons only), no chevron by default
				sidebar.classList.remove('mobile');
				sidebar.classList.add('collapsed');
				document.body.classList.add('sidebar-collapsed');
				sidebarToggle.style.display = 'none'; // Hide chevron in collapsed state
				chevronIcon.classList.remove('fa-chevron-left');
				chevronIcon.classList.add('fa-chevron-right');
			} else {
				// Desktop view: Full sidebar
				sidebar.classList.remove('mobile');
				sidebar.classList.remove('collapsed');
				document.body.classList.remove('sidebar-collapsed');
				sidebarToggle.style.display = 'block'; // Show chevron
				chevronIcon.classList.remove('fa-chevron-right');
				chevronIcon.classList.add('fa-chevron-left');
			}
		}

		// Run on page load and resize
		handleResize();
		window.addEventListener('resize', handleResize);

		// Show chevron when sidebar is expanded in tablet view
		sidebar.addEventListener('click', function() {
			if (window.innerWidth > 480 && window.innerWidth <= 992) {
				if (!sidebar.classList.contains('collapsed')) {
					sidebarToggle.style.display = 'block';
					chevronIcon.classList.remove('fa-chevron-right');
					chevronIcon.classList.add('fa-chevron-left');
				} else {
					sidebarToggle.style.display = 'none';
					chevronIcon.classList.remove('fa-chevron-left');
					chevronIcon.classList.add('fa-chevron-right');
				}
			}
		});
	});
</script>