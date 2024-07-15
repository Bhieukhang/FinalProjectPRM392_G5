package com.example.shirtsalesapp.activity.manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shirtsalesapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

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
                    startActivity(new Intent(MenuManageActivity.this, ManageProductActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.account) {
                    startActivity(new Intent(MenuManageActivity.this, ManageAccountActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.payment) {
                    startActivity(new Intent(MenuManageActivity.this, ManagePaymentActivity.class));
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_bar_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.e("MenuManage", "Activity");
        if (id == R.id.product) {
            startActivity(new Intent(this, ManageProductActivity.class));
            finish();
            return true;
        } else if (id == R.id.account) {
            startActivity(new Intent(this, ManageAccountActivity.class));
            finish();
            return true;
        } else if (id == R.id.payment) {
            startActivity(new Intent(this, ManagePaymentActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
