package se.academy.domain;

public class ProductWrapper {

    private Product product;
    private int quantity;
    private double priceOfProducts;

    public ProductWrapper(){
    }

    public ProductWrapper(Product product, int quantity){
        this.product = product;
        this.quantity = quantity;
        this.priceOfProducts = (product.getPrice()*quantity);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceOfProducts() {
        return priceOfProducts;
    }

    public void setPriceOfProducts(double priceOfProducts) {
        this.priceOfProducts = priceOfProducts;
    }

}
