package controller.customer;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.WishlistDAO;
import model.User;
import model.WishlistItem;

@WebServlet("/user/wishlist")
public class WishlistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("user");

		// Check if user is logged in
		if (currentUser == null) {
			session.setAttribute("errorMessage", "Please log in to view your wishlist.");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		int userId = currentUser.getUserId();
		try {
			// Get wishlist items
			List<WishlistItem> wishlistItems = WishlistDAO.getWishlistByUser(userId);

			// Set attributes for JSP
			request.setAttribute("wishlistItems", wishlistItems);
			request.setAttribute("pageTitle", "Wishlist");
			request.setAttribute("pageContent", "/WEB-INF/views/customer/wishlist.jsp");
			request.getRequestDispatcher("/WEB-INF/views/customer/customer-template.jsp").forward(request, response);
		} catch (Exception e) {
			session.setAttribute("errorMessage", "Error loading wishlist.");
			response.sendRedirect(request.getContextPath() + "/user/home");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("user");

		if (currentUser == null) {
			session.setAttribute("errorMessage", "Please log in to modify your wishlist.");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		int userId = currentUser.getUserId();
		String action = request.getParameter("action");
		String productIdParam = request.getParameter("productId");
		String wishlistIdParam = request.getParameter("wishlistId");

		try {
			if ("add".equals(action) && productIdParam != null && !productIdParam.trim().isEmpty()) {
				int productId = Integer.parseInt(productIdParam);
				if (WishlistDAO.addToWishlist(userId, productId)) {
					session.setAttribute("message", "Product added to wishlist.");
				} else {
					session.setAttribute("errorMessage", "Failed to add product to wishlist.");
				}
				response.sendRedirect(request.getContextPath() + "/product/detail?id=" + productId);
				return;
			} else if ("remove".equals(action) && productIdParam != null && !productIdParam.trim().isEmpty()) {
				int productId = Integer.parseInt(productIdParam);
				if (WishlistDAO.removeProductFromWishlist(userId, productId)) {
					session.setAttribute("message", "Product removed from wishlist.");
				} else {
					session.setAttribute("errorMessage", "Failed to remove product from wishlist.");
				}
				response.sendRedirect(request.getContextPath() + "/product/detail?id=" + productId);
				return;
			} else if ("remove".equals(action) && wishlistIdParam != null && !wishlistIdParam.trim().isEmpty()) {
				int wishlistId = Integer.parseInt(wishlistIdParam);
				if (WishlistDAO.removeFromWishlist(wishlistId)) {
					session.setAttribute("message", "Item removed from wishlist.");
				} else {
					session.setAttribute("errorMessage", "Failed to remove item from wishlist.");
				}
			} else if ("moveToCart".equals(action) && wishlistIdParam != null && !wishlistIdParam.trim().isEmpty()) {
				int wishlistId = Integer.parseInt(wishlistIdParam);
				int quantity = 1; // Default quantity
				if (WishlistDAO.moveToCart(wishlistId, quantity)) {
					session.setAttribute("message", "Item moved to cart.");
				} else {
					session.setAttribute("errorMessage", "Failed to move item to cart.");
				}
			} else if ("clear".equals(action)) {
				if (WishlistDAO.clearWishlist(userId)) {
					session.setAttribute("message", "Wishlist cleared.");
				} else {
					session.setAttribute("errorMessage", "Failed to clear wishlist.");
				}
			} else {
				session.setAttribute("errorMessage", "Invalid action.");
			}
		} catch (NumberFormatException e) {

			session.setAttribute("errorMessage", "Invalid input format.");
		} catch (Exception e) {
			session.setAttribute("errorMessage", "Error processing action.");
		}

		response.sendRedirect(request.getContextPath() + "/user/wishlist");
	}
}