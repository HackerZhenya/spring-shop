package com.hackerzhenya.springshop.models;

public class Product {
    private static long counter = 1;

    private final long id;
    private String title;
    private double price;
    private long quantity;

    public Product(String title, double price, long quantity) {
        this.id = counter++;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(long id, String title, double price, long quantity) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(Product product) {
        this(product.getId(), product.getTitle(), product.getPrice(), product.getQuantity());
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
