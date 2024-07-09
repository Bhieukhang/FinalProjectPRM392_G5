package com.example.shirtsalesapp.activity.manager.account;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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
        View view = inflater.inflate(R.layout.activity_account_active, container, false);
        Log.e("AccountActiveFragment", "view: " + view);
        listView = view.findViewById(R.id.listViewAccountActive);
        userList = new ArrayList<>();
        adapter = new AccountAdapter(requireContext(), userList);
        listView.setAdapter(adapter);

        setupListViewClickListener();

        fetchDataFromApi();

        return view;
    }

    private void setupListViewClickListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = userList.get(position);
            showDeleteConfirmation(selectedUser);
        });
    }

    private void showDeleteConfirmation(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Delete");
        builder.setMessage("Do you want to delete account?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            deleteUser(user.getId());
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteUser(int userId) {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        AccountAPI apiService = retrofit.create(AccountAPI.class);
        Call<Void> call = apiService.deleteUser(userId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa thành công, cập nhật lại danh sách
                    userList.removeIf(user -> user.getId() == userId);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("AccountActiveFragment", "Delete request failed: " + response.message());
                    Toast.makeText(requireContext(), "Failed to delete account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AccountActiveFragment", "Network request failed", t);
                Toast.makeText(requireContext(), "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
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
