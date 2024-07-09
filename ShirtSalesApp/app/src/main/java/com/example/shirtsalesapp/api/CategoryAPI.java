package com.example.shirtsalesapp.api;

import com.example.shirtsalesapp.model.Category;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryAPI {
    @GET("api/Category/GetCategoryById/{id}")
    Call<Category> getCategoryById(@Path("id") int categoryId);
}

