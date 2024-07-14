package com.example.shirtsalesapp.api;

import com.example.shirtsalesapp.model.Cart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartAPI {
    @GET("/api/Cart/GetAllCarts")
    Call<List<Cart>> getAllCarts();

    @GET("/api/Cart/GetCartById/{id}")
    Call<Cart> getCartById(@Path("id") int cartId);

    @POST("/api/Cart/CreateCart")
    Call<Cart> createCart(@Body Cart cart);

    @PUT("/api/Cart/UpdateCart/{id}")
    Call<Cart> updateCart(@Path("id") int cartId, @Body Cart cart);

    @DELETE("/api/Cart/DeleteCart/{id}")
    Call<Void> deleteCart(@Path("id") int cartId);

    @PUT("/api/Cart/UpdateCartStatus/{id}/{status}")
    Call<Void> updateCartStatus(@Path("id") int cartId, @Path("status") int status);
}
