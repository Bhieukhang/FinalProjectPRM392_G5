package com.example.shirtsalesapp.activity.product;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.activity.cart.CartActivity;
import com.example.shirtsalesapp.activity.cart.CartManager;
import com.example.shirtsalesapp.api.CategoryAPI;
import com.example.shirtsalesapp.model.Cart;
import com.example.shirtsalesapp.model.CartProduct;
import com.example.shirtsalesapp.model.Category;
import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.model.RetrofitClient;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducDetailActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productName;
    private TextView productDecription;
    private TextView productPrice;
    private TextView productCategory;
    private CategoryAPI categoryAPI;
    private Category category;
    private Button addToCart;
    private CartManager cartManager;
    //private Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_produc_detail);


        cartManager = new CartManager(this);

        Product product = (Product) getIntent().getSerializableExtra("productItem");

        productImage = findViewById(R.id.image_product);
        productName = findViewById(R.id.text_product_name);
        productDecription = findViewById(R.id.text_product_description);
        productPrice = findViewById(R.id.text_product_price);
        productCategory = findViewById(R.id.text_category);
        addToCart = findViewById(R.id.button_add_to_cart);
        categoryAPI = RetrofitClient.getRetrofitInstance().create(CategoryAPI.class);
        // Display
        if(product != null){
            productName.setText("Name: " + product.getProductName());
            productDecription.setText("Decription: " + product.getDescription());
            productPrice.setText("Price: " + String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
            //productCategory.setText("Loai: ");
            fetchCategory(product.getCategoryId());
            loadImage(this, product.getImageUrl(), productImage);
        }
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart = cartManager.loadCart();
                if (cart == null) {
                    cart = new Cart();
                    cart.setUserId(1);  // Assuming a user ID here
                }
                CartProduct cartProduct = new CartProduct();
                cartProduct.setProductID(product.getId());
                cartProduct.setQuantity(1);  // Default quantity
                cart.addProduct(cartProduct);
                cartManager.saveCart(cart);

                Toast.makeText(v.getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    private void fetchCategory(int categoryId) {
        categoryAPI.getCategoryById(categoryId).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if(response.isSuccessful() && response.body() != null){
                    category = response.body();
                    productCategory.setText("Category: " + category.getTitle());
                }else{
                    Toast.makeText(ProducDetailActivity.this,"Lỏ",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("Glide", "Fail to get data");
            }
        });
    }
    private static void loadImage(Context context, String imageUrl, ImageView imageView) {
        if (context == null) {
            Log.e("Glide", "Context is null");
            return;
        }
        if (imageView == null) {
            Log.e("Glide", "ImageView is null");
            return;
        }
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            Log.e("Glide", "Image load failed", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.d("Glide", "Image loaded successfully");
                            return false;
                        }
                    })
                    .into(imageView);
        } else {
            Log.e("Glide", "Image URL is null or empty");
            // Optionally, set a placeholder or error image if URL is null
            imageView.setImageResource(R.drawable.img_ao_demo);
        }
    }
}