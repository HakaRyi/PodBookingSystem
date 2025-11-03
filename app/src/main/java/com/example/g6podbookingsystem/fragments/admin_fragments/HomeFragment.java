package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.adapters.BookingAdapter;
import com.example.g6podbookingsystem.models.Booking;
import com.example.g6podbookingsystem.repositories.BookingRepository;
import com.example.g6podbookingsystem.services.BookingApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchBox;
    private ImageButton btnFilterDate;
    private BookingAdapter adapter;
    private List<Booking> bookingList = new ArrayList<>();
    private Calendar selectedDate = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBookings);
        searchBox = view.findViewById(R.id.editSearchBooking);
        btnFilterDate = view.findViewById(R.id.btnFilterDate);


        adapter = new BookingAdapter(bookingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Chọn ngày
        btnFilterDate.setOnClickListener(v -> showDatePicker());
        // Chạy Hàm
        loadBookings();
        return view;
    }

    private void showDatePicker() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getContext(), (DatePicker view, int y, int m, int d) -> {
            selectedDate.set(y, m, d);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Toast.makeText(getContext(), "Lọc booking ngày: " + sdf.format(selectedDate.getTime()), Toast.LENGTH_SHORT).show();
        }, year, month, day).show();
    }

    private void loadBookings() {
        BookingApi api = BookingRepository.getBookingService();

        api.getAllBookings().enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookingList.clear();
                    for (Booking b : response.body()) {
                        if ("BOOKED".equalsIgnoreCase(b.getStatus()) ||
                                "CHECK-IN".equalsIgnoreCase(b.getStatus())) {
                            bookingList.add(b);
                        }
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không có dữ liệu Booking", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}