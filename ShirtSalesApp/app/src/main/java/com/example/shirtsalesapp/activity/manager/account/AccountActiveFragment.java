package com.example.shirtsalesapp.activity.manager.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class AccountActiveFragment extends Fragment {
    private ListView listView;
    private AccountAdapter adapter;
    private List<User> userList;

    public AccountActiveFragment() {}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_account_active,
                container, false);
        Log.e("AccountActiveFragment", "view: " + view);
        listView = view.findViewById(R.id.listViewAccountActive);
        userList = new ArrayList<>();
        adapter = new AccountAdapter(requireContext(), userList);
        listView.setAdapter(adapter);

        fetchDataFromApi();

        return view;
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
                        Log.e("AccountActive", "List user: " + userList);

                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("AccountActiveFragment", "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("AccountActiveFragment", "Network request failed", t);
            }
        });
    }
}
