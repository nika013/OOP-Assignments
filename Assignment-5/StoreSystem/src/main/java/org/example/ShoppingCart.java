package org.example;

import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.bind.SchemaOutputResolver;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ShoppingCart {
    private Map<String, Product> products;
    public ShoppingCart() {
        products = new HashMap<>();
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public void updateCart(String id, int quantity) {
        if (quantity == 0) {
            products.remove(id);
        } else {
            Product product = products.get(id);
            product.setQuantity(quantity);
            products.put(id, product);
        }
    }

    public void addToCart(String name, String id, int price) {
        if (!products.containsKey(id)) {
            Product product = new Product(id, name, price);
            product.setQuantity(1);
            products.put(id, product);
        } else {
            Product product = products.get(id);
            int quantity = product.getQuantity();
            product.setQuantity(quantity + 1);
            products.put(id, product);
        }
    }

    public int getTotal() {
        int total = 0;
        for (String id : products.keySet()) {
            Product product = products.get(id);
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }
}
