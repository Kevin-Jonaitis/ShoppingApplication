package com.shopping.demo.ShoppingBackend.data.rest;

import org.springframework.data.annotation.Id;

/**
 * This object is only used for passing between the frontend and the backend, specifically
 * when a new user signs up.
 */
public class RESTUser {
    public String userName;
    // Cleartext password. Should *not* be stored, only used for new signups
    public String password;

    public RESTUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

}
