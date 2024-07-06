package com.example.shirtsalesapp.api;

import com.example.shirtsalesapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AccountAPI {

    @GET(" api/User/GetAllUsers")
    Call<List<User>> getAllUsers();
    @GET("api/User/CountUsers")
    Call<Integer> getCountAccount();
    @GET("api/User/CountUsers")
    Call<Integer> getCountAcc(@Query("status") int status);
}
