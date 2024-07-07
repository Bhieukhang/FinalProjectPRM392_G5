package com.example.shirtsalesapp.activity.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.model.CartProduct;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {

    private List<CartProduct> cartProducts;
    private Context context;

    public CartProductAdapter(Context context, List<CartProduct> cartProducts) {
        this.context = context;
        this.cartProducts = cartProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartProduct cartProduct = cartProducts.get(position);
        holder.textViewQuantity.setText(String.valueOf(cartProduct.getQuantity()));

        holder.buttonIncrease.setOnClickListener(v -> {
            cartProduct.setQuantity(cartProduct.getQuantity() + 1);
            notifyItemChanged(position);
        });

        holder.buttonDecrease.setOnClickListener(v -> {
            if (cartProduct.getQuantity() > 1) {
                cartProduct.setQuantity(cartProduct.getQuantity() - 1);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuantity;
        ImageButton buttonIncrease, buttonDecrease;

        ViewHolder(View itemView) {
            super(itemView);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            buttonIncrease = itemView.findViewById(R.id.button_increase);
            buttonDecrease = itemView.findViewById(R.id.button_decrease);
        }
    }
}


