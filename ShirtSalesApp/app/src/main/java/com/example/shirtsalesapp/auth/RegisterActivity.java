package com.example.shirtsalesapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.model.ApiClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText userNameEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton, loginButton;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userNameEditText = findViewById(R.id.userNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        userService = ApiClient.getRetrofitInstance().create(UserService.class);

        registerButton.setOnClickListener(v -> register());

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void register() {
        String userName = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest registerRequest = new RegisterRequest(userName, password);
        Call<ResponseBody> call = userService.register(registerRequest);

        long startTime = System.currentTimeMillis();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                Log.d("RegisterActivity", "API response time: " + duration + "ms");

                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("RegisterActivity", "Response body: " + responseBody);
                        Toast.makeText(RegisterActivity.this, "Đã tạo account", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (IOException e) {
                        Log.e("RegisterActivity", "Malformed JSON: " + e.getMessage());
                        Toast.makeText(RegisterActivity.this, "Malformed JSON response from server", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Log.e("RegisterActivity", "HTTP Code: " + response.code());
                        String errorBody = response.errorBody().string();
                        Log.e("RegisterActivity", "Error: " + errorBody);
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                Log.d("RegisterActivity", "API response time: " + duration + "ms");
                Log.e("RegisterActivity", "Failure: " + t.getMessage());
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
