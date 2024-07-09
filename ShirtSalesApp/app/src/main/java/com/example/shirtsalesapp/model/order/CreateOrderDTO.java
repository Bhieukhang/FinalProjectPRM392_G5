package com.example.shirtsalesapp.model.order;

public class CreateOrderDTO {
    public int UserId ;
    public int PaymentId ;
    public double TotalPrice ;
    public double FinalPrice ;
    public String Address ;
    public int VoucherId ;
    public int ShippingId ;

    public CreateOrderDTO() {
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getPaymentId() {
        return PaymentId;
    }

    public void setPaymentId(int paymentId) {
        PaymentId = paymentId;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public double getFinalPrice() {
        return FinalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        FinalPrice = finalPrice;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getVoucherId() {
        return VoucherId;
    }

    public void setVoucherId(int voucherId) {
        VoucherId = voucherId;
    }

    public int getShippingId() {
        return ShippingId;
    }

    public void setShippingId(int shippingId) {
        ShippingId = shippingId;
    }
}
