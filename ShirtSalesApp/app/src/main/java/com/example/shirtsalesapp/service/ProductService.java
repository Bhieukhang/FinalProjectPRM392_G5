package com.example.shirtsalesapp.service;

import com.example.shirtsalesapp.api.HostAPI;
import com.example.shirtsalesapp.api.ProductAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductService {
    private static ProductAPI api;

    public static synchronized ProductAPI getApi() {
        if (api == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();

            api = new Retrofit.Builder()
                    .baseUrl(HostAPI.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(ProductAPI.class);
        }
        return api;
    }
}
