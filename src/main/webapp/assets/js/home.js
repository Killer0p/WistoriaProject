// Placeholder functions for demonstration
function generateWishlistHTML() {
    return '<p>Sample wishlist content (replace with actual data).</p>';
}

function generateOrdersHTML() {
    return '<p>Sample orders content (replace with actual data).</p>';
}

// Tab functionality for Recent Activity section (if present)
document.addEventListener('DOMContentLoaded', function() {
    const tabs = document.querySelectorAll('.tab');

    if (tabs.length > 0) {
        tabs.forEach(tab => {
            tab.addEventListener('click', function() {
                // Remove active class from all tabs
                tabs.forEach(t => t.classList.remove('active'));

                // Add active class to clicked tab
                this.classList.add('active');

                // Load tab content (placeholder for AJAX)
                const tabType = this.getAttribute('data-tab');
                console.log(`Loading ${tabType} data...`);

                const recentList = document.querySelector('.recent-list');
                if (recentList) {
                    if (tabType === 'wishlist') {
                        recentList.innerHTML = generateWishlistHTML();
                    } else if (tabType === 'orders') {
                        recentList.innerHTML = generateOrdersHTML();
                    } else {
                        location.reload(); // Reset to recently viewed
                    }
                }
            });
        });
    }

});