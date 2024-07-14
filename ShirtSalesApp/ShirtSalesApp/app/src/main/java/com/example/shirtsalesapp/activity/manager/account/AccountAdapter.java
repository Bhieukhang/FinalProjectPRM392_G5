package com.example.shirtsalesapp.activity.manager.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shirtsalesapp.R;
import com.example.shirtsalesapp.model.User;

import java.util.List;

public class AccountAdapter extends ArrayAdapter<User> {

    public AccountAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_account, parent, false);
        }

        User currentUser = getItem(position);

        TextView userNameTextView = listItemView.findViewById(R.id.userName);
        TextView statusTextView = listItemView.findViewById(R.id.status);

        if (currentUser != null) {
            userNameTextView.setText(currentUser.getUserName());
            statusTextView.setText(currentUser.getStatus() == 1 ? "Active" : "Inactive");
        }

        return listItemView;
    }
}

