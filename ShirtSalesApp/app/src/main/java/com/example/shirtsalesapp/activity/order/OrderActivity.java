package com.example.shirtsalesapp.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.activity.cart.CartManager;
import com.example.shirtsalesapp.activity.cart.CartProductAdapter;
import com.example.shirtsalesapp.activity.product.ProductListActivity;
import com.example.shirtsalesapp.api.OrderService;
import com.example.shirtsalesapp.api.ProductAPI;
import com.example.shirtsalesapp.model.Cart;
import com.example.shirtsalesapp.model.CartProduct;
import com.example.shirtsalesapp.model.CreateOrderRequest;
import com.example.shirtsalesapp.model.Product;
import com.example.shirtsalesapp.model.RetrofitClient;
import com.example.shirtsalesapp.model.order.CreateOrderDTO;
import com.example.shirtsalesapp.model.order.CreateOrderDetailDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartProductAdapter adapter;
    private CartManager cartManager;
    private List<CartProduct> cartProducts = new ArrayList<>();
    private double totalPrice;
    private TextView total;
    OrderService orderService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        orderService = OrderRepository.getOrderService();
//        recyclerView = findViewById(R.id.recycler_view_cart_items);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                        if(response.isSuccessful() && response.body() != null) {
                            totalPrice += cartProduct.getQuantity() * response.body().getPrice();
                            Log.d("API Response", "Response received" + response.body().getId());
                            total.setText("Total: " + totalPrice);
                            Log.d("Product Get id", "Successs" + totalPrice);
                            CreateOrderDTO createOrderDTO = new CreateOrderDTO();
                            createOrderDTO.FinalPrice = totalPrice;
                            createOrderDTO.Address = "Quận 9, Thủ Đức, Thành Phố Hồ Chí Minh";
                            createOrderDTO.PaymentId = 1;
                            createOrderDTO.ShippingId = 1;
                            createOrderDTO.UserId = 1;
                            createOrderDTO.VoucherId = 1;
                            ArrayList<CreateOrderDetailDTO> list = new ArrayList<>();
                            for (CartProduct item : cartProducts) {
                                list.add(new CreateOrderDetailDTO(item.getProductId(), item.getQuantity(), response.body().getPrice()));
                            }
                            CreateOrderRequest orderRequest = new CreateOrderRequest();
                            orderRequest.OrderDTO = createOrderDTO;
                            orderRequest.OrderDetailDTO = list;
                            try{
                            Call<CreateOrderRequest> calling = orderService.createOrder(orderRequest);
                            calling.enqueue(new Callback<CreateOrderRequest>() {
                                @Override
                                public void onResponse(Call<CreateOrderRequest> call, Response<CreateOrderRequest> response) {

                                }

                                @Override
                                public void onFailure(Call<CreateOrderRequest> call, Throwable t) {

                                }
                            });
                        } catch(Exception e){
                                Log.d("t",e.getMessage());
                            }
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

        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
        finish();
//        adapter = new CartProductAdapter(this, cartProducts);
//        recyclerView.setAdapter(adapter);
    }
}