package com.example.shirtsalesapp.model.response;

import com.example.shirtsalesapp.model.Product;

import java.util.List;

public class ProductResponse {
    public List<Product> productList;
    public List<Product> getProducts() {
        return productList;
    }

    public void setProducts(List<Product> products) {
        this.productList = products;
    }
}
