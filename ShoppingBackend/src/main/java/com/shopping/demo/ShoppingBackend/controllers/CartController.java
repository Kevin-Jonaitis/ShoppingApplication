package com.shopping.demo.ShoppingBackend.controllers;

import com.shopping.demo.ShoppingBackend.AuthenticationService;
import com.shopping.demo.ShoppingBackend.data.db.Cart;
import com.shopping.demo.ShoppingBackend.data.db.Item;
import com.shopping.demo.ShoppingBackend.data.db.User;
import com.shopping.demo.ShoppingBackend.repository.CartRepository;
import com.shopping.demo.ShoppingBackend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController()
public class CartController {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    AuthenticationService authenticationService;


    /**
     * For simplicity's sake, we'll always have the user send over their password.
     * The password(salted and hashed) will be stored in the user's browser.
     *
     * Normally we'd send over an api key and store that and it would auto expire,
     * but that's a lot of work for this simple coding program
     */
    @GetMapping("/cart")
    public Cart getCart(HttpServletRequest request) {
        User user = authenticationService.authenticateAndGetUserFromAuthToken(request);
        if (user != null) {
            Cart cart =  cartRepository.findByUserName(user.userName);

            // We should create a new cart
            if (cart == null || cart.itemList == null) {
                cart = new Cart(user.userName, new ArrayList<>(), new ArrayList<>());
            }

            return cart;

        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Username or password incorrect"
            );
        }
    }

    /**
     * Verifies a user is who they say they are, and then updates their cart.
     */
    @PutMapping("/cart")
    public Cart updateCart(HttpServletRequest request,
                          @RequestBody Map<String, Integer> productIdToCountMap) {
        User user = authenticationService.authenticateAndGetUserFromAuthToken(request);
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Username or password incorrect"
            );
        } else {
            Cart dbCart = cartRepository.findByUserName(user.userName);

            // We should create a new cart
            if (dbCart == null || dbCart.itemList == null) {
                dbCart = new Cart(user.userName, new ArrayList<>(), new ArrayList<>());
            }

            Map<Item, Integer> dbMapItemToCount = getItemToCountMapFromCartObject(dbCart);

            //Get items based off of IDs
            List<String> productList = new ArrayList<>(productIdToCountMap.keySet());
            Iterable<Item> allItemsById = itemRepository.findAllById(productList);
            List<Item> newItemCountsToAdd = new ArrayList<>();
            allItemsById.forEach(newItemCountsToAdd::add);

            for (Item item : newItemCountsToAdd) {
                if (productIdToCountMap.get(item.id) == 0) { //If it's 0, clear the item from our cart
                    dbMapItemToCount.remove(item);
                } else {
                    dbMapItemToCount.put(item, productIdToCountMap.get(item.id));
                }
            }

            dbCart.itemList = new ArrayList<>(dbMapItemToCount.keySet());
            dbCart.itemCount = new ArrayList<>(dbMapItemToCount.values());

            //Update the user's cart list
            cartRepository.save(dbCart);

            return dbCart;
        }
    }

    public Map<Item, Integer> getItemToCountMapFromCartObject(Cart dbCart) {

        //Construct a map out of the item list stored in the DB
        Map<Item, Integer> dbMapItemToCount = new HashMap<>();
        for (int i = 0; i < dbCart.itemList.size(); i++) {
            Item item = dbCart.itemList.get(i);
            Integer integer = dbCart.itemCount.get(i);
            dbMapItemToCount.put(item, integer);
        }

        return dbMapItemToCount;
    }
}
