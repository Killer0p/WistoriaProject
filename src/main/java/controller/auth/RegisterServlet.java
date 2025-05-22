package controller.auth;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import java.io.IOException;

/**
 * Servlet for handling user registration requests.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String REGISTER_VIEW = "/WEB-INF/views/common/register.jsp";
    private static final String LOGIN_REDIRECT = "/login?registered=true";

    /**
     * Handles GET requests to display the registration page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Forward to registration page
            request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
        } catch (ServletException | IOException e) {
            request.setAttribute("error", "Failed to load registration page. Please try again.");
            request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
        }
    }

    /**
     * Handles POST requests to register a new user.
     * Validates input, checks for existing email, and saves user.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate input fields
        if (name == null || email == null || password == null ||
            name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
            return;
        }

        try {
            // Check if email is already registered
            if (UserDAO.isEmailExists(email)) {
                request.setAttribute("error", "An account with this email already exists.");
                request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
                return;
            }

            // Create and save new user
            User newUser = new User(name, email, password);
            boolean isCreated = UserDAO.registerUser(newUser);

            if (isCreated) {
                // Set success message and redirect to login
                request.setAttribute("success", "Registration successful. Please log in.");
                response.sendRedirect(request.getContextPath() + LOGIN_REDIRECT);
            } else {
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Something went wrong. Please try again.");
            request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
        }
    }
}