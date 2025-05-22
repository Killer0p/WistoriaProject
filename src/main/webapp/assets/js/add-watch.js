document.addEventListener('DOMContentLoaded', function() {
	const form = document.getElementById('addWatchForm');
	const imageInput = document.getElementById('watchImage');
	const imagePreview = document.querySelector('.image-preview');

	// Image preview
	if (imageInput) {
		imageInput.addEventListener('change', function(event) {
			const file = event.target.files[0];
			if (file) {
				const reader = new FileReader();
				reader.onload = function(e) {
					if (imagePreview) {
						imagePreview.innerHTML = `<img src="${e.target.result}" alt="Preview" class="product-img">`;
					} else {
						const newPreview = document.createElement('div');
						newPreview.className = 'image-preview';
						newPreview.innerHTML = `<img src="${e.target.result}" alt="Preview" class="product-img">`;
						imageInput.parentNode.appendChild(newPreview);
					}
				};
				reader.readAsDataURL(file);
			}
		});
	}

	// Form validation
	if (form) {
		form.addEventListener('submit', function(event) {
			const price = document.getElementById('price').value;
			const stock = document.getElementById('stock').value;
			const waterResistance = document.getElementById('waterResistance').value;
			const warranty = document.getElementById('warrantyPeriod').value;

			if (price < 0) {
				alert('Price cannot be negative.');
				event.preventDefault();
				return;
			}
			if (stock < 0) {
				alert('Stock quantity cannot be negative.');
				event.preventDefault();
				return;
			}
			if (waterResistance < 0) {
				alert('Water resistance cannot be negative.');
				event.preventDefault();
				return;
			}
			if (warranty < 0) {
				alert('Warranty period cannot be negative.');
				event.preventDefault();
				return;
			}
		});
	}
});