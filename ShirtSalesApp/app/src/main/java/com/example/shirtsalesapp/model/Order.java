package com.example.shirtsalesapp.model;

public class Order {
    private int id;
    private int userId;
    private int paymentId;
    private int totalPrice;
    private int finalPrice;
    private String address;
    private int voucherId;
    private int shippingId;
    private int status;
    private Object payment;
    private Object shipping;
    private Object user;
    private Object voucher;

    public Order() {
    }

    // Constructor
    public Order(int id, int userId, int paymentId, int totalPrice, int finalPrice, String address, int voucherId, int shippingId, int status, Object payment, Object shipping, Object user, Object voucher) {
        this.id = id;
        this.userId = userId;
        this.paymentId = paymentId;
        this.totalPrice = totalPrice;
        this.finalPrice = finalPrice;
        this.address = address;
        this.voucherId = voucherId;
        this.shippingId = shippingId;
        this.status = status;
        this.payment = payment;
        this.shipping = shipping;
        this.user = user;
        this.voucher = voucher;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getPayment() {
        return payment;
    }

    public void setPayment(Object payment) {
        this.payment = payment;
    }

    public Object getShipping() {
        return shipping;
    }

    public void setShipping(Object shipping) {
        this.shipping = shipping;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public Object getVoucher() {
        return voucher;
    }

    public void setVoucher(Object voucher) {
        this.voucher = voucher;
    }
}

