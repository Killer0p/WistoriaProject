document.addEventListener('DOMContentLoaded', function() {
	// Handle toggle for order details
	const toggleButtons = document.querySelectorAll('.toggle-details');

	toggleButtons.forEach(button => {
		button.addEventListener('click', function() {
			const orderId = this.getAttribute('data-order');
			const detailsRow = document.querySelector(`#details-${orderId}`).closest('.order-details-row');

			// Close all other open detail rows
			document.querySelectorAll('.order-details-row').forEach(row => {
				if (row !== detailsRow && row.style.display === 'table-row') {
					row.style.display = 'none';
					const otherButton = document.querySelector(`.toggle-details[data-order="${row.querySelector('.order-details').id.replace('details-', '')}"]`);
					if (otherButton) otherButton.textContent = 'View Details';
				}
			});

			// Toggle the clicked row
			if (detailsRow) {
				if (detailsRow.style.display === 'none' || detailsRow.style.display === '') {
					detailsRow.style.display = 'table-row';
					this.textContent = 'Hide Details';
				} else {
					detailsRow.style.display = 'none';
					this.textContent = 'View Details';
				}
			}
		});
	});

	// Handle status update confirmation
	const statusSelects = document.querySelectorAll('.status-select');
	statusSelects.forEach(select => {
		select.addEventListener('change', function(event) {
			if (!confirm('Are you sure you want to update this order status?')) {
				event.preventDefault();
				this.selectedIndex = 0; // Reset to default option
			}
		});
	});
});