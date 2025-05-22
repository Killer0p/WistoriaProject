package controller.admin;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ContactMessageDAO;
import model.ContactMessage;
/**
 * Servlet for handling admin contact message management.
 */
@WebServlet(name = "AdminContactMessageServlet", urlPatterns = { "/admin/contact-messages" })
public class AdminContactMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ADMIN_TEMPLATE_PATH = "/WEB-INF/views/admin/admin-template.jsp";
	private static final String MESSAGE_LIST_PAGE = "/WEB-INF/views/admin/message-list.jsp";
	private static final String MESSAGE_DETAIL_PAGE = "/WEB-INF/views/admin/message-detail.jsp";
	private static final int MESSAGES_PER_PAGE = 10;

	private ContactMessageDAO contactMessageDAO;

	@Override
	public void init() {
		contactMessageDAO = new ContactMessageDAO();
	}

	/**
	 * Handles GET requests to display contact messages or message details.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String action = request.getParameter("action");
		if (action == null) {
			action = "list";
		}

		switch (action) {
		case "view":
			viewMessage(request, response);
			break;
		case "delete":
			deleteMessage(request, response);
			break;
		case "list":
		default:
			listMessages(request, response);
			break;
		}
	}

	/**
	 * Displays a paginated list of contact messages.
	 */
	private void listMessages(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Get pagination parameters
			int page = 1;
			String pageStr = request.getParameter("page");
			if (pageStr != null && !pageStr.isEmpty()) {
				try {
					page = Integer.parseInt(pageStr);
					if (page < 1) {
						page = 1;
					}
				} catch (NumberFormatException e) {
					// Ignore and use default page 1
				}
			}

			// Calculate offset
			int offset = (page - 1) * MESSAGES_PER_PAGE;

			// Get messages for current page
			List<ContactMessage> messages = contactMessageDAO.getMessages(offset, MESSAGES_PER_PAGE);

			// Get total count for pagination
			int totalMessages = contactMessageDAO.getTotalMessageCount();
			int totalPages = (int) Math.ceil((double) totalMessages / MESSAGES_PER_PAGE);

			// Set attributes for the view
			request.setAttribute("messages", messages);
			request.setAttribute("currentPage", page);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("pageTitle", "Contact Messages - Admin Panel");
			request.setAttribute("pageContent", MESSAGE_LIST_PAGE);

			// Forward to template
			request.getRequestDispatcher(ADMIN_TEMPLATE_PATH).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Error retrieving contact messages: " + e.getMessage());
			request.setAttribute("pageTitle", "Error - Admin Panel");
			request.setAttribute("pageContent", MESSAGE_LIST_PAGE);
			request.getRequestDispatcher(ADMIN_TEMPLATE_PATH).forward(request, response);
		}
	}

	/**
	 * Displays a single contact message.
	 */
	private void viewMessage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Get message ID from request
			String messageIdStr = request.getParameter("id");
			if (messageIdStr == null || messageIdStr.isEmpty()) {
				response.sendRedirect(request.getContextPath() + "/admin/contact-messages");
				return;
			}

			int messageId = Integer.parseInt(messageIdStr);
			ContactMessage message = contactMessageDAO.getMessageById(messageId);

			if (message == null) {
				request.setAttribute("error", "Message not found");
				request.setAttribute("pageTitle", "Error - Admin Panel");
				request.setAttribute("pageContent", MESSAGE_LIST_PAGE);
				listMessages(request, response);
				return;
			}

			// Set attributes for the view
			request.setAttribute("message", message);
			request.setAttribute("pageTitle", "Message Details - Admin Panel");
			request.setAttribute("pageContent", MESSAGE_DETAIL_PAGE);

			// Forward to template
			request.getRequestDispatcher(ADMIN_TEMPLATE_PATH).forward(request, response);
		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/admin/contact-messages");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Error retrieving message details: " + e.getMessage());
			request.setAttribute("pageTitle", "Error - Admin Panel");
			request.setAttribute("pageContent", MESSAGE_LIST_PAGE);
			listMessages(request, response);
		}
	}

	/**
	 * Deletes a contact message.
	 */
	private void deleteMessage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Get message ID from request
			String messageIdStr = request.getParameter("id");
			if (messageIdStr == null || messageIdStr.isEmpty()) {
				response.sendRedirect(request.getContextPath() + "/admin/contact-messages");
				return;
			}

			int messageId = Integer.parseInt(messageIdStr);
			boolean deleted = contactMessageDAO.deleteMessage(messageId);

			HttpSession session = request.getSession();
			if (deleted) {
				session.setAttribute("success", "Message deleted successfully");
			} else {
				session.setAttribute("error", "Failed to delete message");
			}

			// Redirect to message list
			response.sendRedirect(request.getContextPath() + "/admin/contact-messages");
		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/admin/contact-messages");
		} catch (Exception e) {
			e.printStackTrace();
			HttpSession session = request.getSession();
			session.setAttribute("error", "Error deleting message: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/contact-messages");
		}
	}
}