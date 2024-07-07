package com.example.shirtsalesapp.activity.cart;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.model.Cart;
import com.example.shirtsalesapp.model.CartProduct;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartProductAdapter adapter;
    private CartManager cartManager;
    private List<CartProduct> cartProducts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.recycler_view_cart_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartManager = new CartManager(this);

        Cart cart = cartManager.loadCart();
        if (cart != null) {
            // Load cart products from the saved cart
            cartProducts = cart.getProducts(); // Assuming a getProducts method exists
        }

        adapter = new CartProductAdapter(this, cartProducts);
        recyclerView.setAdapter(adapter);
    }
}