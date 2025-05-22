package controller.customer;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling product search requests.
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(SearchServlet.class.getName());
    private static final String TEMPLATE_PATH = "/WEB-INF/views/customer/customer-template.jsp";
    private static final String SEARCH_PAGE = "/WEB-INF/views/customer/search-result.jsp";
    private static final String ERROR_MESSAGE = "error";

    /**
     * Handles GET requests to perform product search and display results.
     * Fetches products based on query, sets attributes, and forwards to template.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("query");
        String pageTitle = "Search Results - Wistoria Watches";

        try {
            // Validate search query
            if (query == null || query.trim().isEmpty()) {
                request.getSession().setAttribute(ERROR_MESSAGE, "Search query cannot be empty.");
            } else {
                // Perform search
                List<Product> searchResults = ProductDAO.searchProducts(query.trim());
                request.setAttribute("results", searchResults);
                request.setAttribute("searchQuery", query.trim());
                // Clear any previous error message
                request.getSession().removeAttribute(ERROR_MESSAGE);
            }

            // Set template attributes
            request.setAttribute("pageContent", SEARCH_PAGE);
            request.setAttribute("pageTitle", pageTitle);

            // Forward to template
            request.getRequestDispatcher(TEMPLATE_PATH).forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error performing search for query: " + query, e);
            request.getSession().setAttribute(ERROR_MESSAGE, "An error occurred during search. Please try again.");
            request.setAttribute("pageContent", SEARCH_PAGE);
            request.setAttribute("pageTitle", pageTitle);
            request.getRequestDispatcher(TEMPLATE_PATH).forward(request, response);
        }
    }

    /**
     * Handles POST requests by delegating to doGet.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}