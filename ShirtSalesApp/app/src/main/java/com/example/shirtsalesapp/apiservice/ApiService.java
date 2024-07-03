package com.example.shirtsalesapp.apiservice;
import com.example.shirtsalesapp.model.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/Product/GetAllProducts")
    Call<List<Product>> getAllProducts();
}
