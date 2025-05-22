package controller.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.PasswordSecurityUtil;
import dao.UserDAO;

@WebServlet(name = "ProfileServlet", value = "/profile")
public class ProfileServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if (user == null) {
			session.setAttribute("error", "Please log in to view your profile.");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		// Fetch fresh user data
		User updatedUser = UserDAO.getUserById(user.getUserId());
		if (updatedUser == null) {
			session.setAttribute("error", "User not found.");
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		session.setAttribute("user", updatedUser);

		// Set attributes for JSP
		request.setAttribute("pageContent", "/WEB-INF/views/common/profile.jsp");
		request.setAttribute("pageTitle", "Wistoria - My Profile");
		request.setAttribute("activeNavItem", "profile");

		// Forward to appropriate template based on role
		String template = updatedUser.getRole().equals("admin") ? "/WEB-INF/views/admin/admin-template.jsp"
				: "/WEB-INF/views/customer/customer-template.jsp";
		request.getRequestDispatcher(template).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if (user == null) {
			session.setAttribute("error", "Please log in to update your profile.");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String action = request.getParameter("action");
		try {
			if ("updateProfile".equals(action)) {
				// Update name and phone number
				String name = request.getParameter("name");
				String phoneNumber = request.getParameter("phoneNumber");

				// Server-side validation
				if (name == null || name.trim().isEmpty()) {
					session.setAttribute("error", "Name is required.");
					response.sendRedirect(request.getContextPath() + "/profile");
					return;
				}
				if (phoneNumber != null && !phoneNumber.matches("^\\+?[1-9]\\d{1,14}$")) {
					session.setAttribute("error", "Invalid phone number format.");
					response.sendRedirect(request.getContextPath() + "/profile");
					return;
				}

				User updatedUser = new User();
				updatedUser.setUserId(user.getUserId());
				updatedUser.setName(name.trim());
				updatedUser.setPhoneNumber(phoneNumber != null ? phoneNumber.trim() : null);

				if (UserDAO.updateUser(updatedUser)) {
					// Update session user
					User freshUser = UserDAO.getUserById(user.getUserId());
					session.setAttribute("user", freshUser);
					session.setAttribute("success", "Profile updated successfully.");
				} else {
					session.setAttribute("error", "Failed to update profile.");
				}
			} else if ("updatePassword".equals(action)) {
				// Update password
				String currentPassword = request.getParameter("currentPassword");
				String newPassword = request.getParameter("newPassword");
				String confirmPassword = request.getParameter("confirmPassword");

				// Server-side validation
				if (currentPassword == null || newPassword == null || confirmPassword == null) {
					session.setAttribute("error", "All password fields are required.");
					response.sendRedirect(request.getContextPath() + "/profile");
					return;
				}
				if (!newPassword.equals(confirmPassword)) {
					session.setAttribute("error", "New password and confirmation do not match.");
					response.sendRedirect(request.getContextPath() + "/profile");
					return;
				}
				if (newPassword.length() < 8) {
					session.setAttribute("error", "New password must be at least 8 characters long.");
					response.sendRedirect(request.getContextPath() + "/profile");
					return;
				}

				// Verify current password
				User dbUser = UserDAO.getUserById(user.getUserId());
				if (dbUser == null || !PasswordSecurityUtil.validatePassword(currentPassword, dbUser.getPassword())) {
					session.setAttribute("error", "Current password is incorrect.");
					response.sendRedirect(request.getContextPath() + "/profile");
					return;
				}

				if (UserDAO.updatePassword(user.getUserId(), newPassword)) {
					session.setAttribute("success", "Password updated successfully.");
				} else {
					session.setAttribute("error", "Failed to update password.");
				}
			} else {
				session.setAttribute("error", "Invalid action.");
			}
		} catch (Exception e) {
			System.err.println("Error updating profile: " + e.getMessage());
			session.setAttribute("error", "An error occurred while updating your profile.");
		}

		response.sendRedirect(request.getContextPath() + "/profile");
	}
}