package com.example.g6podbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.adapters.RoomAdapter;
import com.example.g6podbookingsystem.models.Room;
import com.example.g6podbookingsystem.repositories.RoomRepository;
import com.example.g6podbookingsystem.services.RoomApi;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindRoomActivity extends AppCompatActivity {

    private RecyclerView rvAllRooms;
    private TextView tvEmpty;
    private TextInputEditText etSearch;
    private RoomAdapter adapter;
    private List<Room> allRooms = new ArrayList<>();
    private List<Room> filteredRooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // View
        rvAllRooms = findViewById(R.id.rvAllRooms);
        tvEmpty = findViewById(R.id.tvEmpty);
        etSearch = findViewById(R.id.etSearch);

        rvAllRooms.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomAdapter(this, filteredRooms);
        rvAllRooms.setAdapter(adapter);

        // Click vào card → RoomDetailActivity
        adapter.setOnItemClickListener(room -> {
            Intent intent = new Intent(FindRoomActivity.this, RoomDetailActivity.class);
            intent.putExtra("room", room);
            startActivity(intent);
        });

        // Gọi API lấy tất cả phòng
        loadAllRooms();

        // Search realtime
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filterRooms(s.toString());
            }
        });
    }

    private void loadAllRooms() {
        RoomApi api = RoomRepository.getRoomService();
        api.getAllRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allRooms = response.body();
                    filteredRooms.clear();
                    filteredRooms.addAll(allRooms);
                    adapter.notifyDataSetChanged();
                    updateEmptyState();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                tvEmpty.setText("Lỗi kết nối");
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void filterRooms(String query) {
        filteredRooms.clear();
        if (query.isEmpty()) {
            filteredRooms.addAll(allRooms);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Room room : allRooms) {
                if (room.getName() != null && room.getName().toLowerCase().contains(lowerQuery)) {
                    filteredRooms.add(room);
                }
            }
        }
        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (filteredRooms.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvAllRooms.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvAllRooms.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}