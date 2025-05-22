package controller.admin;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.OrderDAO;
import model.Order;
import model.ShippingInfo;
import model.User;

@WebServlet(name = "OrderPageServlet", value = "/admin/order")
public class OrderPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int ITEMS_PER_PAGE = 10;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// Get page number, default to 1
			int page = 1;
			String pageParam = request.getParameter("page");
			if (pageParam != null && !pageParam.isEmpty()) {
				try {
					page = Integer.parseInt(pageParam);
					if (page < 1)
						page = 1;
				} catch (NumberFormatException e) {
					page = 1;
				}
			}

			// Get filter parameters
			String status = request.getParameter("status");
			String sortBy = request.getParameter("sortBy");

			// Parse date parameters
			Date dateFrom = null;
			Date dateTo = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			String dateFromParam = request.getParameter("dateFrom");
			if (dateFromParam != null && !dateFromParam.isEmpty()) {
				try {
					dateFrom = dateFormat.parse(dateFromParam);
				} catch (ParseException e) {
					// Ignore invalid format
				}
			}

			String dateToParam = request.getParameter("dateTo");
			if (dateToParam != null && !dateToParam.isEmpty()) {
				try {
					Date parsedDateTo = dateFormat.parse(dateToParam);
					// Extend dateTo to include entire day
					dateTo = new Date(parsedDateTo.getTime() + 24 * 60 * 60 * 1000);
				} catch (ParseException e) {
					// Ignore invalid format
				}
			}

			// Status update
			String updateStatus = request.getParameter("updateStatus");
			String orderId = request.getParameter("orderId");

			if (updateStatus != null && orderId != null) {
				try {
					int id = Integer.parseInt(orderId);
					OrderDAO.updateOrderStatus(id, updateStatus);
					response.sendRedirect(
							request.getContextPath() + "/admin/order?status=" + (status != null ? status : "")
									+ "&sortBy=" + (sortBy != null ? sortBy : "") + "&page=" + page);
					return;
				} catch (Exception e) {
					request.setAttribute("errorMessage", "Failed to update order status: " + e.getMessage());
				}
			}

			// Get filtered/paginated orders
			List<Order> orders = OrderDAO.getOrders(page, ITEMS_PER_PAGE, status, sortBy, dateFrom, dateTo);

			// Validate orders and log data
			for (Order order : orders) {
				if (order == null || order.getOrderId() == null) {
					System.out.println("Null or invalid order in list");
					continue;
				}
				if (order.getUser() == null || order.getUser().getName() == null) {
					User defaultUser = new User();
					defaultUser.setName("Unknown User");
					defaultUser.setEmail("N/A");
					defaultUser.setPhoneNumber("N/A");
					order.setUser(defaultUser);
					System.out.println("Set default user for order_id=" + order.getOrderId());
				}
				if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
					System.out.println("No order details for order_id=" + order.getOrderId());
				}
				if (order.getShippingInfo() == null || order.getShippingInfo().getAddress() == null) {
					ShippingInfo defaultInfo = new ShippingInfo();
					defaultInfo.setAddress("N/A");
					defaultInfo.setContact("N/A");
					defaultInfo.setShippingMethod("N/A");
					order.setShippingInfo(defaultInfo);
					System.out.println("Set default shipping info for order_id=" + order.getOrderId());
				}
			}

			int totalOrders = OrderDAO.getTotalOrderCount(status, dateFrom, dateTo);
			int totalPages = (int) Math.ceil((double) totalOrders / ITEMS_PER_PAGE);

			// Set attributes for view
			request.setAttribute("orders", orders);
			request.setAttribute("currentPage", page);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("status", status);
			request.setAttribute("sortBy", sortBy);
			request.setAttribute("dateFrom", dateFromParam);
			request.setAttribute("dateTo", dateToParam);

			// Forward to template
			String pageTitle = "Wistoria - Order Management";
			request.setAttribute("activeNavItem", "order");
			request.setAttribute("pageContent", "/WEB-INF/views/admin/order-management.jsp");
			request.setAttribute("pageTitle", pageTitle);
			request.getRequestDispatcher("/WEB-INF/views/admin/admin-template.jsp").forward(request, response);

		} catch (Exception e) {
			System.err.println("Error in OrderPageServlet: " + e.getMessage());
			request.setAttribute("errorMessage", "Error loading orders: " + e.getMessage());
			request.setAttribute("orders", List.of());
			request.setAttribute("currentPage", 1);
			request.setAttribute("totalPages", 1);
			request.setAttribute("status", request.getParameter("status"));
			request.setAttribute("sortBy", request.getParameter("sortBy"));
			request.setAttribute("dateFrom", request.getParameter("dateFrom"));
			request.setAttribute("dateTo", request.getParameter("dateTo"));
			String pageTitle = "Wistoria - Order Management";
			request.setAttribute("activeNavItem", "order");
			request.setAttribute("pageContent", "/WEB-INF/views/admin/order-management.jsp");
			request.setAttribute("pageTitle", pageTitle);
			request.getRequestDispatcher("/WEB-INF/views/admin/admin-template.jsp").forward(request, response);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}