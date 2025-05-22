package controller.customer;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ContactMessageDAO;
import model.ContactMessage;
import model.User;

/**
 * Servlet for handling contact page functionality.
 */
@WebServlet(name = "ContactServlet", urlPatterns = { "/contact" })
public class ContactPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TEMPLATE_PATH = "/WEB-INF/views/customer/customer-template.jsp";
	private static final String CONTACT_PAGE = "/WEB-INF/views/common/contact.jsp";

	private ContactMessageDAO contactMessageDAO;

	@Override
	public void init() {
		contactMessageDAO = new ContactMessageDAO();
	}

	/**
	 * Handles GET requests to display the contact form page.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Get the current user if logged in
		HttpSession session = request.getSession(false);
		User currentUser = null;

		if (session != null && session.getAttribute("user") != null) {
			currentUser = (User) session.getAttribute("user");
			request.setAttribute("user", currentUser);
		}

		// Set page attributes for template
		request.setAttribute("pageTitle", "Contact Us - Wistoria Watches");
		request.setAttribute("pageContent", CONTACT_PAGE);

		// Forward to the template
		request.getRequestDispatcher(TEMPLATE_PATH).forward(request, response);
	}

	/**
	 * Handles POST requests to process the contact form submission.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		// Get form parameters
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");

		// Form validation
		if (name == null || email == null || subject == null || message == null || name.trim().isEmpty()
				|| email.trim().isEmpty() || subject.trim().isEmpty() || message.trim().isEmpty()) {

			request.setAttribute("error", "All fields are required. Please complete the form.");
			request.setAttribute("pageTitle", "Contact Us - Wistoria Watches");
			request.setAttribute("pageContent", CONTACT_PAGE);
			request.getRequestDispatcher(TEMPLATE_PATH).forward(request, response);
			return;
		}

		// Basic email validation
		if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			request.setAttribute("error", "Please enter a valid email address.");
			request.setAttribute("pageTitle", "Contact Us - Wistoria Watches");
			request.setAttribute("pageContent", CONTACT_PAGE);
			request.getRequestDispatcher(TEMPLATE_PATH).forward(request, response);
			return;
		}

		try {
			// Create ContactMessage object
			ContactMessage contactMessage = new ContactMessage();
			contactMessage.setName(name);
			contactMessage.setEmail(email);
			contactMessage.setSubject(subject);
			contactMessage.setMessage(message);

			// Set user ID if logged in, otherwise set to 0 or a negative value
			if (session != null && session.getAttribute("user") != null) {
				User currentUser = (User) session.getAttribute("user");
				contactMessage.setUserId(currentUser.getUserId());
			} else {
				// Set userId to 0 or a negative value for non-logged in users
				// The DAO handles this by using setNull when userId <= 0
				contactMessage.setUserId(0);
			}

			// Save the message
			boolean saved = contactMessageDAO.saveMessage(contactMessage);

			if (saved) {
				// Set success message in session
				session.setAttribute("success", "Your message has been sent successfully. We'll get back to you soon!");

				// Redirect to prevent form resubmission
				response.sendRedirect(request.getContextPath() + "/contact");
			} else {
				request.setAttribute("error", "Failed to send message. Please try again later.");
				request.setAttribute("pageTitle", "Contact Us - Wistoria Watches");
				request.setAttribute("pageContent", CONTACT_PAGE);
				request.getRequestDispatcher(TEMPLATE_PATH).forward(request, response);
			}

		} catch (Exception e) {
			// Log the exception
			e.printStackTrace();

			// Show error message
			request.setAttribute("error", "An error occurred while processing your request. Please try again later.");
			request.setAttribute("pageTitle", "Contact Us - Wistoria Watches");
			request.setAttribute("pageContent", CONTACT_PAGE);
			request.getRequestDispatcher(TEMPLATE_PATH).forward(request, response);
		}
	}
}