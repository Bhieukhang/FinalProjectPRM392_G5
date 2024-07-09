package com.example.shirtsalesapp.model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    private int id;
    private String title;
    private String productName;
    private String description;
    private double price;
    private int quantity;
    private int status;
    private int categoryId;
    private String imageUrl;
    private List<CartProduct> cartProduct;
    public Product() {
        // Default constructor
    }

    public Product(int id, String title, String productName, String description, double price, int quantity, int status, int categoryId, String imageUrl, List<CartProduct> cartProduct) {
        this.id = id;
        this.title = title;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.cartProduct = cartProduct;
    }

    // Getters and setters for all fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<CartProduct> getCartProduct() {
        return cartProduct;
    }

    public void setCartProduct(List<CartProduct> cartProduct) {
        this.cartProduct = cartProduct;
    }
}
