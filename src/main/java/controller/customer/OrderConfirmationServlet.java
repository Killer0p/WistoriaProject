package controller.customer;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.OrderDAO;
import model.Order;
import model.User;

@WebServlet("/user/order-confirmation")
public class OrderConfirmationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        // Check if user is logged in
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = currentUser.getUserId();
        String orderIdParam = request.getParameter("orderId");

        // Validate orderId parameter
        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Order ID is missing.");
            response.sendRedirect(request.getContextPath() + "/user/home");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);
            Order order = OrderDAO.getOrderById(orderId);

            // Verify order exists and belongs to user
            if (order == null || order.getUserId() != userId) {
                session.setAttribute("errorMessage", "Order not found or access denied.");
                response.sendRedirect(request.getContextPath() + "/user/healthcare");
                return;
            }

            // Convert LocalDateTime to Date for JSP compatibility
            Date orderDateAsDate = null;
            if (order.getOrderDate() != null) {
                orderDateAsDate = Date.from(order.getOrderDate().atZone(ZoneId.systemDefault()).toInstant());
            }

            // Get shipping info
            Object shippingInfo;
            if (order.getShippingInfo() != null) {
                // Use the ShippingInfo object directly
                shippingInfo = order.getShippingInfo();
            } else {
                // Fallback to Map from OrderDAO if ShippingInfo is not populated
                shippingInfo = OrderDAO.getShippingInfo(orderId);
            }

            // Set attributes for JSP
            request.setAttribute("order", order);
            request.setAttribute("orderDateAsDate", orderDateAsDate);
            request.setAttribute("shippingInfo", shippingInfo);
            request.setAttribute("pageTitle", "Order Confirmation");
            request.setAttribute("pageContent", "/WEB-INF/views/customer/order-confirmation.jsp");

            // Clear error message if previously set
            session.removeAttribute("errorMessage");

            // Forward to template
            request.getRequestDispatcher("/WEB-INF/views/customer/customer-template.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid order ID format.");
            response.sendRedirect(request.getContextPath() + "/user/home");
        } catch (Exception e) {
            // Log the error for debugging
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading the order confirmation.");
            response.sendRedirect(request.getContextPath() + "/user/home");
        }
    }
}