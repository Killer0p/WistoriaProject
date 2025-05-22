package controller.customer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.CartItemDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import model.CartItem;
import model.Order;
import model.User;

@WebServlet("/user/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            session.setAttribute("error", "Please log in to proceed to checkout.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = currentUser.getUserId();
        try {
            List<CartItem> cartItems = CartItemDAO.getCartItemsByUser(userId);
            if (cartItems.isEmpty()) {
                session.setAttribute("error", "Your cart is empty. Add some products before checking out.");
                response.sendRedirect(request.getContextPath() + "/user/cart");
                return;
            }

            Double cartSubtotal = CartItemDAO.getCartTotal(userId);
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("cartSubtotal", cartSubtotal);
            request.setAttribute("pageTitle", "Checkout");
            request.setAttribute("pageContent", "/WEB-INF/views/customer/checkout.jsp");
            request.getRequestDispatcher("/WEB-INF/views/customer/customer-template.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Error loading checkout: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/user/cart");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            session.setAttribute("errorMessage", "Please log in to proceed to checkout.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = currentUser.getUserId();
        String action = request.getParameter("action");

        if (!"confirm".equals(action)) {
            session.setAttribute("errorMessage", "Invalid action.");
            response.sendRedirect(request.getContextPath() + "/user/checkout");
            return;
        }

        try {
            List<CartItem> cartItems = CartItemDAO.getCartItemsByUser(userId);
            if (cartItems.isEmpty()) {
                session.setAttribute("errorMessage", "Your cart is empty.");
                response.sendRedirect(request.getContextPath() + "/user/cart");
                return;
            }

            if (!CartItemDAO.checkStockAvailability(userId)) {
                session.setAttribute("errorMessage", "Some items are out of stock or have insufficient quantity.");
                response.sendRedirect(request.getContextPath() + "/user/cart");
                return;
            }

            String address = request.getParameter("address");
            String contact = request.getParameter("contact");
            String shippingMethod = request.getParameter("shippingMethod");
            if (address == null || address.trim().isEmpty() || contact == null || contact.trim().isEmpty() ||
                shippingMethod == null || !List.of("standard", "express").contains(shippingMethod)) {
                session.setAttribute("errorMessage", "Please provide valid shipping information.");
                response.sendRedirect(request.getContextPath() + "/user/checkout");
                return;
            }

            // Calculate shipping cost
            double shippingCost = "standard".equals(shippingMethod) ? 5.00 : 15.00;
            Double cartSubtotal = CartItemDAO.getCartTotal(userId);
            double total = cartSubtotal + shippingCost;

            // Prepare shipping info
            Map<String, String> shippingInfo = new HashMap<>();
            shippingInfo.put("address", address);
            shippingInfo.put("contact", contact);
            shippingInfo.put("shippingMethod", shippingMethod);

            // Create order
            Order order = new Order(userId, new BigDecimal(total));
            int orderId = OrderDAO.createOrder(order, cartItems, shippingInfo);

            // Update product stock
            for (CartItem item : cartItems) {
                if (!ProductDAO.decreaseStockQuantity(item.getProductId(), item.getQuantity())) {
                    throw new Exception("Failed to update stock for product ID: " + item.getProductId());
                }
            }

            // Clear cart
            CartItemDAO.clearCart(userId);

            // Set success message
            session.setAttribute("success", "Order placed successfully! Order ID: " + orderId);
            response.sendRedirect(request.getContextPath() + "/user/order-confirmation?orderId=" + orderId);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error processing order: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/user/checkout");
        }
    }
}