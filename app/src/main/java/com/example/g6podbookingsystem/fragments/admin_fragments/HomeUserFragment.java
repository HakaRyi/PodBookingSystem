package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.adapters.RoomAdapter;
import com.example.g6podbookingsystem.models.Room;
import com.example.g6podbookingsystem.repositories.RoomRepository;
import com.example.g6podbookingsystem.services.RoomApi;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeUserFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        RecyclerView rvPods = view.findViewById(R.id.rvFeaturedPods);
        TextView tvPlaceholder = view.findViewById(R.id.tvPlaceholder);

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
}