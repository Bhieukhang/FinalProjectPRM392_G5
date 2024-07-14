package com.example.shirtsalesapp.activity.product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.shirtsalesapp.activity.auth.LoginActivity;
import com.example.shirtsalesapp.activity.cart.CartManager;
import com.example.shirtsalesapp.model.Cart;
import com.example.shirtsalesapp.model.CartProduct;
import com.example.shirtsalesapp.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> productList;
    private int viewType;
    private CartManager cartManager;
    private Cart cart;

    public ProductAdapter(Context context, List<Product> productList, int type) {
        this.productList = productList;
        this.viewType = type;
        this.cartManager = new CartManager(context);
    }

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_card_item_product, parent, false);
            cartManager = new CartManager(view.getContext());

            return new ProductViewHolder(view);
        } else if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product, parent, false);
            cartManager = new CartManager(view.getContext());

            return new ProductItemViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_card_item_product, parent, false);
            cartManager = new CartManager(view.getContext());

            return new ProductViewHolder(view);
        }
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = productList.get(position);
        cart = cartManager.loadCart();
        if (holder instanceof ProductViewHolder) {
            ((ProductViewHolder) holder).bind(product);
            // Add On click
            ((ProductViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ProducDetailActivity.class);

                    intent.putExtra("productItem", product);
                    v.getContext().startActivity(intent);
                }
            });
            ((ProductViewHolder) holder).addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    if (isLoggedIn(context)) {
                        addToCart(context, product);
                    } else {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("productItem", product);
                        context.startActivity(intent);
                    }
                }
            });
        } else if (holder instanceof ProductItemViewHolder) {
            ((ProductItemViewHolder) holder).bind(product);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void addToCart(Context context, Product product) {
        cart = cartManager.loadCart();
        if (cart == null) {
            cart = new Cart();
            // Đặt userId cho giỏ hàng nếu cần thiết
            // cart.setUserId(userId);
        }

        boolean check = true;
        for (CartProduct c : cart.getProducts()) {
            if (c.getProductId() == product.getId()) {
                c.setQuantity(c.getQuantity() + 1);
                Toast.makeText(context, "Duplicated item +1 Quantity", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
    }

    //Activity ProductList
    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView productPrice;
        ImageView productImage;
        ImageButton addToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tv_product_name);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            productImage = itemView.findViewById(R.id.product_image);
            addToCart = itemView.findViewById(R.id.button_addToCart);
        }

        public void bind(Product product) {
            productName.setText(product.getProductName());
            productPrice.setText(String.valueOf(product.getPrice()));
            Context context = itemView.getContext();
            loadImage(context, product.getImageUrl(), productImage);
        }
    }

    //Activity Manager Product
    static class ProductItemViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView productPrice;
        ImageView productImage;

        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.textProductName);
            productPrice = itemView.findViewById(R.id.textPrice);
            productImage = itemView.findViewById(R.id.imageViewProduct);
        }

        public void bind(Product product) {
            productName.setText(product.getProductName());
            productPrice.setText(String.valueOf(product.getPrice()));
            Context context = itemView.getContext();
            loadImage(context, product.getImageUrl(), productImage);
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

    public void updateProductList(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }
}

