package model;

import java.math.BigDecimal;

public class OrderDetail {
	private Integer orderDetailsId;
	private Integer orderId;
	private Integer productId;
	private Integer quantity;
	private BigDecimal price;
	private Boolean isDeleted;

	// Reference objects
	private Order order;
	private Product product;

	// Constructors
	public OrderDetail() {
	}
	
	public OrderDetail(Integer orderDetailsId, Integer orderId, Integer productId, Integer quantity, BigDecimal price) {
		super();
		this.orderDetailsId = orderDetailsId;
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}

	public OrderDetail(Integer orderDetailsId, Integer orderId, Integer productId, Integer quantity, BigDecimal price,
			Boolean isDeleted) {
		this.orderDetailsId = orderDetailsId;
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
		this.isDeleted = isDeleted;
	}

	// Getters and Setters
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
		if (order != null) {
			this.orderId = order.getOrderId();
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
		return "OrderDetail{" + "orderDetailsId=" + orderDetailsId + ", orderId=" + orderId + ", productId=" + productId
				+ ", quantity=" + quantity + ", price=" + price + ", isDeleted=" + isDeleted + '}';
	}
}