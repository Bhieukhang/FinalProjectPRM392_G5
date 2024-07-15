package com.example.shirtsalesapp.activity.cart;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.shirtsalesapp.model.Cart;
import com.google.gson.Gson;

public class CartManager {
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public CartManager(Context context) {
        sharedPreferences = context.getSharedPreferences("CartPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveCart(Cart cart) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String cartJson = gson.toJson(cart);
        editor.putString("cart", cartJson);
        editor.apply();
    }

    public Cart loadCart() {
        String cartJson = sharedPreferences.getString("cart", "{}");
        if(cartJson.equals("{}")) return null;
        return  gson.fromJson(cartJson, Cart.class);
    }
}




