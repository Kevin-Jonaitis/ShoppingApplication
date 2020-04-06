package com.shopping.demo.ShoppingBackend;

import com.shopping.demo.ShoppingBackend.data.db.Item;
import com.shopping.demo.ShoppingBackend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service that queries the backend and would handle any business logic.
 */
@Service
public class ItemSearchService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Item findItemByName(String name) {
        return itemRepository.findByName(name);
    }

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public List<Item> findProductsLessThanPrice(long priceMin, long priceMax) {
        Query query = new Query();
        query.addCriteria(Criteria.where("price").lt(priceMax).gt(priceMin));
        return mongoTemplate.find(query, Item.class);
    }
}
