package com.example.shirtsalesapp.auth;

import com.example.shirtsalesapp.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @POST("api/User/Login")
    Call<User> login(@Query("userName") String userName, @Query("password") String password);

    @POST("api/User/CreateUser")
    Call<ResponseBody> register(@Body RegisterRequest registerRequest);
}
