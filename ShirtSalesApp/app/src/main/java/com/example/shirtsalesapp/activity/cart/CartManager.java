package com.example.shirtsalesapp.activity.cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.shirtsalesapp.api.CartAPI;
import com.example.shirtsalesapp.model.Cart;
import com.example.shirtsalesapp.model.CartProduct;
import com.example.shirtsalesapp.model.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
        return cartJson == null ? new Cart() : gson.fromJson(cartJson, Cart.class);
    }
}




