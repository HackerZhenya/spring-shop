package com.hackerzhenya.springshop.services;

import com.hackerzhenya.springshop.models.CartItem;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CartService {
    private final static Map<Long, Long> repository = new HashMap<>();
    private final ProductsService service;

    public CartService(ProductsService service) {
        this.service = service;
    }

    public Collection<CartItem> getCart() {
        return repository.entrySet()
                         .stream()
                         .map(e -> new CartItem(e.getKey(), e.getValue()))
                         .collect(Collectors.toList());
    }

    public void addToCart(CartItem item) {
        var newQuantity = repository.getOrDefault(item.getProductId(), 0L) + item.getQuantity();
        var product = service.getItem(item.getProductId());

        if (product.getQuantity() < newQuantity) {
            throw new RuntimeException(
                    String.format("Не достаточно товара на складе для продукта \"%s\"", product.getTitle()));
        }

        repository.put(item.getProductId(), newQuantity);
    }

    public void addToCart(long productId, long quantity) {
        addToCart(new CartItem(productId, quantity));
    }

    public void removeFromCart(CartItem item) {
        var quantity = repository.get(item.getProductId());
        if (quantity == null) return;

        if (quantity <= item.getQuantity()) {
            repository.remove(item.getProductId());
        } else {
            repository.put(item.getProductId(), quantity - item.getQuantity());
        }
    }

    public void removeFromCart(long productId, long quantity) {
        removeFromCart(new CartItem(productId, quantity));
    }

    public void checkout() {
        if (service.getItems()
                   .stream()
                   .filter(x -> repository.containsKey(x.getId()))
                   .anyMatch(x -> x.getQuantity() < repository.getOrDefault(x.getId(), 0L))) {
            throw new RuntimeException("Не достаточно товара на складе для некоторых продуктов");
        }

        for (var entry : repository.entrySet()) {
            service.decreaseQuantity(entry.getKey(), entry.getValue());
        }

        repository.clear();
    }
}
