document.addEventListener('DOMContentLoaded', function () {
    // Profile dropdown click handler for touch devices
    const profileBtn = document.querySelector('.profile-btn');
    const dropdownContent = document.querySelector('.dropdown-content');

    if (profileBtn && dropdownContent) {
        profileBtn.addEventListener('click', function (e) {
            e.preventDefault();
            dropdownContent.classList.toggle('active');
        });

        // Close dropdown when clicking outside
        document.addEventListener('click', function (e) {
            if (!profileBtn.contains(e.target) && !dropdownContent.contains(e.target)) {
                dropdownContent.classList.remove('active');
            }
        });
    }
});