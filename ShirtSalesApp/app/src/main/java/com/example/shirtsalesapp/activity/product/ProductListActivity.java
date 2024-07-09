package com.example.shirtsalesapp.activity.product;

import static com.example.shirtsalesapp.R.id.bottomNavigationViewCustomer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.activity.manager.ManageAccountActivity;
import com.example.shirtsalesapp.activity.manager.ManagePaymentActivity;
import com.example.shirtsalesapp.activity.manager.ManageProductActivity;
import com.example.shirtsalesapp.api.ProductAPI;
import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.api.ProductAPI;
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
    private ImageButton btnSearch, btnSearchInside;
    private LinearLayout searchContainer;
    private View outsideView;
    private ImageView iconFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cloth);

        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        btnSearchInside = findViewById(R.id.btn_search_inside);
        searchContainer = findViewById(R.id.search_container);
        iconFilter = findViewById(R.id.iconFilter);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContainer.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.GONE);
            }
        });

        btnSearchInside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch(etSearch.getText().toString());
            }
        });

        iconFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                ChooseProductDialogFragment dialogFragment = new ChooseProductDialogFragment();
                dialogFragment.show(fragmentManager, "dialog_choose_product");
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewCustomer);
        setupBottomNavigation(bottomNavigationView);

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
                    productAdapter = new ProductAdapter(productList, 1);
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
            // Handle the search functionality here
            Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_SHORT).show();
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
