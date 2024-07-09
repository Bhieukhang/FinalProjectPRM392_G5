package com.example.shirtsalesapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.shirtsalesapp.activity.cart.CartManager;
import com.example.shirtsalesapp.activity.product.ProductListActivity;
import com.example.shirtsalesapp.model.Cart;
import com.example.shirtsalesapp.zaloApi.CreateOrder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class ZaloPayMethodActivity extends AppCompatActivity {
    TextView lblZpTransToken, txtToken,txtDate;
    Button btnCreateOrder, btnPay;
    TextView txtAmount;
    private void BindView() {
        txtToken = findViewById(R.id.txtToken);
        lblZpTransToken = findViewById(R.id.lblZpTransToken);
        btnCreateOrder = findViewById(R.id.btnCreateOrder);
        txtAmount = findViewById(R.id.txtAmount);
        btnPay = findViewById(R.id.btnPay);

        txtDate = findViewById(R.id.txtDate);
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("vi", "VN"));
        String formattedDate = dateFormat.format(currentDate);
        txtDate.setText(formattedDate);
        IsLoading();
    }
    private void IsLoading() {
        lblZpTransToken.setVisibility(View.INVISIBLE);
        txtToken.setVisibility(View.INVISIBLE);
        btnPay.setVisibility(View.INVISIBLE);
    }
    private void IsDone() {
        lblZpTransToken.setVisibility(View.VISIBLE);
        txtToken.setVisibility(View.VISIBLE);
        btnPay.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zalo_pay_method);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2554, Environment.SANDBOX);
        // bind components with ids
        BindView();
        // Retrieve total amount from Intent
        String totalAmount = getIntent().getStringExtra("TOTAL_AMOUNT");
        if (totalAmount != null) {
            txtAmount.setText(totalAmount);
        }
        // Automatically trigger btnCreateOrder click
        boolean automaticCreateOrder = getIntent().getBooleanExtra("AUTOMATIC_CREATE_ORDER", false);


        // handle CreateOrder
        btnCreateOrder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                CreateOrder orderApi = new CreateOrder();

                try {
                    JSONObject data = orderApi.createOrder(txtAmount.getText().toString());
                    Log.d("ZaloPayMethodActivity", "createOrder Response: " + data.toString());
                    String code = data.getString("return_code");
                    Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();

                    if ("1".equals(code)) {
                        lblZpTransToken.setText("zptranstoken");
                        txtToken.setText(data.getString("zp_trans_token"));
                        IsDone();
                    } else {
                        Log.e("ZaloPayMethodActivity", "createOrder failed with code: " + code);
                        Log.e("ZaloPayMethodActivity", "Error Message: " + data.getString("return_message"));
                        // Optionally log sub_return_code and sub_return_message if needed
                        Toast.makeText(getApplicationContext(), "Error: " + data.getString("return_message"), Toast.LENGTH_LONG).show();
                        // Handle error case here
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("ZaloPayMethodActivity", "Exception in createOrder: " + e.getMessage());
                    // Handle exception
                }

            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = txtToken.getText().toString();
                ZaloPaySDK.getInstance().payOrder(ZaloPayMethodActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(ZaloPayMethodActivity.this)
                                        .setTitle("Payment Success")
                                        .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                clearCartSession(); // Clear cart session if necessary

                                                // Create intent to navigate back to ProductListActivity
                                                Intent intent = new Intent(ZaloPayMethodActivity.this, ProductListActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish(); // Optional: finish current activity

                                                // Alternatively, if you want to keep ZaloPayMethodActivity in the stack
                                                //startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            }
                        });
                        IsLoading(); // Handle loading state or UI updates
                    }


                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        new AlertDialog.Builder(ZaloPayMethodActivity.this)
                                .setTitle("User Cancel Payment")
                                .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                        new AlertDialog.Builder(ZaloPayMethodActivity.this)
                                .setTitle("Payment Fail")
                                .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();
                    }
                });
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
    private void clearCartSession() {
        // Xóa session của giỏ hàng ở đây, ví dụ sử dụng CartManager
        CartManager cartManager = new CartManager(this);
        Cart cart = cartManager.loadCart();
        if (cart != null) {
            cart.clearProducts();
            cartManager.saveCart(cart);
        }
    }
}