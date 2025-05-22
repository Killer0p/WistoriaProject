package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.CartItem;
import model.Product;
import util.DbConnectionUtil;

public class CartItemDAO {
    private static final Logger LOGGER = Logger.getLogger(CartItemDAO.class.getName());

    // Adds an item to the user's cart or updates quantity if already present
    public static boolean addToCart(int userId, int productId, int quantity) throws Exception {
        try (Connection conn = DbConnectionUtil.getConnection()) {
            // Validate product existence and stock
            String checkSql = "SELECT stock_quantity FROM Products WHERE product_id = ? AND is_deleted = false";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, productId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    LOGGER.warning("Product not found: productId=" + productId);
                    return false;
                }
                int stock = rs.getInt("stock_quantity");
                if (stock < quantity) {
                    LOGGER.warning("Insufficient stock for productId=" + productId + ": requested=" + quantity + ", available=" + stock);
                    return false;
                }
            }

            // Check if item already exists in cart
            CartItem existingItem = getCartItemByUserAndProduct(userId, productId);
            if (existingItem != null) {
                // Validate stock for updated quantity
                int newQuantity = quantity;
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, productId);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt("stock_quantity") < newQuantity) {
                        LOGGER.warning("Insufficient stock for updating productId=" + productId + ": requested=" + newQuantity);
                        return false;
                    }
                }
                // Update existing item
                boolean updated = updateCartItemQuantity(existingItem.getCartItemId(), newQuantity);
                LOGGER.info("Updated cart item: userId=" + userId + ", productId=" + productId + ", newQuantity=" + newQuantity + ", success=" + updated);
                return updated;
            }

            // Insert new cart item
            String sql = "INSERT INTO CartItems (user_id, product_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setInt(2, productId);
                ps.setInt(3, quantity);
                int result = ps.executeUpdate();
                if (result > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            LOGGER.info("Added new cart item: userId=" + userId + ", productId=" + productId + ", quantity=" + quantity);
                            return true;
                        }
                    }
                }
                LOGGER.warning("Failed to insert cart item: userId=" + userId + ", productId=" + productId);
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error adding to cart: userId=" + userId + ", productId=" + productId, e);
            throw new Exception("Database error adding to cart: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding to cart: userId=" + userId + ", productId=" + productId, e);
            throw e;
        }
    }

    // Retrieves a cart item for a specific user and product
    public static CartItem getCartItemByUserAndProduct(int userId, int productId) throws Exception {
        String sql = "SELECT * FROM CartItems WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCartItem(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error fetching cart item: userId=" + userId + ", productId=" + productId, e);
            throw new Exception("Database error fetching cart item: " + e.getMessage(), e);
        }
        return null;
    }

    // Retrieves a cart item by its ID
    public static CartItem getCartItemById(int cartItemId) throws Exception {
        String sql = "SELECT * FROM CartItems WHERE cart_item_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCartItem(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error fetching cart item by ID: cartItemId=" + cartItemId, e);
            throw new Exception("Database error fetching cart item: " + e.getMessage(), e);
        }
        return null;
    }

    // Retrieves all cart items for a user and attaches product details
    public static List<CartItem> getCartItemsByUser(int userId) throws Exception {
        String sql = "SELECT * FROM CartItems WHERE user_id = ? ORDER BY added_at DESC";
        List<CartItem> cartItems = new ArrayList<>();
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cartItems.add(mapResultSetToCartItem(rs));
                }
            }
            // Fetch product details
            for (CartItem item : cartItems) {
                Product product = ProductDAO.getProductById(item.getProductId());
                if (product == null) {
                    LOGGER.warning("Product not found for cart item: userId=" + userId + ", productId=" + item.getProductId());
                    continue;
                }
                item.setProduct(product);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error fetching cart items: userId=" + userId, e);
            throw new Exception("Database error fetching cart items: " + e.getMessage(), e);
        }
        return cartItems;
    }

    // Updates the quantity of a cart item
    public static boolean updateCartItemQuantity(int cartItemId, int quantity) throws Exception {
        if (quantity <= 0) {
            return removeFromCart(cartItemId);
        }
        // Validate stock
        int stock = getProductStockQuantity(cartItemId);
        if (stock == -1) {
            LOGGER.warning("Product not found for cartItemId=" + cartItemId);
            return false;
        }
        if (quantity > stock) {
            LOGGER.warning("Insufficient stock for cartItemId=" + cartItemId + ": requested=" + quantity + ", available=" + stock);
            return false;
        }
        String sql = "UPDATE CartItems SET quantity = ? WHERE cart_item_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);
            int rows = ps.executeUpdate();
            LOGGER.info("Updated cart item quantity: cartItemId=" + cartItemId + ", quantity=" + quantity + ", rows=" + rows);
            return rows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error updating cart item: cartItemId=" + cartItemId, e);
            throw new Exception("Database error updating cart item: " + e.getMessage(), e);
        }
    }

    // Removes a cart item by its ID
    public static boolean removeFromCart(int cartItemId) throws Exception {
        String sql = "DELETE FROM CartItems WHERE cart_item_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            int rows = ps.executeUpdate();
            LOGGER.info("Removed cart item: cartItemId=" + cartItemId + ", rows=" + rows);
            return rows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error removing cart item: cartItemId=" + cartItemId, e);
            throw new Exception("Database error removing cart item: " + e.getMessage(), e);
        }
    }

    // Clears all items from a user's cart
    public static boolean clearCart(int userId) throws Exception {
        String sql = "DELETE FROM CartItems WHERE user_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rows = ps.executeUpdate();
            LOGGER.info("Cleared cart: userId=" + userId + ", rows=" + rows);
            return rows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error clearing cart: userId=" + userId, e);
            throw new Exception("Database error clearing cart: " + e.getMessage(), e);
        }
    }

    // Returns the count of items in a user's cart
    public static int getCartItemCount(int userId) throws Exception {
        String sql = "SELECT COUNT(*) FROM CartItems WHERE user_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    LOGGER.info("Cart item count: userId=" + userId + ", count=" + count);
                    return count;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error getting cart item count: userId=" + userId, e);
            throw new Exception("Database error getting cart item count: " + e.getMessage(), e);
        }
        return 0;
    }

    // Calculates total price of all items in the cart
    public static Double getCartTotal(int userId) throws Exception {
        String sql = "SELECT SUM(p.price * ci.quantity) AS total FROM CartItems ci " +
                     "JOIN Products p ON ci.product_id = p.product_id " +
                     "WHERE ci.user_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double total = rs.getDouble("total");
                    LOGGER.info("Cart total: userId=" + userId + ", total=" + total);
                    return total;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error calculating cart total: userId=" + userId, e);
            throw new Exception("Database error calculating cart total: " + e.getMessage(), e);
        }
        return 0.0;
    }

    // Checks if all items in the cart have enough stock
    public static boolean checkStockAvailability(int userId) throws Exception {
        String sql = "SELECT ci.product_id, ci.quantity, p.stock_quantity " +
                     "FROM CartItems ci JOIN Products p ON ci.product_id = p.product_id " +
                     "WHERE ci.user_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int requestedQuantity = rs.getInt("quantity");
                    int availableStock = rs.getInt("stock_quantity");
                    if (requestedQuantity > availableStock) {
                        LOGGER.warning("Insufficient stock: userId=" + userId + ", productId=" + rs.getInt("product_id") +
                                       ", requested=" + requestedQuantity + ", available=" + availableStock);
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error checking stock availability: userId=" + userId, e);
            throw new Exception("Database error checking stock availability: " + e.getMessage(), e);
        }
        return true;
    }

    // Retrieves stock quantity for a specific cart item's product
    public static int getProductStockQuantity(int cartItemId) throws Exception {
        String sql = "SELECT p.stock_quantity " +
                     "FROM CartItems ci " +
                     "JOIN Products p ON ci.product_id = p.product_id " +
                     "WHERE ci.cart_item_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int stock = rs.getInt("stock_quantity");
                    LOGGER.info("Product stock quantity: cartItemId=" + cartItemId + ", stock=" + stock);
                    return stock;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error getting product stock: cartItemId=" + cartItemId, e);
            throw new Exception("Database error getting product stock: " + e.getMessage(), e);
        }
        LOGGER.warning("Product not found for cartItemId=" + cartItemId);
        return -1;
    }

    // Maps result set data to a CartItem object
    private static CartItem mapResultSetToCartItem(ResultSet rs) throws Exception {
        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(rs.getInt("cart_item_id"));
        cartItem.setUserId(rs.getInt("user_id"));
        cartItem.setProductId(rs.getInt("product_id"));
        cartItem.setQuantity(rs.getInt("quantity"));
        cartItem.setAddedAt(rs.getTimestamp("added_at").toLocalDateTime());
        return cartItem;
    }
}