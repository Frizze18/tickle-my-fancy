package se.academy.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private Map<Integer,ProductWrapper> shoppingmap; //key = productID is Integer
    private double totalPrice;

    public ShoppingCart(){
        this.shoppingmap = new HashMap<>();
        this.totalPrice = 0;
    }

    public Map<Integer, ProductWrapper> getShoppingmap() {
        return shoppingmap;
    }

    public void setShoppingmap(Map<Integer,ProductWrapper> shoppingmap) {
        this.shoppingmap = shoppingmap;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void addProduct(Product product){
        ProductWrapper productWrapper;
        if(shoppingmap.get(product.getProductID()) == null){
            productWrapper = new ProductWrapper(product,1);
            shoppingmap.put(product.getProductID(),productWrapper);
        }
        else{
            productWrapper = new ProductWrapper(product,shoppingmap.get(product.getProductID()).getQuantity()+1);
            shoppingmap.put(product.getProductID(),productWrapper);
        }
        totalPrice = totalPrice + product.getPrice();
    }

}
