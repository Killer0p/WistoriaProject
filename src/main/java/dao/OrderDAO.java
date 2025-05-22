package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

import model.CartItem;
import model.Order;
import model.OrderDetail;
import model.Product;
import model.ShippingInfo;
import model.User;
import util.DbConnectionUtil;

public class OrderDAO {

	// Get total number of non-deleted orders
	public static int getTotalOrderCount() throws Exception {
		String sql = "SELECT COUNT(*) FROM Orders WHERE is_deleted = false";
		try (Connection conn = DbConnectionUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			return rs.next() ? rs.getInt(1) : 0;
		}
	}

	// Get total revenue from non-deleted orders
	public static double getTotalRevenue() throws Exception {
		String sql = "SELECT SUM(total_amount) FROM Orders WHERE is_deleted = false";
		try (Connection conn = DbConnectionUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			return rs.next() ? rs.getDouble(1) : 0.0;
		}
	}

	// Get formatted month labels for last 6 months from orders
	public static List<String> getLast6MonthsLabels() throws Exception {
		String sql = "SELECT DATE_FORMAT(order_date, '%b %Y') AS month " + "FROM Orders WHERE is_deleted = false "
				+ "GROUP BY YEAR(order_date), MONTH(order_date) "
				+ "ORDER BY YEAR(order_date) DESC, MONTH(order_date) DESC LIMIT 6";

		List<String> labels = new ArrayList<>();
		try (Connection conn = DbConnectionUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				labels.add("\"" + rs.getString("month") + "\"");
			}
		}
		Collections.reverse(labels); // Chronological order
		return labels;
	}

	// Get revenue data for the last 6 months
	public static List<Double> getLast6MonthsRevenue() throws Exception {
		String sql = "SELECT DATE_FORMAT(order_date, '%b %Y') AS month, SUM(total_amount) AS revenue "
				+ "FROM Orders WHERE is_deleted = false " + "GROUP BY YEAR(order_date), MONTH(order_date) "
				+ "ORDER BY YEAR(order_date) DESC, MONTH(order_date) DESC LIMIT 6";

		List<Double> revenues = new ArrayList<>();
		try (Connection conn = DbConnectionUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				revenues.add(rs.getDouble("revenue"));
			}
		}
		Collections.reverse(revenues); // Match order of labels
		return revenues;
	}

	// Get paginated and filtered list of orders
	public static List<Order> getOrders(int page, int limit, String status, String sortBy, Date dateFrom, Date dateTo)
			throws Exception {
		List<Order> orders = new ArrayList<>();
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> parameters = new ArrayList<>();

		sqlBuilder.append(
				"SELECT o.*, u.name, u.email, u.phone_number, si.shipping_id, si.address, si.contact, si.shipping_method "
						+ "FROM Orders o " + "JOIN Users u ON o.user_id = u.user_id "
						+ "LEFT JOIN ShippingInfo si ON o.shipping_id = si.shipping_id "
						+ "WHERE o.is_deleted = false ");

		if (status != null && !status.isEmpty()) {
			sqlBuilder.append("AND o.status = ? ");
			parameters.add(status);
		}
		if (dateFrom != null) {
			sqlBuilder.append("AND o.order_date >= ? ");
			parameters.add(new Timestamp(dateFrom.getTime()));
		}
		if (dateTo != null) {
			sqlBuilder.append("AND o.order_date <= ? ");
			parameters.add(new Timestamp(dateTo.getTime()));
		}

		// Apply sorting
		switch (sortBy != null ? sortBy : "") {
		case "newest":
			sqlBuilder.append("ORDER BY o.order_date DESC ");
			break;
		case "oldest":
			sqlBuilder.append("ORDER BY o.order_date ASC ");
			break;
		case "highest":
			sqlBuilder.append("ORDER BY o.total_amount DESC ");
			break;
		case "lowest":
			sqlBuilder.append("ORDER BY o.total_amount ASC ");
			break;
		default:
			sqlBuilder.append("ORDER BY o.order_date DESC ");
			break;
		}

		// Pagination
		sqlBuilder.append("LIMIT ? OFFSET ? ");
		parameters.add(limit);
		parameters.add((page - 1) * limit);

		try (Connection conn = DbConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

			for (int i = 0; i < parameters.size(); i++) {
				pstmt.setObject(i + 1, parameters.get(i));
			}

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Order order = new Order(rs.getInt("order_id"), rs.getInt("user_id"),
							rs.getTimestamp("order_date").toLocalDateTime(), rs.getBigDecimal("total_amount"),
							rs.getString("status"), rs.getBoolean("is_deleted"), rs.getInt("shipping_id"));

					User user = new User();
					user.setUserId(rs.getInt("user_id"));
					String name = rs.getString("name");
					user.setName(name != null ? name : "Unknown User");
					user.setEmail(rs.getString("email"));
					user.setPhoneNumber(rs.getString("phone_number"));
					order.setUser(user);

					ShippingInfo shippingInfo = new ShippingInfo();
					int shippingId = rs.getInt("shipping_id");
					if (!rs.wasNull()) {
						shippingInfo.setShippingId(shippingId);
						shippingInfo.setAddress(rs.getString("address"));
						shippingInfo.setContact(rs.getString("contact"));
						shippingInfo.setShippingMethod(rs.getString("shipping_method"));
						shippingInfo.setIsDeleted(false);
					} else {
						System.out.println("No shipping info for order_id=" + order.getOrderId());
						shippingInfo.setAddress("N/A");
						shippingInfo.setContact("N/A");
						shippingInfo.setShippingMethod("N/A");
					}
					order.setShippingInfo(shippingInfo);

					List<OrderDetail> details = getOrderDetails(order.getOrderId());
					order.setOrderDetails(details);
					if (details.isEmpty()) {
						System.out.println("No order details found for order_id=" + order.getOrderId());
					}

					orders.add(order);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching orders: " + e.getMessage());
			throw e;
		}
		return orders;
	}

	// Get total order count with optional filters for pagination
	public static int getTotalOrderCount(String status, Date dateFrom, Date dateTo) throws Exception {
		StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(*) FROM Orders WHERE is_deleted = false ");
		List<Object> parameters = new ArrayList<>();

		if (status != null && !status.isEmpty()) {
			sqlBuilder.append("AND status = ? ");
			parameters.add(status);
		}
		if (dateFrom != null) {
			sqlBuilder.append("AND order_date >= ? ");
			parameters.add(new Timestamp(dateFrom.getTime()));
		}
		if (dateTo != null) {
			sqlBuilder.append("AND order_date <= ? ");
			parameters.add(new Timestamp(dateTo.getTime()));
		}

		try (Connection conn = DbConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {
			for (int i = 0; i < parameters.size(); i++) {
				pstmt.setObject(i + 1, parameters.get(i));
			}
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		}
	}

	// Get order details for a specific order
	private static List<OrderDetail> getOrderDetails(int orderId) throws Exception {
		List<OrderDetail> orderDetails = new ArrayList<>();
		String sql = "SELECT od.*, p.name, p.image_url " + "FROM OrderDetails od "
				+ "JOIN Products p ON od.product_id = p.product_id "
				+ "WHERE od.order_id = ? AND od.is_deleted = false";

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, orderId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					OrderDetail detail = new OrderDetail(rs.getInt("order_details_id"), rs.getInt("order_id"),
							rs.getInt("product_id"), rs.getInt("quantity"), rs.getBigDecimal("price"));

					Product product = new Product();
					product.setProductId(rs.getInt("product_id"));
					String productName = rs.getString("name");
					product.setName(productName != null ? productName : "Unknown Product");
					product.setImageUrl(rs.getString("image_url"));
					detail.setProduct(product);

					orderDetails.add(detail);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching order details for order_id=" + orderId + ": " + e.getMessage());
			throw e;
		}
		return orderDetails;
	}

	// Update the status of an order
	public static boolean updateOrderStatus(int orderId, String status) throws Exception {
		String sql = "UPDATE Orders SET status = ? WHERE order_id = ? AND is_deleted = false";

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, status);
			pstmt.setInt(2, orderId);

			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Updated status for order_id=" + orderId + " to " + status);
				return true;
			}
			System.out.println("No rows updated for order_id=" + orderId);
			return false;
		}
	}

	// Get shipping info for an order
	public static ShippingInfo getShippingInfo(int orderId) throws Exception {
		String sql = "SELECT si.shipping_id, si.address, si.contact, si.shipping_method " + "FROM ShippingInfo si "
				+ "JOIN Orders o ON o.shipping_id = si.shipping_id " + "WHERE o.order_id = ? AND si.is_deleted = false";

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, orderId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					ShippingInfo shippingInfo = new ShippingInfo();
					shippingInfo.setShippingId(rs.getInt("shipping_id"));
					shippingInfo.setAddress(rs.getString("address"));
					shippingInfo.setContact(rs.getString("contact"));
					shippingInfo.setShippingMethod(rs.getString("shipping_method"));
					shippingInfo.setIsDeleted(false);
					System.out.println("Fetched shipping info for order_id=" + orderId + ": " + shippingInfo);
					return shippingInfo;
				} else {
					System.out.println("No shipping info found for order_id=" + orderId);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching shipping info for order_id=" + orderId + ": " + e.getMessage());
			throw e;
		}
		// Fallback to default values
		ShippingInfo defaultInfo = new ShippingInfo();
		defaultInfo.setAddress("N/A");
		defaultInfo.setContact("N/A");
		defaultInfo.setShippingMethod("N/A");
		return defaultInfo;
	}

	// Get a single order by ID with user and order details
	public static Order getOrderById(int orderId) throws Exception {
		String sql = "SELECT o.*, u.name, u.email, u.phone_number, si.shipping_id, si.address, si.contact, si.shipping_method "
				+ "FROM Orders o " + "JOIN Users u ON o.user_id = u.user_id "
				+ "LEFT JOIN ShippingInfo si ON o.shipping_id = si.shipping_id "
				+ "WHERE o.order_id = ? AND o.is_deleted = false";

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, orderId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					Order order = new Order(rs.getInt("order_id"), rs.getInt("user_id"),
							rs.getTimestamp("order_date").toLocalDateTime(), rs.getBigDecimal("total_amount"),
							rs.getString("status"), rs.getBoolean("is_deleted"), rs.getInt("shipping_id"));

					User user = new User();
					user.setUserId(rs.getInt("user_id"));
					String name = rs.getString("name");
					user.setName(name != null ? name : "Unknown User");
					user.setEmail(rs.getString("email"));
					user.setPhoneNumber(rs.getString("phone_number"));
					order.setUser(user);

					ShippingInfo shippingInfo = new ShippingInfo();
					int shippingId = rs.getInt("shipping_id");
					if (!rs.wasNull()) {
						shippingInfo.setShippingId(shippingId);
						shippingInfo.setAddress(rs.getString("address"));
						shippingInfo.setContact(rs.getString("contact"));
						shippingInfo.setShippingMethod(rs.getString("shipping_method"));
						shippingInfo.setIsDeleted(false);
					} else {
						shippingInfo.setAddress("N/A");
						shippingInfo.setContact("N/A");
						shippingInfo.setShippingMethod("N/A");
					}
					order.setShippingInfo(shippingInfo);

					List<OrderDetail> details = getOrderDetails(order.getOrderId());
					order.setOrderDetails(details);
					if (details.isEmpty()) {
						System.out.println("No order details found for order_id=" + order.getOrderId());
					}

					return order;
				} else {
					System.out.println("No order found for order_id=" + orderId);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching order_id=" + orderId + ": " + e.getMessage());
			throw e;
		}
		return null;
	}

	// Create a new order without shipping info
	public static int createOrder(Order order, List<CartItem> cartItems) throws Exception {
		Connection conn = null;
		try {
			conn = DbConnectionUtil.getConnection();
			conn.setAutoCommit(false); // Start transaction

			// Insert into Orders table
			String orderSql = "INSERT INTO Orders (user_id, order_date, total_amount, status, is_deleted) VALUES (?, ?, ?, ?, ?)";
			int orderId;
			try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
				orderStmt.setInt(1, order.getUserId());
				orderStmt.setTimestamp(2, order.getOrderDate() != null ? Timestamp.valueOf(order.getOrderDate())
						: Timestamp.valueOf(LocalDateTime.now()));
				orderStmt.setBigDecimal(3, order.getTotalAmount());
				orderStmt.setString(4, order.getStatus() != null ? order.getStatus() : "Pending");
				orderStmt.setBoolean(5, false);
				orderStmt.executeUpdate();

				try (ResultSet rs = orderStmt.getGeneratedKeys()) {
					if (!rs.next())
						throw new Exception("Failed to retrieve order ID");
					orderId = rs.getInt(1);
				}
			}

			// Insert order details
			String detailSql = "INSERT INTO OrderDetails (order_id, product_id, quantity, price, is_deleted) VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement detailStmt = conn.prepareStatement(detailSql)) {
				for (CartItem item : cartItems) {
					detailStmt.setInt(1, orderId);
					detailStmt.setInt(2, item.getProductId());
					detailStmt.setInt(3, item.getQuantity());
					detailStmt.setDouble(4, item.getProduct().getPrice());
					detailStmt.setBoolean(5, false);
					detailStmt.addBatch();
				}
				int[] results = detailStmt.executeBatch();
				System.out.println("Inserted " + results.length + " order details for order_id=" + orderId);
			}

			conn.commit();
			System.out.println("Created order_id=" + orderId);
			return orderId;
		} catch (Exception e) {
			if (conn != null)
				conn.rollback();
			System.err.println("Error creating order: " + e.getMessage());
			throw e;
		} finally {
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}
		}
	}

	// Create a new order with shipping info
	public static int createOrder(Order order, List<CartItem> cartItems, Map<String, String> shippingInfo)
			throws Exception {
		Connection conn = null;
		try {
			conn = DbConnectionUtil.getConnection();
			conn.setAutoCommit(false);

			// Insert shipping info
			String shippingSql = "INSERT INTO ShippingInfo (address, contact, shipping_method, is_deleted) VALUES (?, ?, ?, ?)";
			int shippingId;
			try (PreparedStatement shippingStmt = conn.prepareStatement(shippingSql, Statement.RETURN_GENERATED_KEYS)) {
				shippingStmt.setString(1, shippingInfo.get("address"));
				shippingStmt.setString(2, shippingInfo.get("contact"));
				shippingStmt.setString(3, shippingInfo.get("shippingMethod"));
				shippingStmt.setBoolean(4, false);
				shippingStmt.executeUpdate();

				try (ResultSet rs = shippingStmt.getGeneratedKeys()) {
					if (!rs.next())
						throw new Exception("Failed to retrieve shipping ID");
					shippingId = rs.getInt(1);
				}
			}

			// Insert order
			String orderSql = "INSERT INTO Orders (user_id, order_date, total_amount, status, is_deleted, shipping_id) VALUES (?, ?, ?, ?, ?, ?)";
			int orderId;
			try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
				orderStmt.setInt(1, order.getUserId());
				orderStmt.setTimestamp(2, order.getOrderDate() != null ? Timestamp.valueOf(order.getOrderDate())
						: Timestamp.valueOf(LocalDateTime.now()));
				orderStmt.setBigDecimal(3, order.getTotalAmount());
				orderStmt.setString(4, order.getStatus() != null ? order.getStatus() : "Pending");
				orderStmt.setBoolean(5, false);
				orderStmt.setInt(6, shippingId);
				orderStmt.executeUpdate();

				try (ResultSet rs = orderStmt.getGeneratedKeys()) {
					if (!rs.next())
						throw new Exception("Failed to retrieve order ID");
					orderId = rs.getInt(1);
				}
			}

			// Insert order details
			String detailSql = "INSERT INTO OrderDetails (order_id, product_id, quantity, price, is_deleted) VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement detailStmt = conn.prepareStatement(detailSql)) {
				for (CartItem item : cartItems) {
					detailStmt.setInt(1, orderId);
					detailStmt.setInt(2, item.getProductId());
					detailStmt.setInt(3, item.getQuantity());
					detailStmt.setDouble(4, item.getProduct().getPrice());
					detailStmt.setBoolean(5, false);
					detailStmt.addBatch();
				}
				int[] results = detailStmt.executeBatch();
				System.out.println("Inserted " + results.length + " order details for order_id=" + orderId);
			}

			conn.commit();
			System.out.println("Created order_id=" + orderId + " with shipping_id=" + shippingId);
			return orderId;
		} catch (Exception e) {
			if (conn != null)
				conn.rollback();
			System.err.println("Error creating order: " + e.getMessage());
			throw e;
		} finally {
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}
		}
	}
}