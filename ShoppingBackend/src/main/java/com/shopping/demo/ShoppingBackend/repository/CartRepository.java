package com.shopping.demo.ShoppingBackend.repository;

import com.shopping.demo.ShoppingBackend.data.db.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
    Cart save(Cart entity);
    Cart findByUserName(String userName);
}
