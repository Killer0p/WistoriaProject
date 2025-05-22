package controller.customer;

import dao.ProductDAO;
import dao.WishlistDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import model.User;
import java.io.IOException;

/**
 * Servlet for handling product detail page requests.
 */
@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product/detail"})
public class ProductDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE_PATH = "/WEB-INF/views/customer/customer-template.jsp";
    private static final String DETAIL_PAGE = "/WEB-INF/views/customer/product-detail.jsp";
    private static final String HOME_REDIRECT = "/user/home";

    /**
     * Handles GET requests to display product details.
     * Fetches product and wishlist status, sets template attributes.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            // Get product ID parameter
            String productIdParam = request.getParameter("id");
            if (productIdParam == null || productIdParam.trim().isEmpty()) {
                session.setAttribute("errorMessage", "No product ID provided.");
                response.sendRedirect(request.getContextPath() + HOME_REDIRECT);
                return;
            }

            // Parse product ID
            int productId = Integer.parseInt(productIdParam);
            Product product = ProductDAO.getProductById(productId);

            if (product == null || (product.getIsDeleted() != null && product.getIsDeleted())) {
                session.setAttribute("errorMessage", "Product not found.");
                response.sendRedirect(request.getContextPath() + HOME_REDIRECT);
                return;
            }

            // Check wishlist status for logged-in user
            boolean isInWishlist = false;
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                isInWishlist = WishlistDAO.isProductInWishlist(currentUser.getUserId(), productId);
            }

            // Set attributes for JSP
            request.setAttribute("product", product);
            request.setAttribute("isProductInWishlist", isInWishlist);
            request.setAttribute("pageContent", DETAIL_PAGE);
            request.setAttribute("pageTitle", product.getName() + " - Wistoria Watches");

            // Forward to template
            request.getRequestDispatcher(TEMPLATE_PATH).forward(request, response);
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid product ID format.");
            response.sendRedirect(request.getContextPath() + HOME_REDIRECT);
        } catch (Exception e) {
            session.setAttribute("error", "Failed to load product details. Please try again.");
            response.sendRedirect(request.getContextPath() + HOME_REDIRECT);
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