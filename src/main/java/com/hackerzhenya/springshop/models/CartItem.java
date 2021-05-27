package com.hackerzhenya.springshop.models;

public class CartItem {
    private long productId;
    private long quantity;

    public CartItem(long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
