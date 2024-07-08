package com.example.shirtsalesapp.api;

import com.example.shirtsalesapp.model.CartProduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartProductAPI {
    @GET("/api/CartProduct/GetAllCartProducts")
    Call<List<CartProduct>> getAllCartProducts();

    @GET("/api/CartProduct/GetCartProductById/{id}")
    Call<CartProduct> getCartProductById(@Path("id") int productId);

    @POST("/api/CartProduct/CreateCartProduct")
    Call<CartProduct> createCartProduct(@Body CartProduct cartProduct);

    @PUT("/api/CartProduct/UpdateCartProduct/{id}")
    Call<CartProduct> updateCartProduct(@Path("id") int productId, @Body CartProduct cartProduct);

    @DELETE("/api/CartProduct/DeleteCartProduct/{id}")
    Call<Void> deleteCartProduct(@Path("id") int productId);

    @PUT("/api/CartProduct/UpdateCartProductStatus/{id}/{status}")
    Call<Void> updateCartProductStatus(@Path("id") int productId, @Path("status") int status);
}
