package com.example.shirtsalesapp.activity.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.activity.cart.CartManager;
import com.example.shirtsalesapp.activity.product.ProductListActivity;
import com.example.shirtsalesapp.model.ApiClient;
import com.example.shirtsalesapp.model.User;
import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.model.Cart;
import com.example.shirtsalesapp.model.CartProduct;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userNameEditText, passwordEditText;
    private Button loginButton, registerRedirectButton;
    private UserService userService;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEditText = findViewById(R.id.userNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerRedirectButton = findViewById(R.id.registerRedirectButton);

        userService = ApiClient.getRetrofitInstance().create(UserService.class);

        // Nhận sản phẩm từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("productItem")) {
            product = (Product) intent.getSerializableExtra("productItem");
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerRedirectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        String userName = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Call<User> call = userService.login(userName, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                        // Lưu trạng thái đăng nhập vào SharedPreferences
                        saveLoginState(true);

                        // Handle successful login
                        if (product != null) {
                            addToCartAfterLogin(user.getId(), product);
                        }
                        Intent intent = new Intent(LoginActivity.this, ProductListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Hãy thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCartAfterLogin(int userId, Product product) {
        CartManager cartManager = new CartManager(this);
        Cart cart = cartManager.loadCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
        }

        boolean check = true;
        for (CartProduct c : cart.getProducts()) {
            if (c.getProductId() == product.getId()) {
                c.setQuantity(c.getQuantity() + 1);
                Toast.makeText(this, "Duplicated item +1 Quantity", Toast.LENGTH_SHORT).show();
                check = false;
            }
        }
        if (check) {
            CartProduct cartProduct = new CartProduct();
            cartProduct.setProductId(product.getId());
            cartProduct.setQuantity(1); // Default quantity
            cartProduct.setStatus(1);
            cart.addProduct(cartProduct);
        }
        cartManager.saveCart(cart);
        Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
    }

    private void saveLoginState(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }
}
