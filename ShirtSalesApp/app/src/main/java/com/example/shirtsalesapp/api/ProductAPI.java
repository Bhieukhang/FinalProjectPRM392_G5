package com.example.shirtsalesapp.api;

import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.model.response.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductAPI {
    @GET("api/Product/GetAllProducts")
    Call<List<Product>> getAllProducts();
}

