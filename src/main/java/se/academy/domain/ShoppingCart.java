package se.academy.domain;

import org.springframework.beans.factory.annotation.Autowired;
import se.academy.repository.DbRepository;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    @Autowired
    private DbRepository repository;

    private List<Product> shoppinglist;

    public ShoppingCart(){
        this.shoppinglist = new ArrayList<>();

    }

    public List<Product> getShoppinglist() {
        return shoppinglist;
    }

    public void setShoppinglist(List<Product> shoppinglist) {
        this.shoppinglist = shoppinglist;
    }

    public void addProduct(Product product){
        shoppinglist.add(product);
    }
}
