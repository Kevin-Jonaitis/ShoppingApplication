package com.shopping.demo.ShoppingBackend.data.db;

import org.springframework.data.annotation.Id;

import java.util.Objects;

/**
 * Object model for the items we'll be offering on our website
 */
public class Item {

    public String id;
    public String name;
    public String description;
    public long price;
    public String filePath;

    public Item(String id, String name, String description, long price, String filePath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return price == item.price &&
                Objects.equals(id, item.id) &&
                Objects.equals(name, item.name) &&
                Objects.equals(description, item.description) &&
                Objects.equals(filePath, item.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, filePath);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
