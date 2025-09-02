package com.example.final_assignment_app1;

import java.util.ArrayList;
import java.util.List;

public class ShoppingBagList {
//singleton object class for app1's shopping bag
    public static ShoppingBagList instance;

    private final List<App1Products> shoppingBagList;

    private ShoppingBagList(){
        shoppingBagList = new ArrayList<>();

    }

    //if instance is not null get the current instance
    public static ShoppingBagList getInstance(){
        if (instance == null){
            instance = new ShoppingBagList();
        }
        return instance;
    }

    public void addProductToList(App1Products product){
        shoppingBagList.add(product);
    }

    public void removeProductToList(App1Products product){
        shoppingBagList.remove(product);
    }

    public void clearList(){
        shoppingBagList.clear();
    }

    public List<App1Products> getBag(){
        return shoppingBagList;
    }


}
