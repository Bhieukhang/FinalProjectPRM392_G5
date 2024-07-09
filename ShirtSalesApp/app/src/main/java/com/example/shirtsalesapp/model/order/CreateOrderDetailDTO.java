package com.example.shirtsalesapp.model.order;

public class CreateOrderDetailDTO {
    public int ProductId ;
    public int Quantity ;
    public double TotalPrice ;

    public CreateOrderDetailDTO() {
    }

    public CreateOrderDetailDTO(int productId, int quantity, double totalPrice) {
        ProductId = productId;
        Quantity = quantity;
        TotalPrice = totalPrice;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }
}
