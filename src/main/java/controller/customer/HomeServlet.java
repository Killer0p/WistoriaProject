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

/**
 * Servlet for handling the user home page.
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/user/home"})
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE_PATH = "/WEB-INF/views/customer/customer-template.jsp";
    private static final String HOME_PAGE = "/WEB-INF/views/customer/home.jsp";
    private static final String PAGE_TITLE = "Wistoria - Your Watch Destination";
    private static final String LOGIN_REDIRECT = "/login";

    /**
     * Handles GET requests to display the user home page.
     * Fetches products and sets template attributes.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Check for valid session
            if (request.getSession().getAttribute("user") == null) {
                request.setAttribute("error", "Please log in to access the home page.");
                response.sendRedirect(request.getContextPath() + LOGIN_REDIRECT);
                return;
            }

            // Fetch products
            List<Product> recommendedProducts = ProductDAO.getRecommendedProducts();
            List<Product> allProducts = ProductDAO.getAllProducts();

            // Set attributes for template and products
            request.setAttribute("pageContent", HOME_PAGE);
            request.setAttribute("pageTitle", PAGE_TITLE);
            request.setAttribute("recommendedProducts", recommendedProducts);
            request.setAttribute("allProducts", allProducts);

            // Forward to user template
            request.getRequestDispatcher(TEMPLATE_PATH).forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Failed to load home page. Please try again.");
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