package controller.admin;

import dao.OrderDAO;
import dao.ProductDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Servlet for handling the admin dashboard view. Displays total products,
 * orders, users, revenue, and monthly sales data.
 */
@WebServlet(name = "AdminDashboardServlet", value = "/admin/dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Handles GET requests to display the admin dashboard. Fetches statistics and
	 * monthly sales data, then forwards to the dashboard JSP.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// Fetch statistics from DAOs
			int totalProducts = ProductDAO.getAllProducts().size();
			int totalOrders = OrderDAO.getTotalOrderCount();
			int totalUsers = UserDAO.getTotalUserCount();
			double totalRevenue = OrderDAO.getTotalRevenue();

			// Fetch monthly sales data for the last 6 months
			List<String> months = OrderDAO.getLast6MonthsLabels();
			List<Double> revenue = OrderDAO.getLast6MonthsRevenue();

			// Format totalRevenue to two decimal places
			DecimalFormat df = new DecimalFormat("#,##0.00");
			String formattedRevenue = df.format(totalRevenue);

			// Set request attributes for JSP
			req.setAttribute("totalProducts", totalProducts);
			req.setAttribute("totalOrders", totalOrders);
			req.setAttribute("totalUsers", totalUsers);
			req.setAttribute("totalRevenue", formattedRevenue);

			// Set chart data
			req.setAttribute("monthLabels", months);
			req.setAttribute("monthlyRevenue", revenue);

			// Set success message for successful data load
			req.setAttribute("success", "Dashboard data loaded successfully.");

			// Set page attributes for template
			req.setAttribute("activeNavItem", "dashboard");
			req.setAttribute("pageContent", "/WEB-INF/views/admin/dashboard.jsp");
			req.setAttribute("pageTitle", "Wistoria - Admin Dashboard");

			// Forward to admin template
			req.getRequestDispatcher("/WEB-INF/views/admin/admin-template.jsp").forward(req, resp);

		} catch (Exception e) {
			// Log error and set error message
			e.printStackTrace();
			req.setAttribute("error", "Failed to load dashboard data: " + e.getMessage());
			// Set page attributes for error display
			req.setAttribute("activeNavItem", "dashboard");
			req.setAttribute("pageContent", "/views/admin/dashboard.jsp");
			req.setAttribute("pageTitle", "Wistoria - Admin Dashboard");
			req.getRequestDispatcher("/views/admin/admin-template.jsp").forward(req, resp);
		}
	}

	/**
	 * Handles POST requests by delegating to doGet. Can be extended for future form
	 * submissions (e.g., dashboard settings).
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}