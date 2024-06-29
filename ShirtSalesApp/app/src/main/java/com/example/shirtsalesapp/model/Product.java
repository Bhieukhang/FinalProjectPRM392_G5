package com.example.shirtsalesapp.model;

public class Product {

    public int Id;
    public String Title;
    public String ProductName;
    public String Description;
    public double Price;
    public int Quantity;
    public int Status;
    public int CategoryId;
    public String ImageUrl;

    public Product(String name, double price, String imageUrl) {
        ProductName = name;
        Price = price;
        ImageUrl = imageUrl;
    }

    public Product(int id, String title, String productName, String description, double price, int quantity, int status, int categoryId, String imageUrl) {
        Id = id;
        Title = title;
        ProductName = productName;
        Description = description;
        Price = price;
        Quantity = quantity;
        Status = status;
        CategoryId = categoryId;
        ImageUrl = imageUrl;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
