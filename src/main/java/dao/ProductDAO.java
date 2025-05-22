package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Product;
import util.DbConnectionUtil;

public class ProductDAO {

	// Adds a new product to the database
	public static boolean addProduct(Product product) {
		String sql = "INSERT INTO Products (name, description, price, stock_quantity, category, image_url, brand, case_material, movement_type, water_resistance_meters, warranty_months) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DbConnectionUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, product.getName());
			ps.setString(2, product.getDescription());
			ps.setDouble(3, product.getPrice());
			ps.setInt(4, product.getStockQuantity());
			ps.setString(5, product.getCategory());
			ps.setString(6, product.getImageUrl());
			ps.setString(7, product.getBrand());
			ps.setString(8, product.getCaseMaterial());
			ps.setString(9, product.getMovementType());
			ps.setInt(10, product.getWaterResistanceMeters());
			ps.setInt(11, product.getWarrantyMonths());

			int result = ps.executeUpdate();

			if (result > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						product.setProductId(rs.getInt(1));
					}
				}
				return true;
			}
		} catch (Exception e) {
			System.err.println("Error adding product: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	// Retrieves a product by its ID
	public static Product getProductById(int productId) {
		String sql = "SELECT * FROM Products WHERE product_id = ?";

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, productId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToProduct(rs);
				}
			}
		} catch (Exception e) {
			System.err.println("Error getting product by ID: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	// Retrieves all non-deleted products
	public static List<Product> getAllProducts() {
		String sql = "SELECT * FROM Products WHERE is_deleted = FALSE ORDER BY product_id";
		List<Product> products = new ArrayList<>();

		try (Connection conn = DbConnectionUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				products.add(mapResultSetToProduct(rs));
			}
		} catch (Exception e) {
			System.err.println("Error retrieving all products: " + e.getMessage());
			e.printStackTrace();
		}
		return products;
	}

	// Retrieves products filtered by category
	public static List<Product> getProductsByCategory(String category) {
		String sql = "SELECT * FROM Products WHERE category = ? ORDER BY name";
		List<Product> products = new ArrayList<>();

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, category);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					products.add(mapResultSetToProduct(rs));
				}
			}
		} catch (Exception e) {
			System.err.println("Error retrieving products by category: " + e.getMessage());
			e.printStackTrace();
		}
		return products;
	}

	// Searches products by keyword in name or description
	public static List<Product> searchProducts(String searchTerm) {
		String sql = "SELECT * FROM Products WHERE name LIKE ? OR description LIKE ? ORDER BY name";
		List<Product> products = new ArrayList<>();
		String searchPattern = "%" + searchTerm + "%";

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, searchPattern);
			ps.setString(2, searchPattern);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					products.add(mapResultSetToProduct(rs));
				}
			}
		} catch (Exception e) {
			System.err.println("Error searching products: " + e.getMessage());
			e.printStackTrace();
		}
		return products;
	}

	// Updates product details
	public static boolean updateProduct(Product product) {
		String sql = "UPDATE Products SET name = ?, description = ?, price = ?, stock_quantity = ?, category = ?, image_url = ?, brand = ?, case_material = ?, movement_type = ?, water_resistance_meters = ?, warranty_months = ? WHERE product_id = ?";

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, product.getName());
			ps.setString(2, product.getDescription());
			ps.setDouble(3, product.getPrice());
			ps.setInt(4, product.getStockQuantity());
			ps.setString(5, product.getCategory());
			ps.setString(6, product.getImageUrl());
			ps.setString(7, product.getBrand());
			ps.setString(8, product.getCaseMaterial());
			ps.setString(9, product.getMovementType());
			ps.setInt(10, product.getWaterResistanceMeters());
			ps.setInt(11, product.getWarrantyMonths());
			ps.setInt(12, product.getProductId());

			int result = ps.executeUpdate();
			return result > 0;
		} catch (Exception e) {
			System.err.println("Error updating product: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Sets the stock quantity to a new value
	public static boolean updateStockQuantity(int productId, int newQuantity) {
		String sql = "UPDATE Products SET stock_quantity = ? WHERE product_id = ?";

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, newQuantity);
			ps.setInt(2, productId);

			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			System.err.println("Error updating stock quantity: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Decreases stock quantity safely, ensuring non-negative stock
	public static boolean decreaseStockQuantity(int productId, int quantity) {
		String sql = "UPDATE Products SET stock_quantity = stock_quantity - ? WHERE product_id = ? AND stock_quantity >= ? AND is_deleted = FALSE";

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, quantity);
			ps.setInt(2, productId);
			ps.setInt(3, quantity);

			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			System.err.println("Error decreasing stock quantity: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Soft deletes a product by setting is_deleted = TRUE
	public static boolean deleteProduct(int productId) {
		String sql = "UPDATE Products SET is_deleted = TRUE WHERE product_id = ?";

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, productId);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			System.err.println("Error deleting product: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Gets all unique product categories
	public static List<String> getAllCategories() {
		String sql = "SELECT DISTINCT category FROM Products ORDER BY category";
		List<String> categories = new ArrayList<>();

		try (Connection conn = DbConnectionUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				categories.add(rs.getString("category"));
			}
		} catch (Exception e) {
			System.err.println("Error fetching categories: " + e.getMessage());
			e.printStackTrace();
		}
		return categories;
	}

	// Checks if a product name already exists in the database
	public static boolean isProductNameExists(String name) {
		String sql = "SELECT COUNT(*) FROM Products WHERE name = ?";

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, name);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() && rs.getInt(1) > 0;
			}
		} catch (Exception e) {
			System.err.println("Error checking product name existence: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Retrieves paginated products
	public static List<Product> getProductsWithPagination(int page, int itemsPerPage) {
		List<Product> products = new ArrayList<>();
		int offset = (page - 1) * itemsPerPage;

		String sql = "SELECT * FROM products WHERE is_deleted = FALSE ORDER BY product_id DESC LIMIT ? OFFSET ?";

		try (Connection conn = DbConnectionUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, itemsPerPage);
			stmt.setInt(2, offset);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					products.add(mapResultSetToProduct(rs));
				}
			}
		} catch (Exception e) {
			System.err.println("Error retrieving paginated products: " + e.getMessage());
			e.printStackTrace();
		}
		return products;
	}

	// Gets total count of products in the database
	public static int getTotalProductCount() {
		String sql = "SELECT COUNT(*) FROM Products";
		int count = 0;

		try (Connection conn = DbConnectionUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println("Error getting total product count: " + e.getMessage());
			e.printStackTrace();
		}
		return count;
	}

	// Gets recommended (recent) products (limit 6)
	public static List<Product> getRecommendedProducts() {
		String sql = "SELECT * FROM Products WHERE is_deleted = FALSE ORDER BY product_id DESC LIMIT 6";
		List<Product> recommended = new ArrayList<>();

		try (Connection conn = DbConnectionUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				recommended.add(mapResultSetToProduct(rs));
			}
		} catch (Exception e) {
			System.err.println("Error fetching recommended products: " + e.getMessage());
			e.printStackTrace();
		}
		return recommended;
	}

	// Maps ResultSet row to Product object
	private static Product mapResultSetToProduct(ResultSet rs) throws Exception {
		Product product = new Product();
		product.setProductId(rs.getInt("product_id"));
		product.setName(rs.getString("name"));
		product.setDescription(rs.getString("description"));
		product.setPrice(rs.getDouble("price"));
		product.setStockQuantity(rs.getInt("stock_quantity"));
		product.setCategory(rs.getString("category"));
		product.setImageUrl(rs.getString("image_url"));
		product.setBrand(rs.getString("brand"));
		product.setCaseMaterial(rs.getString("case_material"));
		product.setMovementType(rs.getString("movement_type"));
		product.setWaterResistanceMeters(rs.getInt("water_resistance_meters"));
		product.setWarrantyMonths(rs.getInt("warranty_months"));
		return product;
	}
}
