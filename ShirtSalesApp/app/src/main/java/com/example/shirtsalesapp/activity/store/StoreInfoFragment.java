package com.example.shirtsalesapp.activity.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shirtsalesapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class StoreInfoFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView storeNameTextView;
    private TextView storeAddressTextView;
    private TextView storeBusinessTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_store_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize the TextViews
        storeNameTextView = view.findViewById(R.id.store_name);
        storeAddressTextView = view.findViewById(R.id.store_address);
        storeBusinessTextView = view.findViewById(R.id.store_business);

        // Set store information
        storeNameTextView.setText("Bao Store");
        storeAddressTextView.setText("Lưu Hữu Phước, Đông Hoà, Dĩ An, Bình Dương, Vietnam");
        storeBusinessTextView.setText("Shirt");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker and move the camera
        LatLng storeLocation = new LatLng(10.876260252078009, 106.8005408338596);
        mMap.addMarker(new MarkerOptions().position(storeLocation).title("Bao Store"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 15));
    }
}
