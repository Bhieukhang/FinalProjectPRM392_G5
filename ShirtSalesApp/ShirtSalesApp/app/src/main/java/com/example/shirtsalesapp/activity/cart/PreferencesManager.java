package com.example.shirtsalesapp.activity.cart;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREFS_NAME = "appPreferences";
    private static final String KEY_TOTAL_PRICE = "totalPrice";
    private SharedPreferences sharedPreferences;

    public PreferencesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveTotalPrice(double total) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(KEY_TOTAL_PRICE, (float) total);
        editor.apply();
    }

    public double getTotalPrice() {
        return sharedPreferences.getFloat(KEY_TOTAL_PRICE, 0.0f);  // Default value is 0.0
    }
}
