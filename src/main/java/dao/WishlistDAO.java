package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.WishlistItem;
import util.DbConnectionUtil;
import model.Product;

public class WishlistDAO {

    // Adds a product to the user's wishlist
    public static boolean addToWishlist(int userId, int productId) {
        String sql = "INSERT INTO Wishlist (user_id, product_id, added_at) VALUES (?, ?, NOW())";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            System.out.println("Adding to wishlist: userId=" + userId + ", productId=" + productId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding to wishlist.");
            e.printStackTrace();
            return false;
        }
    }

    // Removes a specific product from the user's wishlist
    public static boolean removeProductFromWishlist(int userId, int productId) {
        String sql = "DELETE FROM Wishlist WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            System.out.println("Removing from wishlist: userId=" + userId + ", productId=" + productId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error removing product from wishlist.");
            e.printStackTrace();
            return false;
        }
    }

    // Checks whether a product exists in the user's wishlist
    public static boolean isProductInWishlist(int userId, int productId) {
        String sql = "SELECT 1 FROM Wishlist WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next();
            System.out.println("Checking wishlist: userId=" + userId + ", productId=" + productId + ", exists=" + exists);
            return exists;
        } catch (SQLException e) {
            System.err.println("Error checking if product is in wishlist.");
            e.printStackTrace();
            return false;
        }
    }

    // Retrieves all wishlist items for a user
    public static List<WishlistItem> getWishlistByUser(int userId) {
        List<WishlistItem> items = new ArrayList<>();
        String sql = "SELECT wishlist_id, user_id, product_id, added_at FROM Wishlist WHERE user_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                WishlistItem item = new WishlistItem();
                item.setWishlistId(rs.getInt("wishlist_id"));
                item.setUserId(rs.getInt("user_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setAddedAt(rs.getTimestamp("added_at").toLocalDateTime());
                try {
                    Product product = ProductDAO.getProductById(item.getProductId());
                    if (product != null) {
                        item.setProduct(product);
                        items.add(item);
                    } else {
                        System.err.println("Product not found for productId=" + item.getProductId());
                    }
                } catch (Exception e) {
                    System.err.println("Error retrieving product details for productId=" + item.getProductId());
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving wishlist for userId=" + userId);
            e.printStackTrace();
        }
        return items;
    }

    // Removes a wishlist item by its unique wishlist_id
    public static boolean removeFromWishlist(int wishlistId) {
        String sql = "DELETE FROM Wishlist WHERE wishlist_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, wishlistId);
            System.out.println("Removing wishlist item: wishlistId=" + wishlistId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error removing wishlist item.");
            e.printStackTrace();
            return false;
        }
    }

    // Moves a wishlist item to the cart and removes it from the wishlist
    public static boolean moveToCart(int wishlistId, int quantity) {
        String sql = "SELECT user_id, product_id FROM Wishlist WHERE wishlist_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, wishlistId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                int productId = rs.getInt("product_id");
                try {
                    boolean addedToCart = CartItemDAO.addToCart(userId, productId, quantity);
                    if (addedToCart) {
                        return removeFromWishlist(wishlistId);
                    }
                    System.err.println("Failed to add product to cart: userId=" + userId + ", productId=" + productId);
                    return false;
                } catch (Exception e) {
                    System.err.println("Error while moving product to cart: userId=" + userId + ", productId=" + productId);
                    e.printStackTrace();
                    return false;
                }
            }
            System.err.println("Wishlist item not found for wishlistId=" + wishlistId);
            return false;
        } catch (SQLException e) {
            System.err.println("Error fetching wishlist item for move to cart.");
            e.printStackTrace();
            return false;
        }
    }

    // Clears all wishlist items for a user
    public static boolean clearWishlist(int userId) {
        String sql = "DELETE FROM Wishlist WHERE user_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            System.out.println("Clearing wishlist for userId=" + userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error clearing wishlist.");
            e.printStackTrace();
            return false;
        }
    }
}
