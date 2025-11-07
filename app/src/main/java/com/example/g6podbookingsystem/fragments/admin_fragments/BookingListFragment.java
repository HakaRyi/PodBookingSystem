package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.adapters.BookingAdapter; // <-- Dùng Adapter của bạn
import com.example.g6podbookingsystem.models.Booking;
import com.example.g6podbookingsystem.repositories.BookingRepository;
import com.example.g6podbookingsystem.services.ApiClient;
import com.example.g6podbookingsystem.services.BookingApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// KHÔNG cần "implements BookingAdapter.OnBookingClickListener" nữa
public class BookingListFragment extends Fragment {

    private static final String TAG = "BookingListFragment";

    private RecyclerView recyclerViewBookings;
    private ProgressBar progressBar;
    private BookingAdapter adapter; // <-- Adapter của bạn
    private List<Booking> bookingList = new ArrayList<>(); // <-- Danh sách này sẽ được đưa cho Adapter
    private FloatingActionButton fabAddBooking;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_list, container, false);

        recyclerViewBookings = view.findViewById(R.id.recyclerViewBookings);
        progressBar = view.findViewById(R.id.progressBar);
        fabAddBooking = view.findViewById(R.id.fabAddBooking);

        setupRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchBookings(); // Tải dữ liệu khi Fragment được tạo

        fabAddBooking.setOnClickListener(v -> {
            // Xử lý khi nhấn nút "Create"
            // Vẫn cần API riêng cho Admin Create
            Toast.makeText(getContext(), "Chức năng này cần API Create của Admin!", Toast.LENGTH_SHORT).show();
            // (Khi có API, bạn sẽ mở BookingCreateFragment hoặc Activity từ đây)
        });
    }

    private void setupRecyclerView() {
        // Khởi tạo Adapter của bạn, truyền danh sách (đang rỗng) vào
        adapter = new BookingAdapter(bookingList);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBookings.setAdapter(adapter);
    }

    private void fetchBookings() {
        progressBar.setVisibility(View.VISIBLE);
        BookingApi bookingApi = BookingRepository.getBookingService();

        Call<List<Booking>> call = bookingApi.getAllBookings();
        call.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {

                    // Cập nhật danh sách
                    bookingList.clear(); // Xóa dữ liệu cũ
                    bookingList.addAll(response.body()); // Thêm dữ liệu mới
                    adapter.notifyDataSetChanged(); // Báo cho Adapter biết dữ liệu đã thay đổi

                    Log.d(TAG, "Tải thành công " + bookingList.size() + " bookings.");
                } else {
                    Log.e(TAG, "Tải thất bại: " + response.message());
                    Toast.makeText(getContext(), "Tải danh sách thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Lỗi mạng: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // KHÔNG CẦN HÀM onBookingClick nữa, vì Adapter của bạn tự xử lý hết rồi.
}