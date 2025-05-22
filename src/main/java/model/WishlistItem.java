package model;

import java.time.LocalDateTime;

public class WishlistItem {
	private Integer wishlistId;
	private Integer userId;
	private Integer productId;
	private LocalDateTime addedAt;
	private Boolean isDeleted;

	// Reference objects
	private User user;
	private Product product;

	// Constructors
	public WishlistItem() {
	}

	public WishlistItem(Integer wishlistId, Integer userId, Integer productId, LocalDateTime addedAt, Boolean isDeleted) {
		this.wishlistId = wishlistId;
		this.userId = userId;
		this.productId = productId;
		this.addedAt = addedAt;
		this.isDeleted = isDeleted;
	}

	// Getters and Setters
	public Integer getWishlistId() {
		return wishlistId;
	}

	public void setWishlistId(Integer wishlistId) {
		this.wishlistId = wishlistId;
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
		return "Wishlist{" + "wishlistId=" + wishlistId + ", userId=" + userId + ", productId=" + productId
				+ ", addedAt=" + addedAt + ", isDeleted=" + isDeleted + '}';
	}
}