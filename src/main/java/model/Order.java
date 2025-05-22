package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
	private Integer orderId;
	private Integer userId;
	private LocalDateTime orderDate;
	private BigDecimal totalAmount;
	private String status;
	private Boolean isDeleted;
	private Integer shippingId;

	// Reference objects
	private User user;
	private ShippingInfo shippingInfo;
	private List<OrderDetail> orderDetails;

	// Constructors
	public Order() {
	}

	public Order(Integer userId, BigDecimal totalAmount) {
		super();
		this.userId = userId;
		this.totalAmount = totalAmount;
	}

	public Order(Integer orderId, Integer userId, LocalDateTime orderDate, BigDecimal totalAmount, String status,
			Boolean isDeleted, Integer shippingId) {
		this.orderId = orderId;
		this.userId = userId;
		this.orderDate = orderDate;
		this.totalAmount = totalAmount;
		this.status = status;
		this.isDeleted = isDeleted;
		this.shippingId = shippingId;
	}

	// Getters and Setters
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getShippingId() {
		return shippingId;
	}

	public void setShippingId(Integer shippingId) {
		this.shippingId = shippingId;
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

	public ShippingInfo getShippingInfo() {
		return shippingInfo;
	}

	public void setShippingInfo(ShippingInfo shippingInfo) {
		this.shippingInfo = shippingInfo;
		if (shippingInfo != null) {
			this.shippingId = shippingInfo.getShippingId();
		}
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	@Override
	public String toString() {
		return "Order{" + "orderId=" + orderId + ", userId=" + userId + ", orderDate=" + orderDate + ", totalAmount="
				+ totalAmount + ", status='" + status + '\'' + ", isDeleted=" + isDeleted + ", shippingId=" + shippingId
				+ '}';
	}
}