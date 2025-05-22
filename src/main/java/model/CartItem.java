package model;

import java.time.LocalDateTime;

public class CartItem {
	private Integer cartItemId;
	private Integer userId;
	private Integer productId;
	private Integer quantity;
	private LocalDateTime addedAt;
	private Boolean isDeleted;

	// Reference objects (not part of table but useful in business logic)
	private User user;
	private Product product;

	// Constructors
	public CartItem() {
	}

	public CartItem(Integer cartItemId, Integer userId, Integer productId, Integer quantity, LocalDateTime addedAt,
			Boolean isDeleted) {
		this.cartItemId = cartItemId;
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;
		this.addedAt = addedAt;
		this.isDeleted = isDeleted;
	}

	// Getters and Setters
	public Integer getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(Integer cartItemId) {
		this.cartItemId = cartItemId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getAddedAt() {
		return addedAt;
	}

	public void setAddedAt(LocalDateTime addedAt) {
		this.addedAt = addedAt;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		if (user != null) {
			this.userId = user.getUserId();
		}
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
		if (product != null) {
			this.productId = product.getProductId();
		}
	}

	@Override
	public String toString() {
		return "CartItem{" + "cartItemId=" + cartItemId + ", userId=" + userId + ", productId=" + productId
				+ ", quantity=" + quantity + ", addedAt=" + addedAt + ", isDeleted=" + isDeleted + '}';
	}
}