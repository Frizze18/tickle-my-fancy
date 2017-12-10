package se.academy.domain;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {

    private Map<String,Integer> shoppingmap;

    public ShoppingCart(){
        this.shoppingmap = new HashMap<>();
    }

    public Map<String, Integer> getShoppingmap() {
        return shoppingmap;
    }

    public void setShoppingmap(Map<String, Integer> shoppingmap) {
        this.shoppingmap = shoppingmap;
    }

    public void addProduct(Product product){
        if(shoppingmap.get(product.getName()) == null){
            shoppingmap.put(product.getName(),1);
        }
        else{
            shoppingmap.put(product.getName(),shoppingmap.get(product.getName())+1);
        }
    }

}
