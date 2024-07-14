package com.example.shirtsalesapp.activity.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.ZaloPayMethodActivity;
import com.example.shirtsalesapp.activity.product.ProducDetailActivity;
import com.example.shirtsalesapp.activity.product.ProductListActivity;
import com.example.shirtsalesapp.api.ProductAPI;
import com.example.shirtsalesapp.model.Cart;
import com.example.shirtsalesapp.model.CartProduct;
import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.model.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartProductAdapter adapter;
    private CartManager cartManager;

    private Button PayButton;
    private TextView total;
    private double totalPrice;
    private ImageButton back;
    private List<CartProduct> cartProducts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //updateTotalPrice(0);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.recycler_view_cart_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Hien thi tong tien
        total = findViewById(R.id.text_total);
        // Nut Pay
        PayButton = findViewById(R.id.button_checkout);
        cartManager = new CartManager(this);
        // Add new Cart to here when have User
        Cart cart = cartManager.loadCart();
        List<CartProduct> del = new ArrayList<>();
        if (cart != null) {
            for(CartProduct c : cart.getProducts()){
                if(c.getStatus()==0) del.add(c);
                else cartProducts.add(c);
                // Assuming a getProducts method existselse ;
            }
            cart.getProducts().removeAll(del);
            cartManager.saveCart(cart);

            for(CartProduct cartProduct : cartProducts){
                Retrofit retrofit = RetrofitClient.getRetrofitInstance();
                ProductAPI productAPI = retrofit.create(ProductAPI.class);
                productAPI.getProductById(cartProduct.getProductId()).enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if(response.isSuccessful() && response.body() != null){
                            totalPrice += cartProduct.getQuantity() * response.body().getPrice();
                            Log.d("API Response", "Response received"+ response.body().getId());
                            total.setText("Total: " + totalPrice);
                            Log.d("Product Get id","Successs" + totalPrice);
                        }else{
                            Log.d("Product Get id","Fail");
                        }
                    }
                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Log.d("Product Get id","Fail API");
                    }
                });
            }
        }

        adapter = new CartProductAdapter(this, cartProducts);
        recyclerView.setAdapter(adapter);
        back = findViewById(R.id.button_back);
        PayButton = findViewById(R.id.button_checkout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductListActivity.class);

                v.getContext().startActivity(intent);
            }
        });
        PayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalText = total.getText().toString();
                String numericTotal = totalText.replaceAll("[^\\d.]", ""); // Remove all non-numeric characters except decimal point
                try {
                    double totalPrice = Double.parseDouble(numericTotal);
                    Log.d("CartActivity", "Total Price: " + totalPrice);

                    Intent intent = new Intent(CartActivity.this, ZaloPayMethodActivity.class);
                    intent.putExtra("TOTAL_AMOUNT", String.valueOf((int) totalPrice)); // Convert to int to remove decimal if it's ".0"
                    intent.putExtra("AUTOMATIC_CREATE_ORDER", true);
                    startActivity(intent);

                } catch (NumberFormatException e) {
                    Log.e("CartActivity", "Error parsing total price", e);
                    Toast.makeText(CartActivity.this, "Error parsing total price", Toast.LENGTH_SHORT).show();
                }

            }
        });
//        PreferencesManager preferencesManager = new PreferencesManager(CartActivity.this);
//        double totalPrice = preferencesManager.getTotalPrice();
       // total.setText("Total: " + String.valueOf(totalPrice));
    }
    public void updateTotalPrice(double newTotal) {
        PreferencesManager preferencesManager = new PreferencesManager(CartActivity.this);
        preferencesManager.saveTotalPrice(newTotal);
    }
}