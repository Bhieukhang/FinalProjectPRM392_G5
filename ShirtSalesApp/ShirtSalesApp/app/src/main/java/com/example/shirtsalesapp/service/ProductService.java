package com.example.shirtsalesapp.service;

import com.example.shirtsalesapp.api.ProductAPI;
import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.model.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductService {
    private static ProductAPI api;

    private static final String TAG = "ProductApiHelper";

    public interface ProductApiCallback {
        void onSuccess(List<Product> productList);
        void onError(String errorMessage);
    }

    public static void fetchAllProducts(ProductApiCallback callback) {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ProductAPI apiService = retrofit.create(ProductAPI.class);

        Call<List<Product>> call = apiService.getAllProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
