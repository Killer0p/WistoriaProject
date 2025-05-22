package controller.admin;

import dao.ProductDAO;
import model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for managing products in the admin panel.
 * Handles CRUD operations (Create, Read, Update, Delete) and search functionality for products.
 * Supports file uploads for product images with validation and pagination for product listing.
 */
@WebServlet("/admin/product")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 5, // 5MB
        maxRequestSize = 1024 * 1024 * 20 // 20MB
)
public class ProductManagementServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIR = "uploads";
    private static final int ITEMS_PER_PAGE = 10; // Number of products per page

    /**
     * Handles GET requests for product management.
     * Actions include listing products, viewing a product, editing, adding, deleting, and searching.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "view":
                    viewProduct(request, response);
                    break;
                case "add":
                    showEditForm(request, response, null);
                    break;
                case "edit":
                    showEditForm(request, response, null);
                    break;
                case "delete":
                    deleteProduct(request, response);
                    break;
                case "search":
                    searchListProducts(request, response);
                    break;
                default:
                    listProducts(request, response);
                    break;
            }
        } catch (Exception ex) {
            request.setAttribute("error", "An unexpected error occurred: " + ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/error.jsp").forward(request, response);
        }
    }

    /**
     * Handles POST requests for adding or updating products.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add":
                    addProduct(request, response);
                    break;
                case "update":
                    updateProduct(request, response);
                    break;
                default:
                    request.setAttribute("error", "Invalid action specified.");
                    response.sendRedirect("product");
                    break;
            }
        } catch (Exception ex) {
            request.setAttribute("error", "An unexpected error occurred: " + ex.getMessage());
            showEditForm(request, response, ex.getMessage());
        }
    }

    /**
     * Lists products with pagination.
     * Sets attributes for products, pagination details, and page metadata.
     */
    private void listProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = validatePageNumber(request.getParameter("page"));

        try {
            // Fetch products with pagination
            List<Product> products = ProductDAO.getProductsWithPagination(page, ITEMS_PER_PAGE);
            int totalProducts = ProductDAO.getTotalProductCount();
            int totalPages = (int) Math.ceil((double) totalProducts / ITEMS_PER_PAGE);

            // Set request attributes for JSP
            request.setAttribute("products", products);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("activeNavItem", "product");
            request.setAttribute("pageTitle", "Wistoria - Product Management");
            request.setAttribute("pageContent", "/WEB-INF/views/admin/product-management.jsp");

            request.getRequestDispatcher("/WEB-INF/views/admin/admin-template.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException("Error listing products", ex);
        }
    }

    /**
     * Searches for products based on a search term and displays results.
     */
    private void searchListProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            request.setAttribute("error", "Please provide a search term.");
            listProducts(request, response);
            return;
        }

        try {
            List<Product> products = ProductDAO.searchProducts(searchTerm.trim());

            request.setAttribute("products", products);
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("totalPages", 1); // Search results shown on a single page
            request.setAttribute("currentPage", 1);
            request.setAttribute("activeNavItem", "product");
            request.setAttribute("pageTitle", "Wistoria - Product Search Results");
            request.setAttribute("pageContent", "/WEB-INF/views/admin/product-management.jsp");

            request.getRequestDispatcher("/WEB-INF/views/admin/admin-template.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException("Error searching products", ex);
        }
    }

    /**
     * Displays details of a single product.
     */
    private void viewProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = validateProductId(request.getParameter("id"));
        try {
            Product product = ProductDAO.getProductById(id);

            if (product == null) {
                request.setAttribute("error", "Product not found.");
                listProducts(request, response);
                return;
            }

            request.setAttribute("product", product);
            request.setAttribute("activeNavItem", "product");
            request.setAttribute("pageTitle", "View Product - Wistoria");
            request.setAttribute("pageContent", "/WEB-INF/views/admin/product-detail.jsp");

            request.getRequestDispatcher("/WEB-INF/views/admin/admin-template.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException("Error viewing product", ex);
        }
    }

    /**
     * Shows the product edit/add form.
     * @param error Optional error message to display on the form.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException {
        int id = 0;
        String action = request.getParameter("action");
        if (request.getParameter("id") != null) {
            id = validateProductId(request.getParameter("id"));
        }

        try {
            Product product = id > 0 ? ProductDAO.getProductById(id) : new Product();

            if (id > 0 && product == null) {
                request.setAttribute("error", "Product not found.");
                listProducts(request, response);
                return;
            }

            request.setAttribute("product", product);
            request.setAttribute("action", action != null ? action : "add");
            if (error != null) {
                request.setAttribute("error", error);
            }
            request.setAttribute("activeNavItem", "product");
            request.setAttribute("pageTitle", id > 0 ? "Edit Product - Wistoria" : "Add Product - Wistoria");
            request.setAttribute("pageContent", "/WEB-INF/views/admin/product-form.jsp");

            request.getRequestDispatcher("/WEB-INF/views/admin/admin-template.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException("Error displaying edit form", ex);
        }
    }

    /**
     * Adds a new product to the database.
     */
    private void addProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = extractProductFromRequest(request);
        String validationError = validateProduct(product);
        if (validationError != null) {
            showEditForm(request, response, validationError);
            return;
        }

        try {
            ProductDAO.addProduct(product);
            request.getSession().setAttribute("success", "Product added successfully.");
            response.sendRedirect("product?action=list");
        } catch (Exception ex) {
            throw new ServletException("Error adding product", ex);
        }
    }

    /**
     * Updates an existing product in the database.
     */
    private void updateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = extractProductFromRequest(request);
        product.setProductId(validateProductId(request.getParameter("productId")));
        String validationError = validateProduct(product);
        if (validationError != null) {
            showEditForm(request, response, validationError);
            return;
        }

        try {
            ProductDAO.updateProduct(product);
            request.getSession().setAttribute("success", "Product updated successfully.");
            response.sendRedirect("product?action=list");
        } catch (Exception ex) {
            throw new ServletException("Error updating product", ex);
        }
    }

    /**
     * Deletes a product from the database.
     */
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = validateProductId(request.getParameter("id"));
        try {
            Product product = ProductDAO.getProductById(id);
            if (product == null) {
                request.getSession().setAttribute("error", "Product not found.");
            } else {
                ProductDAO.deleteProduct(id);
                request.getSession().setAttribute("success", "Product deleted successfully.");
            }
            response.sendRedirect("product");
        } catch (Exception ex) {
            throw new ServletException("Error deleting product", ex);
        }
    }

    /**
     * Extracts product details from the HTTP request, including file upload handling.
     */
    private Product extractProductFromRequest(HttpServletRequest request) throws IOException, ServletException {
        Product product = new Product();

        try {
            // Extract form fields
            product.setName(request.getParameter("name"));
            product.setDescription(request.getParameter("description"));
            product.setPrice(parseDouble(request.getParameter("price"), "Price"));
            product.setStockQuantity(parseInt(request.getParameter("stock_quantity"), "Stock Quantity"));
            product.setCategory(request.getParameter("category"));
            product.setBrand(request.getParameter("brand"));
            product.setCaseMaterial(request.getParameter("material"));
            product.setMovementType(request.getParameter("movement"));
            product.setWaterResistanceMeters(parseInt(request.getParameter("water_resistance"), "Water Resistance"));
            product.setWarrantyMonths(parseInt(request.getParameter("warranty_months"), "Warranty Months"));

            // Handle image upload
            Part filePart = request.getPart("watchImage");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = System.currentTimeMillis() + "_" + extractFileName(filePart);
                String appPath = request.getServletContext().getRealPath("");
                String uploadPath = appPath + File.separator + UPLOAD_DIR;

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                filePart.write(uploadPath + File.separator + fileName);
                String imageUrl = request.getContextPath() + "/" + UPLOAD_DIR + "/" + fileName;
                product.setImageUrl(imageUrl);
            } else {
                String existingUrl = request.getParameter("existing_image_url");
                if (existingUrl != null && !existingUrl.isEmpty()) {
                    product.setImageUrl(existingUrl);
                }
            }
        } catch (Exception ex) {
            throw new ServletException("Error extracting product details: " + ex.getMessage(), ex);
        }

        return product;
    }

    /**
     * Extracts the file name from the content-disposition header of a file part.
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 2, token.length() - 1);
            }
        }
        return "default.png";
    }

    /**
     * Validates the page number parameter, defaults to 1 if invalid.
     */
    private int validatePageNumber(String pageParam) {
        int page = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
            	e.printStackTrace();
            }
        }
        return page;
    }

    /**
     * Validates the product ID parameter, throws an exception if invalid.
     */
    private int validateProductId(String idParam) throws ServletException {
        try {
            return Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid product ID: " + idParam);
        }
    }

    /**
     * Validates product data before saving.
     * @return Error message if validation fails, null if valid.
     */
    private String validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            return "Product name is required.";
        }
        if (product.getPrice() < 0) {
            return "Price cannot be negative.";
        }
        if (product.getStockQuantity() < 0) {
            return "Stock quantity cannot be negative.";
        }
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            return "Category is required.";
        }
        if (product.getBrand() == null || product.getBrand().trim().isEmpty()) {
            return "Brand is required.";
        }
        return null;
    }

    /**
     * Parses a string to a double, with error handling.
     */
    private double parseDouble(String value, String fieldName) throws ServletException {
        try {
            return value != null && !value.isEmpty() ? Double.parseDouble(value) : 0.0;
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid " + fieldName + ": " + value);
        }
    }

    /**
     * Parses a string to an integer, with error handling.
     */
    private int parseInt(String value, String fieldName) throws ServletException {
        try {
            return value != null && !value.isEmpty() ? Integer.parseInt(value) : 0;
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid " + fieldName + ": " + value);
        }
    }
}