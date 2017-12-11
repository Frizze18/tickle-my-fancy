package se.academy.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private Map<Integer,ProductWrapper> shoppingmap; //key = productID is Integer

    public ShoppingCart(){
        this.shoppingmap = new HashMap<>();
    }

    public Map<Integer, ProductWrapper> getShoppingmap() {
        return shoppingmap;
    }

    public void setShoppingmap(Map<Integer,ProductWrapper> shoppingmap) {
        this.shoppingmap = shoppingmap;
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
    }

}
