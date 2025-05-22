package filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "/admin/*", "/user/*", "/home" })
public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		try {
			// Cast to HTTP-specific request and response objects
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;

			// Get the current session without creating a new one
			HttpSession session = request.getSession(false);

			// Check if the user is logged in 
			boolean loggedIn = session != null && session.getAttribute("user") != null;

			if (loggedIn) {
				// Allow access to the requested resource
				filterChain.doFilter(request, response);
			} else {
				// Redirect unauthenticated users to the login page
				response.sendRedirect(request.getContextPath() + "/login");
			}

		} catch (Exception e) {
			// Log the error
			System.err.println("AuthFilter error: " + e.getMessage());

			// Send a generic server error response
			((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Internal server error in authentication filter.");
		}
	}
}
