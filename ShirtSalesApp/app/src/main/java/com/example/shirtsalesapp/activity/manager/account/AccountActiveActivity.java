package com.example.shirtsalesapp.activity.manager.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.api.AccountAPI;
import com.example.shirtsalesapp.model.RetrofitClient;
import com.example.shirtsalesapp.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountActiveActivity extends AppCompatActivity {
    private ListView listView;
    private AccountAdapter adapter;
    private List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_active);

        listView = findViewById(R.id.listViewAccountActive);
        userList = new ArrayList<>();
        adapter = new AccountAdapter(this, userList);
        listView.setAdapter(adapter);

        fetchDataFromApi();
    }

    private void fetchDataFromApi() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        AccountAPI apiService = retrofit.create(AccountAPI.class);
        Call<List<User>> call = apiService.getAllUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    if (users != null) {
                        userList.clear();
                        for (User user : users) {
                            if (user.getStatus() == 1) {
                                userList.add(user);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("AccountActive", "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("AccountActive", "Network request failed", t);
            }
        });
    }
}