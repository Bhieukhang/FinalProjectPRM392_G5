package com.example.shirtsalesapp.activity.cart;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.activity.product.ProducDetailActivity;
import com.example.shirtsalesapp.api.ProductAPI;
import com.example.shirtsalesapp.model.Cart;
import com.example.shirtsalesapp.model.CartProduct;
import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.model.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {

    private List<CartProduct> cartProducts;
    private Context context;
    private CartManager cartManager;
    private int total;
    public CartProductAdapter(Context context, List<CartProduct> cartProducts) {
        this.context = context;
        this.cartProducts = cartProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_product_item, parent, false);
        cartManager = new CartManager(view.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartProduct cartProduct = cartProducts.get(position);
        holder.textViewQuantity.setText(String.valueOf(cartProduct.getQuantity()));
        Cart cart = cartManager.loadCart();
        // Get product
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ProductAPI apiService = retrofit.create(ProductAPI.class);

        Call<Product> call = apiService.getProductById(cartProduct.getProductId());

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    if (product != null) {
                        Log.d("API Response", "Response received "+ product.getId());
                        holder.productName.setText(product.getProductName());
                        holder.productPrice.setText(String.format("%.2f", product.getPrice()));

                        loadImage(context, product.getImageUrl(), holder.productImage);
                    } else {
                        Log.d("API Error", "No product found in response body");
                    }
                } else {
                    Log.d("API Error", "Unsuccessful API call with code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("API Failure", "Failed to fetch product: " + t.getMessage());
            }
        });


        holder.buttonIncrease.setOnClickListener(v -> {
            //cartProduct.setQuantity(cartProduct.getQuantity() + 1);

            boolean check = true;
            for(CartProduct c : cart.getProducts()){
                if(c.getProductId() == cartProduct.getProductId()){
                    c.setQuantity(c.getQuantity()+1);
                    check = false;
                }
            }

            cartManager.saveCart(cart);
            Intent intent = new Intent(v.getContext(),CartActivity.class);
            v.getContext().startActivity(intent);
            Log.d("cart","Tang thanh cong");

        });

        holder.buttonDecrease.setOnClickListener(v -> {
            if (cartProduct.getQuantity() > 1) {
                boolean check = true;
                for(CartProduct c : cart.getProducts()){
                    if(c.getProductId() == cartProduct.getProductId()){
                        c.setQuantity(c.getQuantity()-1);
                        check = false;
                    }
                }
                cartManager.saveCart(cart);
                Intent intent = new Intent(v.getContext(),CartActivity.class);
                v.getContext().startActivity(intent);
                Log.d("cart","Giam thanh cong");

            }
            if(cartProduct.getQuantity() == 1){
                boolean check = true;
                for(CartProduct c : cart.getProducts()){
                    if(c.getProductId() == cartProduct.getProductId()){
                        c.setStatus(0);
                    }
                }

                cartManager.saveCart(cart);
                Intent intent = new Intent(v.getContext(),CartActivity.class);
                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), "San pham da bi xoa khoi cua hang", Toast.LENGTH_SHORT).show();
            }
        });

        //holder.productPrice.setText("Price: " + );

    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuantity;
        ImageButton buttonIncrease, buttonDecrease;
        ImageView productImage;
        TextView productPrice, productName;

        ViewHolder(View itemView) {
            super(itemView);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            buttonIncrease = itemView.findViewById(R.id.button_increase);
            buttonDecrease = itemView.findViewById(R.id.button_decrease);
            productPrice = itemView.findViewById(R.id.text_product_price);
            productImage = itemView.findViewById(R.id.image_product);
            productName = itemView.findViewById(R.id.text_product_name);
        }
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


