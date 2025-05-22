package controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for handling user logout requests.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String LOGIN_REDIRECT = "/login";

    /**
     * Handles GET requests to log out the user.
     * Invalidates the session and redirects to the login page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get session without creating a new one
            HttpSession session = request.getSession(false);

            if (session != null) {
                // Clear user attribute and invalidate session
                session.removeAttribute("user");
                session.invalidate();
            }

            // Set success message for login page
            request.setAttribute("success", "Logout successful.");

            // Redirect to login page
            response.sendRedirect(request.getContextPath() + LOGIN_REDIRECT);
        } catch (IOException e) {
            request.setAttribute("error", "Failed to log out. Please try again.");
            response.sendRedirect(request.getContextPath() + LOGIN_REDIRECT);
        }
    }

    /**
     * Handles POST requests by forwarding to doGet.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}