package com.shopping.demo.ShoppingBackend.repository;

import com.shopping.demo.ShoppingBackend.data.db.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {
    /**
     * Finds all items
     */
    public List<Item> findAll();

    /**
     * Finds items by product name
     */
    public Item findByName(String name);
}
