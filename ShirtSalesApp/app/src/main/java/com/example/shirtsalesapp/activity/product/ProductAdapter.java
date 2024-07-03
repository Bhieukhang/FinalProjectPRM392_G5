package com.example.shirtsalesapp.activity.product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.activity.product.ProductListActivity;
import com.google.gson.Gson;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card_item_product,
                parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(String.format("$%s", product.getPrice()));
        // Assuming you have a way to load images from a URL or resource
        // holder.productImage.setImageResource(product.getImageResource());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductListActivity.class);
            intent.putExtra("product", new Gson().toJson(product));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName, productPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.tv_product_name);
            productPrice = itemView.findViewById(R.id.tv_product_price);
        }
    }
}
