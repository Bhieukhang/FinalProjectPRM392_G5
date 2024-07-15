package com.example.shirtsalesapp.activity.manager.account;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyFragmentAdapter extends FragmentStateAdapter {

    public MyFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Log.e("MyFragment", "AccountActiveFragment");
                return new AccountActiveFragment();
            case 1:
                Log.e("MyFragment", "AccountInactiveFragment");
                return new AccountInactiveFragment();
            default:
                return new AccountActiveFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
