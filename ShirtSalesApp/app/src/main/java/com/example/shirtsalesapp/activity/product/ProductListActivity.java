package com.example.shirtsalesapp.activity.product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.activity.auth.LoginActivity;
import com.example.shirtsalesapp.activity.cart.CartActivity;
import com.example.shirtsalesapp.activity.cart.CartManager;
import com.example.shirtsalesapp.activity.manager.ManageAccountActivity;
import com.example.shirtsalesapp.activity.manager.ManagePaymentActivity;
import com.example.shirtsalesapp.activity.manager.ManageProductActivity;
import com.example.shirtsalesapp.activity.store.StoreActivity;
import com.example.shirtsalesapp.api.ProductAPI;
import com.example.shirtsalesapp.model.Cart;
import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.model.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private EditText etSearch;
    private ImageButton btnSearch, btnSearchInside, btnLogout;
    private LinearLayout searchContainer;
    private ImageView iconCart, icHome;
    private CartManager cartManager;
    private Cart cart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cloth);
        cartManager = new CartManager(ProductListActivity.this);
        if(cartManager.loadCart() == null){
            cart = new Cart();
            // Set Default
            cart.setUserId(1);
            cartManager.saveCart(cart);
        }
        icHome = findViewById(R.id.ic_home_store);
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        btnSearchInside = findViewById(R.id.btn_search_inside);
        btnLogout = findViewById(R.id.btn_logout);
        searchContainer = findViewById(R.id.search_container);
        iconCart = findViewById(R.id.icon_cart);

        icHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, StoreActivity.class);
                startActivity(intent);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContainer.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.GONE);
            }
        });

        etSearch.setImeOptions(EditorInfo.IME_ACTION_DONE); // Hiển thị nút "Hoàn thành"
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Xử lý tại đây khi người dùng ấn nút "Hoàn thành"
                    performSearch(etSearch.getText().toString());
                    // Ẩn bàn phím sau khi hoàn thành
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        btnSearchInside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch(etSearch.getText().toString());
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewCustomer);
        setupBottomNavigation(bottomNavigationView);

        iconCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                if (isLoggedIn(context)) {
                    Intent intent = new Intent(ProductListActivity.this, CartActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("redirectTo", "cart");
                    context.startActivity(intent);
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Kiểm tra trạng thái đăng nhập và hiển thị nút Logout nếu đã đăng nhập
        if (isLoggedIn(this)) {
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            btnLogout.setVisibility(View.GONE);
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Cấu hình Retrofit
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ProductAPI apiService = retrofit.create(ProductAPI.class);

        Call<List<Product>> call = apiService.getAllProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body();
                    productAdapter = new ProductAdapter(productList);
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Toast.makeText(ProductListActivity.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Failed to fetch products: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSearch(String query) {
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ProductAPI apiService = retrofit.create(ProductAPI.class);

        Call<List<Product>> call = apiService.getAllProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body();
                    productAdapter.updateProductList(productList);
                } else {
                    Toast.makeText(ProductListActivity.this, "No products found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Search failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.product) {
                    Intent homeIntent = new Intent(ProductListActivity.this, ManageProductActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.account) {
                    Intent accountIntent = new Intent(ProductListActivity.this, ManageAccountActivity.class);
                    startActivity(accountIntent);
                    return true;
                } else if (itemId == R.id.payment) {
                    if (isLoggedIn(ProductListActivity.this)) {
                        Intent cartIntent = new Intent(ProductListActivity.this, ManagePaymentActivity.class);
                        startActivity(cartIntent);
                    } else {
                        Intent loginIntent = new Intent(ProductListActivity.this, LoginActivity.class);
                        loginIntent.putExtra("redirectTo", "cart");
                        startActivity(loginIntent);
                    }
                    return true;
                }
//                else if (itemId == R.id.action_orders) {
//                    // Handle orders action
//                    return true;
//                }
                return false;
            }
        });
    }

    private boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("userId");
        editor.apply();

        Toast.makeText(ProductListActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent loginIntent = new Intent(ProductListActivity.this, ProductListActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
