package com.example.shirtsalesapp.activity.manager;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.activity.manager.account.AccountActiveFragment;
import com.example.shirtsalesapp.activity.manager.account.AccountActivity;
import com.example.shirtsalesapp.activity.manager.account.AccountInactiveFragment;
import com.example.shirtsalesapp.api.AccountAPI;
import com.example.shirtsalesapp.model.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManageAccountActivity extends AppCompatActivity {
    TextView totalActive, totalInActive, totalAccount, linkActive, linkInactive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        totalActive = findViewById(R.id.tv_total_account_active);
        totalInActive = findViewById(R.id.tv_total_account_inactive);
        totalAccount = findViewById(R.id.circle_text);

        linkActive = findViewById(R.id.link_active);
        linkInactive = findViewById(R.id.link_inactive);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupBottomNavigation(bottomNavigationView);
        linkActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageAccountActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        linkInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageAccountActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        AccountAPI apiService = retrofit.create(AccountAPI.class);

        //Call count account
        Call<Integer> callAccount = apiService.getCountAccount();
        callAccount.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> callAccount, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Integer count = response.body();
                    Log.d(TAG, "Account Count: " + count);
                    totalAccount.setText(String.valueOf(count));
                } else {
                    Log.e(TAG, "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "Network request failed", t);
            }
        });

        //Call count account active - status = 1
        Call<Integer> call = apiService.getCountAcc(0);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Integer count = response.body();
                    Log.d(TAG, "InActive Count: " + count);
                    totalInActive.setText(String.valueOf(count));
                } else {
                    Log.e(TAG, "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "Network request failed", t);
            }
        });


        //Call count account inactive - status = 0
        Call<Integer> callActive = apiService.getCountAcc(1);
        callActive.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> callActive, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Integer count = response.body();
                    Log.d(TAG, "Active Count: " + count);
                    totalActive.setText(String.valueOf(count));
                } else {
                    Log.e(TAG, "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "Network request failed", t);
            }
        });
    }

    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Log.e("MenuManage", "Activity");

                if (id == R.id.product) {
                    startActivity(new Intent(ManageAccountActivity.this, ManageProductActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.account) {
                    startActivity(new Intent(ManageAccountActivity.this, ManageAccountActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.payment) {
                    startActivity(new Intent(ManageAccountActivity.this, ManagePaymentActivity.class));
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}