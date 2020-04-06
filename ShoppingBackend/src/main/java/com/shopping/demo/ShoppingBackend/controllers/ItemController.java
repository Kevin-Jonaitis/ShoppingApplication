package com.shopping.demo.ShoppingBackend.controllers;

import com.shopping.demo.ShoppingBackend.ItemSearchService;
import com.shopping.demo.ShoppingBackend.data.db.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class ItemController {

    @Autowired
    ItemSearchService itemSearchService;

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return itemSearchService.findAllItems();
    }

    @GetMapping("/item/{name}")
    public Item getItemByName(@PathVariable String name) {
        return itemSearchService.findItemByName(name);
    }

    @GetMapping("/search")
    public List<Item> findItems(@RequestParam(value = "minPrice", defaultValue = "0") long minPrice,
                          @RequestParam(value = "maxPrice") long maxPrice) {
        return itemSearchService.findProductsLessThanPrice(minPrice, maxPrice);
    }
}
