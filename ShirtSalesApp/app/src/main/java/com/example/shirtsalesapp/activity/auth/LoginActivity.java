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
import com.example.shirtsalesapp.activity.cart.CartActivity;
import com.example.shirtsalesapp.activity.cart.CartManager;
import com.example.shirtsalesapp.activity.manager.ManageProductActivity;
import com.example.shirtsalesapp.activity.product.ProducDetailActivity;
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

        // Kiểm tra tài khoản manager cứng
        if ("manager".equals(userName) && "123456".equals(password)) {
            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            Intent manageProductIntent = new Intent(LoginActivity.this, ManageProductActivity.class);
            startActivity(manageProductIntent);
            finish();
            return;
        }

        Call<User> call = userService.login(userName, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        saveLoginState(true, user.getId());
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                        // Kiểm tra xem người dùng được chuyển hướng từ đâu
                        Intent intent = getIntent();
                        if (intent != null && intent.hasExtra("redirectTo")) {
                            String redirectTo = intent.getStringExtra("redirectTo");
                            if ("cart".equals(redirectTo)) {
                                Intent cartIntent = new Intent(LoginActivity.this, CartActivity.class);
                                startActivity(cartIntent);
                            } else if (product != null) {
                                addToCartAfterLogin(user.getId(), product);
                            } else {
                                Intent productListIntent = new Intent(LoginActivity.this, ProductListActivity.class);
                                startActivity(productListIntent);
                            }
                        } else {
                            Intent productListIntent = new Intent(LoginActivity.this, ProductListActivity.class);
                            startActivity(productListIntent);
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginState(boolean isLoggedIn, int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.putInt("userId", userId);
        editor.apply();
    }

    private void addToCartAfterLogin(int userId, Product product) {
        CartManager cartManager = new CartManager(this);
        Cart cart = cartManager.loadCart();
        if (cart == null || cart.getStatus() == 0) {
            cart = new Cart();
            cart.setUserId(userId);
        }
        boolean check = true;
        for (CartProduct c : cart.getProducts()) {
            if (c.getProductId() == product.getId()) {
                c.setQuantity(c.getQuantity() + 1);
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

        Intent productDetailIntent = new Intent(LoginActivity.this, ProducDetailActivity.class);
        productDetailIntent.putExtra("productItem", product);
        startActivity(productDetailIntent);
        finish();
    }
}
