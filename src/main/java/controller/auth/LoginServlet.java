package controller.auth;

import dao.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import java.io.IOException;

/**
 * Servlet for handling user login requests.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String LOGIN_VIEW = "/WEB-INF/views/common/login.jsp";
    private static final String USER_COOKIE_NAME = "userCookieEmail";
    private static final int COOKIE_MAX_AGE_SECONDS = 7 * 24 * 60 * 60; // 7 days

    /**
     * Handles GET requests to display the login page.
     * Retrieves user email from cookie if available.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Check for user email cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (USER_COOKIE_NAME.equals(cookie.getName())) {
                        request.setAttribute("userCookieEmail", cookie.getValue());
                        break;
                    }
                }
            }

            // Forward to login page
            RequestDispatcher dispatcher = request.getRequestDispatcher(LOGIN_VIEW);
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            request.setAttribute("error", "Failed to load login page. Please try again.");
            request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
        }
    }

    /**
     * Handles POST requests to authenticate users.
     * Validates credentials, sets session, manages remember me cookie, and redirects by role.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Authenticate user
            User user = UserDAO.loginUser(email, password);

            if (user != null) {
                // Set user in session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                // Handle remember me cookie
                String rememberMe = request.getParameter("rememberMe");
                Cookie emailCookie = new Cookie(USER_COOKIE_NAME, "on".equals(rememberMe) ? email : "");
                emailCookie.setMaxAge("on".equals(rememberMe) ? COOKIE_MAX_AGE_SECONDS : 0);
                response.addCookie(emailCookie);

                // Set success message
                request.setAttribute("success", "Login successful.");

                // Redirect based on role
                String redirectUrl = "admin".equalsIgnoreCase(user.getRole())
                        ? request.getContextPath() + "/admin/dashboard"
                        : request.getContextPath() + "/user/home";
                response.sendRedirect(redirectUrl);
            } else {
                // Invalid credentials
                request.setAttribute("error", "Invalid email or password.");
                request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "An internal error occurred. Please try again.");
            request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
        }
    }
}