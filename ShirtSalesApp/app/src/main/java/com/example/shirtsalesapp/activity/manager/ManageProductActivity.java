package com.example.shirtsalesapp.activity.manager;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.activity.auth.LoginActivity;
import com.example.shirtsalesapp.activity.product.ProductAdapter;
import com.example.shirtsalesapp.api.ProductAPI;
import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.model.RetrofitClient;
import com.example.shirtsalesapp.service.ProductService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManageProductActivity extends AppCompatActivity {
    private EditText editTextAmountProduct;
    private RecyclerView rvListProduct;
    private ProductAdapter productAdapter;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        editTextAmountProduct = findViewById(R.id.editTextAmountProduct);
        rvListProduct = findViewById(R.id.rvListProduct);
        btnLogout = findViewById(R.id.btn_logout);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupBottomNavigation(bottomNavigationView);

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ProductAPI apiService = retrofit.create(ProductAPI.class);
        Call<Integer> call = apiService.getCountProduct(1);

        // Call count product
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Integer count = response.body();
                    Log.d(TAG, "Product Count: " + count);
                    editTextAmountProduct.setText(String.valueOf(count));
                } else {
                    Log.e(TAG, "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "Network request failed", t);
            }
        });

        // Load list product item
        rvListProduct.setLayoutManager(new GridLayoutManager(this, 1));

        ProductService.fetchAllProducts(new ProductService.ProductApiCallback() {
            @Override
            public void onSuccess(List<Product> productList) {
                productAdapter = new ProductAdapter(ManageProductActivity.this, productList, 2);
                rvListProduct.setAdapter(productAdapter);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("API_ERROR", errorMessage);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Log.e("MenuManage", "Activity");

                if (id == R.id.product) {
                    startActivity(new Intent(ManageProductActivity.this, ManageProductActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.account) {
                    startActivity(new Intent(ManageProductActivity.this, ManageAccountActivity.class));
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("userId");
        editor.apply();

        Toast.makeText(ManageProductActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent loginIntent = new Intent(ManageProductActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
