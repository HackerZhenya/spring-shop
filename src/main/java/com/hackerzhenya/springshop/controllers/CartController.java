package com.hackerzhenya.springshop.controllers;

import com.hackerzhenya.springshop.models.CartItem;
import com.hackerzhenya.springshop.services.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("cart")
public class CartController {
    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping("/")
    public Collection<CartItem> getCart() {
        return service.getCart();
    }

    @PostMapping("/")
    public void addToCart(@RequestBody CartItem item) {
        service.addToCart(item.getProductId(), item.getQuantity());
    }

    @DeleteMapping("/")
    public void removeFromCart(@RequestBody CartItem item) {
        service.removeFromCart(item.getProductId(), item.getQuantity());
    }

    @PostMapping("/checkout")
    public void checkout() {
        service.checkout();
    }
}
