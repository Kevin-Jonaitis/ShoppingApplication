package com.shopping.demo.ShoppingBackend.data.db;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

/**
 * This represents a simple key-value store of a user's id to the items in the cart.
 */
public class Cart {
    @Id
    public String userName;

    /**
     * Because mongoDB cant handle storing a map in the DB, we will store our map as two lists
     */

    public List<Item> itemList;
    public List<Integer> itemCount;

    public Cart(String userName, List<Item> itemList, List<Integer> itemCount) {
        this.userName = userName;
        this.itemList = itemList;
        this.itemCount = itemCount;
    }
}
