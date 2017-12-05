package se.academy.domain;

public class Product {
    private int productID;
    private String name;
    private double price;
    private String description;
    private String image;
    private String category;
    private int quantity;

    public Product(int productID, String name, double price, String desciption, String image, String category, int quantity) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.description = desciption;
        this.image = image;
        this.category = category;
        this.quantity = quantity;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}