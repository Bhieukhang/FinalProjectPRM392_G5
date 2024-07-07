package com.example.shirtsalesapp.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int id;
    private int userId;
    private int status;
    private List<CartProduct> products;  // List to store cart products

    public Cart() {
        products = new ArrayList<>();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CartProduct> getProducts() {
        return products;
    }

    public void setProducts(List<CartProduct> products) {
        this.products = products;
    }

    public void addProduct(CartProduct product) {
        this.products.add(product);
    }
}
