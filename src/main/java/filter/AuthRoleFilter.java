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
import model.User;

@WebFilter(filterName = "AuthRoleFilter", urlPatterns = { "/admin/*", "/user/*" })
public class AuthRoleFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		try {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			HttpSession session = request.getSession(false);

			String ctx = request.getContextPath();

			// check session and user
			if (session != null && session.getAttribute("user") != null) {
				User user = (User) session.getAttribute("user");

				String role = user.getRole(); // get user role
				String path = request.getRequestURI();

				// check access by role
				if (path.startsWith(ctx + "/admin") && !"admin".equals(role)) {
					response.sendRedirect(ctx + "/unauthorized");
				} else if (path.startsWith(ctx + "/user") && !"user".equals(role)) {
					response.sendRedirect(ctx + "/unauthorized");
				} else {
					filterChain.doFilter(request, response); // allow
				}

			} else {
				response.sendRedirect(ctx + "/login"); // no session
			}

		} catch (Exception e) {
			System.err.println("AuthRoleFilter error: " + e.getMessage());
			((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Internal error in role filter.");
		}
	}
}
