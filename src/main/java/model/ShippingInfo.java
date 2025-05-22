package model;

public class ShippingInfo {
    private Integer shippingId;
    private String address;
    private String contact;
    private String shippingMethod;
    private Boolean isDeleted;

    // Constructors
    public ShippingInfo() {
    }

    public ShippingInfo(Integer shippingId, String address, String contact, String shippingMethod, Boolean isDeleted) {
        this.shippingId = shippingId;
        this.address = address;
        this.contact = contact;
        this.shippingMethod = shippingMethod;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public Integer getShippingId() {
        return shippingId;
    }

    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "ShippingInfo{" +
                "shippingId=" + shippingId +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", shippingMethod='" + shippingMethod + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}