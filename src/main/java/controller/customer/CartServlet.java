package controller.customer;

import dao.CartItemDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CartItem;
import model.User;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling user cart operations.
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/user/cart"})
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE_PATH = "/WEB-INF/views/customer/customer-template.jsp";
    private static final String CART_PAGE = "/WEB-INF/views/customer/cart.jsp";
    private static final String LOGIN_REDIRECT = "/login";
    private static final String CART_REDIRECT = "/user/cart";

    /**
     * Handles GET requests for cart operations (display, add, update, remove, clear, checkout).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        // Check if user is logged in
        if (currentUser == null) {
            session.setAttribute("errorMessage", "Please log in to view your cart.");
            response.sendRedirect(request.getContextPath() + LOGIN_REDIRECT);
            return;
        }

        int userId = currentUser.getUserId();
        String action = request.getParameter("action");

        try {
            // Handle actions or display cart
            if (action == null || action.isEmpty()) {
                displayCart(request, response, userId);
                return;
            }

            switch (action) {
                case "add":
                    addToCart(request, response, userId);
                    break;
                case "update":
                    updateCart(request, response, userId);
                    break;
                case "remove":
                    removeFromCart(request, response, userId);
                    break;
                case "clear":
                    clearCart(request, response, userId);
                    break;
                case "checkout":
                    checkoutCart(request, response, userId);
                    break;
                default:
                    session.setAttribute("errorMessage", "Invalid cart action.");
                    displayCart(request, response, userId);
                    break;
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            displayCart(request, response, userId);
        }
    }

    /**
     * Handles POST requests by delegating to doGet.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Displays the user's cart with items, total, and item count.
     */
    private void displayCart(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        try {
            // Fetch cart data
            List<CartItem> cartItems = CartItemDAO.getCartItemsByUser(userId);
            Double cartTotal = CartItemDAO.getCartTotal(userId);
            int itemCount = CartItemDAO.getCartItemCount(userId);

            // Set attributes
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("cartTotal", cartTotal);
            request.setAttribute("itemCount", itemCount);
            request.setAttribute("pageTitle", "Your Cart - Wistoria Watches");
            request.setAttribute("pageContent", CART_PAGE);

            // Forward to template
            request.getRequestDispatcher(TEMPLATE_PATH).forward(request, response);
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Failed to load cart. Please try again.");
            request.setAttribute("pageTitle", "Your Cart - Wistoria Watches");
            request.setAttribute("pageContent", CART_PAGE);
            request.getRequestDispatcher(TEMPLATE_PATH).forward(request, response);
        }
    }

    /**
     * Adds an item to the user's cart.
     */
    private void addToCart(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            // Validate parameters
            String productIdParam = request.getParameter("productId");
            String quantityParam = request.getParameter("quantity");

            if (productIdParam == null || quantityParam == null) {
                session.setAttribute("error", "Missing product ID or quantity.");
                displayCart(request, response, userId);
                return;
            }

            int productId;
            int quantity;
            try {
                productId = Integer.parseInt(productIdParam);
                quantity = Integer.parseInt(quantityParam);
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid product ID or quantity.");
                displayCart(request, response, userId);
                return;
            }

            if (quantity <= 0) {
                quantity = 1; // Default to 1
            }

            boolean success = CartItemDAO.addToCart(userId, productId, quantity);
            if (success) {
                session.setAttribute("success", "Item added to cart successfully!");
            } else {
                session.setAttribute("error", "Failed to add item to cart.");
            }
            response.sendRedirect(request.getContextPath() + CART_REDIRECT);
        } catch (Exception e) {
            session.setAttribute("error", "Failed to add item to cart.");
            displayCart(request, response, userId);
        }
    }

    /**
     * Updates the quantity of an item in the user's cart.
     */
    private void updateCart(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            // Validate parameters
            String cartItemIdParam = request.getParameter("cartItemId");
            String quantityParam = request.getParameter("quantity");

            if (cartItemIdParam == null || quantityParam == null) {
                session.setAttribute("error", "Missing cart item ID or quantity.");
                response.sendRedirect(request.getContextPath() + CART_REDIRECT);
                return;
            }

            int cartItemId;
            int quantity;
            try {
                cartItemId = Integer.parseInt(cartItemIdParam);
                quantity = Integer.parseInt(quantityParam);
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid cart item ID or quantity.");
                response.sendRedirect(request.getContextPath() + CART_REDIRECT);
                return;
            }

            if (quantity <= 0) {
                // Remove item if quantity is 0 or less
                boolean success = CartItemDAO.removeFromCart(cartItemId);
                session.setAttribute("success", success ? "Item removed from cart." : "Failed to remove item from cart.");
            } else {
                // Check stock availability
                int stockQuantity = CartItemDAO.getProductStockQuantity(cartItemId);
                if (stockQuantity == -1) {
                    session.setAttribute("error", "Cart item or product not found.");
                    response.sendRedirect(request.getContextPath() + CART_REDIRECT);
                    return;
                }
                if (quantity > stockQuantity) {
                    session.setAttribute("error", "Requested quantity exceeds available stock.");
                    response.sendRedirect(request.getContextPath() + CART_REDIRECT);
                    return;
                }

                // Update quantity
                boolean success = CartItemDAO.updateCartItemQuantity(cartItemId, quantity);
                session.setAttribute("success", success ? "Cart updated successfully!" : "Failed to update cart.");
            }

            response.sendRedirect(request.getContextPath() + CART_REDIRECT);
        } catch (Exception e) {
            session.setAttribute("error", "Failed to update cart.");
            response.sendRedirect(request.getContextPath() + CART_REDIRECT);
        }
    }

    /**
     * Removes an item from the user's cart.
     */
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String cartItemIdParam = request.getParameter("cartItemId");
            int cartItemId = Integer.parseInt(cartItemIdParam);
            boolean success = CartItemDAO.removeFromCart(cartItemId);
            session.setAttribute("success", success ? "Item removed from cart successfully!" : "Failed to remove item from cart.");
            response.sendRedirect(request.getContextPath() + CART_REDIRECT);
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid cart item ID.");
            response.sendRedirect(request.getContextPath() + CART_REDIRECT);
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Failed to remove item from cart.");
            response.sendRedirect(request.getContextPath() + CART_REDIRECT);
        }
    }

    /**
     * Clears all items from the user's cart.
     */
    private void clearCart(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            boolean success = CartItemDAO.clearCart(userId);
            session.setAttribute("success", success ? "Cart cleared successfully!" : "Failed to clear cart.");
            response.sendRedirect(request.getContextPath() + CART_REDIRECT);
        } catch (Exception e) {
            session.setAttribute("error", "Failed to clear cart.");
            response.sendRedirect(request.getContextPath() + CART_REDIRECT);
        }
    }

    /**
     * Initiates the checkout process for the user's cart.
     */
    private void checkoutCart(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            // Check if cart is empty
            int itemCount = CartItemDAO.getCartItemCount(userId);
            if (itemCount == 0) {
                session.setAttribute("error", "Your cart is empty. Add items before checking out.");
                displayCart(request, response, userId);
                return;
            }

            // Check stock availability
            boolean stockAvailable = CartItemDAO.checkStockAvailability(userId);
            if (!stockAvailable) {
                session.setAttribute("error", "Some items are out of stock or have insufficient quantity.");
                displayCart(request, response, userId);
                return;
            }

            // Forward to checkout page
            request.setAttribute("cartItems", CartItemDAO.getCartItemsByUser(userId));
            request.setAttribute("cartTotal", CartItemDAO.getCartTotal(userId));
            request.setAttribute("pageTitle", "Checkout - Wistoria Watches");
            request.setAttribute("pageContent", "/views/user/checkout.jsp");
            request.getRequestDispatcher(TEMPLATE_PATH).forward(request, response);
        } catch (Exception e) {
            session.setAttribute("error", "Failed to proceed to checkout.");
            displayCart(request, response, userId);
        }
    }
}