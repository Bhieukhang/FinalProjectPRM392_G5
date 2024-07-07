package com.example.shirtsalesapp.api;

import com.example.shirtsalesapp.model.Cart;
import com.example.shirtsalesapp.model.CartProduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartAPI {
    @GET("api/cart")
    Call<List<Cart>> getCartItems();

    @POST("api/Cart/CreateCart")
    Call<Cart> createCart(@Body Cart cart);

    @PUT("api/Cart/UpdateCart/{id}")
    Call<Cart> updateCart(@Path("id") int cartId, @Body Cart cart);
}
