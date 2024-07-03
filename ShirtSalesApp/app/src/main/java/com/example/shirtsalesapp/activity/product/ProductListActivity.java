package com.example.shirtsalesapp.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.model.response.ProductResponse;
import com.example.shirtsalesapp.service.ProductService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        loadProducts();
//        String img = "https://down-vn.img.susercontent.com/file/vn-11134207-7qukw-lg9k79viu86v8c_tn";
        // Dummy data
//        productList = new ArrayList<>();
//        productList.add(new Product("GodMother", 100.0, img));
//        productList.add(new Product("GodMother", 200.0, img));
//        productList.add(new Product("GodMother", 200.0, img));
//        productList.add(new Product("GodMother", 200.0, img));
//        productList.add(new Product("GodMother", 200.0, img));
//        productList.add(new Product("GodMother", 200.0, img));
        // Add more products

//        productAdapter = new ProductAdapter(this, productList);
//        recyclerView.setAdapter(productAdapter);
    }

    private void loadProducts() {
        ProductService.getApi().getProductList().enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body().getProducts();
                    productAdapter = new ProductAdapter(ProductListActivity.this, productList);
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Toast.makeText(ProductListActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
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
}
