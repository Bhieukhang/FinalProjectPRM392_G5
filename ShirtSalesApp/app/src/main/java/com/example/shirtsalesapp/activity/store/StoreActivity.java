package com.example.shirtsalesapp.activity.store;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.shirtsalesapp.R;

public class StoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new StoreInfoFragment())
                    .commitNow();
        }
    }
}