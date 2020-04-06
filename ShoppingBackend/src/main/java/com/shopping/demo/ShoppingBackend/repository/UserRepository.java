package com.shopping.demo.ShoppingBackend.repository;

import com.shopping.demo.ShoppingBackend.data.db.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    public User save(User entity);
    public User findByUserName(String userName);
}
