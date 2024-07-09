package com.example.shirtsalesapp.activity.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.activity.cart.CartActivity;
import com.example.shirtsalesapp.activity.cart.CartManager;
import com.example.shirtsalesapp.activity.manager.ManageAccountActivity;
import com.example.shirtsalesapp.activity.manager.ManagePaymentActivity;
import com.example.shirtsalesapp.activity.manager.ManageProductActivity;
import com.example.shirtsalesapp.api.ProductAPI;
import com.example.shirtsalesapp.model.Cart;
import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.api.ProductAPI;
import com.example.shirtsalesapp.model.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
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
    private ImageButton btnSearch, btnSearchInside;
    private LinearLayout searchContainer;
    private View outsideView;
    private ImageView iconFilter, iconCart;
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
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        btnSearchInside = findViewById(R.id.btn_search_inside);
        searchContainer = findViewById(R.id.search_container);
//        iconFilter = findViewById(R.id.iconFilter);
        iconCart = findViewById(R.id.icon_cart);
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

//        iconFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                ChooseProductDialogFragment dialogFragment = new ChooseProductDialogFragment();
//                dialogFragment.show(fragmentManager, "dialog_choose_product");
//            }
//        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewCustomer);
        setupBottomNavigation(bottomNavigationView);

        iconCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
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
                    productAdapter = new ProductAdapter(ProductListActivity.this,productList, 1);
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Log.e("API_ERROR", "Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void performSearch(String query) {
        if (!TextUtils.isEmpty(query)) {
            List<Product> filteredList = new ArrayList<>();
            String[] keywords = query.toLowerCase().split("\\s+"); // Tách từ khóa tìm kiếm thành mảng từ

            for (Product product : productList) {
                String productName = product.getProductName().toLowerCase();
                boolean found = true;

                // Kiểm tra từng từ khóa trong query xem có trong tên sản phẩm không
                for (String keyword : keywords) {
                    if (!productName.contains(keyword)) {
                        found = false;
                        break;
                    }
                }

                if (found) {
                    filteredList.add(product);
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No products found for: " + query, Toast.LENGTH_SHORT).show();
            } else {
                productAdapter.updateProductList(filteredList);
            }
        } else {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
        }
    }


    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Log.e("MenuManage", "Activity");

                if (id == R.id.product) {
                    startActivity(new Intent(ProductListActivity.this, ManageProductActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.account) {
                    startActivity(new Intent(ProductListActivity.this, ManageAccountActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.payment) {
                    startActivity(new Intent(ProductListActivity.this, ManagePaymentActivity.class));
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
