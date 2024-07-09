package com.example.shirtsalesapp.api;

import com.example.shirtsalesapp.model.CreateOrderRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderService {
    @POST("/api/Order/CreateOrder")
    Call<CreateOrderRequest> createOrder(@Body CreateOrderRequest request);

}
