package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.adapters.RoomAdminAdapter;
import com.example.g6podbookingsystem.models.Room;
import com.example.g6podbookingsystem.services.ApiClient;
import com.example.g6podbookingsystem.services.RoomApi;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomListFragment extends Fragment implements RoomAdminAdapter.OnRoomListener {

    private static final String TAG = "RoomListFragment";

    private RecyclerView recyclerView;
    private RoomAdminAdapter roomAdapter;
    private List<Room> roomList = new ArrayList<>();
    private RoomApi roomApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_rooms);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_room);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);

        // Nút Back trên Toolbar
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Nút FAB (tạo mới)
        fab.setOnClickListener(v -> {
            // Mở RoomDetailFragment ở chế độ tạo mới (không truyền room)
            openDetailFragment(null);
        });

        // Khởi tạo Adapter và RecyclerView
        setupRecyclerView();

        // Lấy service
        roomApi = ApiClient.getRoomApi();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Gọi API để lấy danh sách
        fetchRooms();
    }

    private void setupRecyclerView() {
        roomAdapter = new RoomAdminAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(roomAdapter);
    }

    private void fetchRooms() {
        roomApi.getAllRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    roomList = response.body();
                    roomAdapter.setRooms(roomList);
                    Log.d(TAG, "Lấy danh sách phòng thành công: " + roomList.size());
                } else {
                    Log.e(TAG, "Lỗi khi lấy danh sách phòng: " + response.code());
                    Toast.makeText(getContext(), "Lỗi khi tải danh sách phòng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Log.e(TAG, "API Call Failed: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Xử lý khi nhấn vào một item
    @Override
    public void onRoomClick(int position) {
        Room clickedRoom = roomList.get(position);
        // Mở RoomDetailFragment ở chế độ xem/sửa (truyền room)
        openDetailFragment(clickedRoom);
    }

    private void openDetailFragment(Room room) {
        RoomDetailFragment detailFragment = new RoomDetailFragment();

        if (room != null) {
            // Chế độ sửa: Truyền đối tượng Room qua Bundle
            Bundle args = new Bundle();
            args.putParcelable("room", room);
            detailFragment.setArguments(args);
        }
        // Nếu room == null, fragment sẽ ở chế độ tạo mới

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, detailFragment) // Dùng ID container của bạn
                .addToBackStack(null)
                .commit();
    }

    // Khi quay lại fragment này (ví dụ sau khi Sửa/Tạo), ta nên làm mới danh sách
    @Override
    public void onResume() {
        super.onResume();
        fetchRooms();
    }
}
