package com.example.shirtsalesapp.model;

public class CartProduct {
    private int ID;
    private int CartID;
    private int ProductID;
    private int Quantity;
    private String Status;

    public CartProduct() {
    }

    public CartProduct(int ID, int cartID, int productID, int quantity, String status) {
        this.ID = ID;
        CartID = cartID;
        ProductID = productID;
        Quantity = quantity;
        Status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCartID() {
        return CartID;
    }

    public void setCartID(int cartID) {
        CartID = cartID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
