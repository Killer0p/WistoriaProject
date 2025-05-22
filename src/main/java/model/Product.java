package model;

public class Product {
    private Integer productId;
    private String name;
    private String description;
    private double price;
    private Integer stockQuantity;
    private String category;
    private String imageUrl;
    private String caseMaterial;
    private String movementType;
    private String brand;
    private Integer waterResistanceMeters;
    private Integer warrantyMonths;
    private Boolean isDeleted;

    // Constructors
    public Product() {
    }

    // Constructor with all fields
    public Product(Integer productId, String name, String description, double price, Integer stockQuantity,
                   String category, String imageUrl, String caseMaterial, String movementType, String brand,
                   Integer waterResistanceMeters, Integer warrantyMonths, Boolean isDeleted) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.imageUrl = imageUrl;
        this.caseMaterial = caseMaterial;
        this.movementType = movementType;
        this.brand = brand;
        this.waterResistanceMeters = waterResistanceMeters;
        this.warrantyMonths = warrantyMonths;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaseMaterial() {
        return caseMaterial;
    }

    public void setCaseMaterial(String caseMaterial) {
        this.caseMaterial = caseMaterial;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getWaterResistanceMeters() {
        return waterResistanceMeters;
    }

    public void setWaterResistanceMeters(Integer waterResistanceMeters) {
        this.waterResistanceMeters = waterResistanceMeters;
    }

    public Integer getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(Integer warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * Determines if the product is in stock based on stock quantity.
     * @return true if stockQuantity > 0, false otherwise
     */
    public boolean getInStock() {
        return stockQuantity != null && stockQuantity > 0;
    }

    @Override
    public String toString() {
        return "Product{" +
               "productId=" + productId +
               ", name='" + name + '\'' +
               ", price=" + price +
               ", stockQuantity=" + stockQuantity +
               ", category='" + category + '\'' +
               ", brand='" + brand + '\'' +
               ", isDeleted=" + isDeleted +
               '}';
    }
}