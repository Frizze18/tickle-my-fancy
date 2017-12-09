package se.academy.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private List<Product> shoppinglist;
    private Map<String,Integer> shoppingmap;

    public ShoppingCart(){
        this.shoppinglist = new ArrayList<>();
        this.shoppingmap = new HashMap<>();
    }

    public List<Product> getShoppinglist() {
        return shoppinglist;
    }

    public void setShoppinglist(List<Product> shoppinglist) {
        this.shoppinglist = shoppinglist;
    }

    public Map<String, Integer> getShoppingmap() {
        return shoppingmap;
    }

    public void setShoppingmap(Map<String, Integer> shoppingmap) {
        this.shoppingmap = shoppingmap;
    }

    public void addProduct(Product product){
        shoppinglist.add(product);
        if(shoppingmap.get(product.getName()) == null){
            shoppingmap.put(product.getName(),1);
        }
        else{
            shoppingmap.put(product.getName(),shoppingmap.get(product.getName())+1);
        }
    }

}
