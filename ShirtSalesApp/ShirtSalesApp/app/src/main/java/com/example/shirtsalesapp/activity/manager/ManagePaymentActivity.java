package com.example.shirtsalesapp.activity.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.shirtsalesapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ManagePaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_payment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupBottomNavigation(bottomNavigationView);
    }

    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Log.e("MenuManage", "Activity");

                if (id == R.id.product) {
                    startActivity(new Intent(ManagePaymentActivity.this, ManageProductActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.account) {
                    startActivity(new Intent(ManagePaymentActivity.this, ManageAccountActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.payment) {
                    startActivity(new Intent(ManagePaymentActivity.this, ManagePaymentActivity.class));
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}