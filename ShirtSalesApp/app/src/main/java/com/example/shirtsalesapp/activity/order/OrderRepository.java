package com.example.shirtsalesapp.activity.order;

import com.example.shirtsalesapp.api.OrderService;
import com.example.shirtsalesapp.model.ApiClient;

public class OrderRepository {
    public static OrderService getOrderService(){
        return ApiClient.getClient().create(OrderService.class);
    }
}
