package com.hackerzhenya.springshop.controllers;

import com.hackerzhenya.springshop.models.Product;
import com.hackerzhenya.springshop.services.ProductsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("products")
public class ProductsController {
    private final ProductsService service;

    public ProductsController(ProductsService service) {
        this.service = service;
    }

    @GetMapping("/")
    public Collection<Product> getProducts() {
        return service.getItems();
    }
}
