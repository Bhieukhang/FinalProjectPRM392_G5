package com.example.shirtsalesapp.api;

import com.example.shirtsalesapp.model.CartProduct;
import com.example.shirtsalesapp.model.Order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderAPI {
    @POST("/api/Order/GetAllOrders")
    Call<Order> createOrder(@Body Order order);
}
