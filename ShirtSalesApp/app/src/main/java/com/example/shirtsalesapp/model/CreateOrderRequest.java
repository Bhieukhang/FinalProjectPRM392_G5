package com.example.shirtsalesapp.model;

import com.example.shirtsalesapp.model.order.CreateOrderDTO;
import com.example.shirtsalesapp.model.order.CreateOrderDetailDTO;

import java.util.List;

public class CreateOrderRequest {
    public CreateOrderDTO OrderDTO;
    public List<CreateOrderDetailDTO> OrderDetailDTO ;
}
