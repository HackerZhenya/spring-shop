package com.hackerzhenya.springshop.services;

import com.hackerzhenya.springshop.models.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class ProductsService {
    private final static Collection<Product> repository = new ArrayList<>() {{
        add(new Product("Тапочки", 149.9, 20));
        add(new Product("Кружка", 199.9, 50));
        add(new Product("Стул", 849.9, 3));
        add(new Product("Стол", 2_499.9, 7));
        add(new Product("Ручка", 39.9, 74));
        add(new Product("Монитор", 5_999.9, 1));
        add(new Product("Телефон", 34_999.9, 2));
        add(new Product("Вешалка", 40, 10));
    }};

    public Collection<Product> getItems() {
        return repository.stream()
                         .map(Product::new)
                         .collect(Collectors.toList());
    }

    public Product getItem(long id) {
        return new Product(
                repository.stream()
                          .filter(x -> x.getId() == id)
                          .findFirst()
                          .orElseThrow(() -> new RuntimeException("Продукт не найден")));
    }

    public void decreaseQuantity(long productId, long quantity) {
        var product = repository
                .stream()
                .filter(p -> p.getId() == productId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException(
                    String.format("Не достаточно товара на складе для продукта \"%s\"", product.getTitle()));
        }

        if (product.getQuantity() == quantity) {
            repository.remove(product);
        } else {
            product.setQuantity(product.getQuantity() - quantity);
        }
    }
}
