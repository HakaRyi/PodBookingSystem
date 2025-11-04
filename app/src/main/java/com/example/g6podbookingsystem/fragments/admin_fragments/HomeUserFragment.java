package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.adapters.RoomAdapter;
import com.example.g6podbookingsystem.models.Room;
import com.example.g6podbookingsystem.repositories.RoomRepository;
import com.example.g6podbookingsystem.services.RoomApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeUserFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;

    private final double DEST_LAT = 10.8411;
    private final double DEST_LNG = 106.8094;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        RecyclerView rvPods = view.findViewById(R.id.rvFeaturedPods);
        TextView tvPlaceholder = view.findViewById(R.id.tvPlaceholder);
        MaterialButton btnNavigate = view.findViewById(R.id.btnNavigate);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        btnNavigate.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                openGoogleMapWithDirections();
            }
        });

        RoomApi api = RoomRepository.getRoomService();
        api.getAllRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Room> rooms = response.body();
                    if (rooms.isEmpty()) {
                        tvPlaceholder.setText("Không có phòng nào.");
                    } else {
                        tvPlaceholder.setVisibility(View.GONE);
                        RoomAdapter adapter = new RoomAdapter(getContext(), rooms);
                        rvPods.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                tvPlaceholder.setText("Lỗi kết nối: " + t.getMessage());
            }
        });
        return view;
    }

    private void openGoogleMapWithDirections() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double currentLat = location.getLatitude();
                        double currentLng = location.getLongitude();

                        String uri = "https://www.google.com/maps/dir/?api=1"
                                + "&origin=" + currentLat + "," + currentLng
                                + "&destination=" + DEST_LAT + "," + DEST_LNG
                                + "&travelmode=driving";

                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(requireContext(),
                                "Không lấy được vị trí hiện tại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGoogleMapWithDirections();
            } else {
                Toast.makeText(requireContext(),
                        "Cần cấp quyền truy cập vị trí để sử dụng tính năng này!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}