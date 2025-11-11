package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.adapters.BookingHistoryAdapter;
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

public class HistoryFragment extends Fragment {
    private RecyclerView rvBookingHistory;
    private BookingHistoryAdapter adapter;
    private List<Booking> fullList = new ArrayList<>();
    private List<Booking> filteredList = new ArrayList<>();

    private TextWatcher textWatcher;

    private BookingApi api;
    private Button btnClearFilter;
    private EditText etSearch;
    private TextView tvDateFilter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

//        rvBookingHistory = view.findViewById(R.id.rvBookingHistory);
//        etSearch = view.findViewById(R.id.etSearch);
//        tvDateFilter = view.findViewById(R.id.tvDateFilter);
//        btnClearFilter = view.findViewById(R.id.btnClearFilter);
//
//        rvBookingHistory.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        api = BookingRepository.getBookingService();
//
//        adapter = new BookingHistoryAdapter(getContext(), filteredList);
//        rvBookingHistory.setAdapter(adapter);
//
//        fetchBookings();
//        setupSearch();
//        setupDateFilter();
//
//        btnClearFilter.setOnClickListener(v -> {
//            etSearch.removeTextChangedListener(textWatcher);
//
//            etSearch.setText("");
//            tvDateFilter.setText("Chọn ngày");
//
//            filteredList.clear();
//            filteredList.addAll(fullList);
//            adapter.updateList(filteredList);
//
//            etSearch.addTextChangedListener(textWatcher);
//        });
//
//        return view;
//    }
//
//    private void fetchBookings() {
//        SharedPreferences prefs = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
//        int userId = prefs.getInt("userId", -1);
//        String role = prefs.getString("role", "User"); // mặc định là User
//
//        if (role.equalsIgnoreCase("Admin")) {
//            api.getAllBookings().enqueue(new Callback<List<Booking>>() {
//                @Override
//                public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        fullList.clear();
//                        fullList.addAll(response.body());
//                        filteredList.clear();
//                        filteredList.addAll(fullList);
//                        adapter.notifyDataSetChanged();
//                    } else {
//                        Toast.makeText(getContext(), "Không có dữ liệu đặt chỗ.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<Booking>> call, Throwable t) {
//                    Toast.makeText(getContext(), "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            // 👤 User thường → chỉ lấy lịch sử của riêng họ
//            if (userId == -1) {
//                Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            api.getUserBookingHistory(userId).enqueue(new Callback<List<Booking>>() {
//                @Override
//                public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        fullList.clear();
//                        fullList.addAll(response.body());
//                        filteredList.clear();
//                        filteredList.addAll(fullList);
//                        adapter.notifyDataSetChanged();
//                    } else {
//                        Toast.makeText(getContext(), "Không có lịch sử đặt chỗ.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<Booking>> call, Throwable t) {
//                    Toast.makeText(getContext(), "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    private void setupSearch() {
//        textWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                filterList();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        };
//        etSearch.addTextChangedListener(textWatcher);
//    }
//
//    private void setupDateFilter() {
//        tvDateFilter.setOnClickListener(v -> {
//            Calendar calendar = Calendar.getInstance();
//            DatePickerDialog datePicker = new DatePickerDialog(
//                    getContext(),
//                    (DatePicker view, int year, int month, int dayOfMonth) -> {
//                        calendar.set(year, month, dayOfMonth);
//                        String dateSelected = sdf.format(calendar.getTime());
//                        tvDateFilter.setText("Ngày: " + dateSelected);
//                        filterList();
//                    },
//                    calendar.get(Calendar.YEAR),
//                    calendar.get(Calendar.MONTH),
//                    calendar.get(Calendar.DAY_OF_MONTH)
//            );
//            datePicker.show();
//        });
//    }
//
//    private void filterList() {
//        String searchText = etSearch.getText().toString().toLowerCase(Locale.ROOT).trim();
//        String selectedDate = tvDateFilter.getText().toString().contains("Ngày:")
//                ? tvDateFilter.getText().toString().replace("Ngày: ", "")
//                : "";
//
//        filteredList.clear();
//        for (Booking b : fullList) {
//            boolean matchName = b.getUser().getName().toLowerCase(Locale.ROOT).contains(searchText)
//                    || b.getBookingDetails().get(0).getRoom().getName().toLowerCase(Locale.ROOT).contains(searchText);
//            String bookingDateOnly = b.getBookingDate().split("T")[0];
//            boolean matchDate = selectedDate.isEmpty() || bookingDateOnly.equals(selectedDate);
//
//            if (matchName && matchDate) filteredList.add(b);
//        }
//        adapter.notifyDataSetChanged();
//    }

        rvBookingHistory = view.findViewById(R.id.rvBookingHistory);
        etSearch = view.findViewById(R.id.etSearch);
        tvDateFilter = view.findViewById(R.id.tvDateFilter);
        btnClearFilter = view.findViewById(R.id.btnClearFilter);

        rvBookingHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        api = BookingRepository.getBookingService();

        adapter = new BookingHistoryAdapter(getContext(), filteredList);
        rvBookingHistory.setAdapter(adapter);

        fetchBookings();
        setupSearch();
        setupDateFilter();

        btnClearFilter.setOnClickListener(v -> {
            etSearch.removeTextChangedListener(textWatcher);

            etSearch.setText("");
            tvDateFilter.setText("Chọn ngày");

            filteredList.clear();
            filteredList.addAll(fullList);
            adapter.updateList(filteredList);

            etSearch.addTextChangedListener(textWatcher);
        });

        return view;
    }

    private void fetchBookings() {
        api.getAllBookings().enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullList.clear();
                    fullList.addAll(response.body());
                    filteredList.clear();
                    filteredList.addAll(fullList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setupSearch() {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        etSearch.addTextChangedListener(textWatcher);
    }

    private void setupDateFilter() {
        tvDateFilter.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(
                    getContext(),
                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        String dateSelected = sdf.format(calendar.getTime());
                        tvDateFilter.setText("Ngày: " + dateSelected);
                        filterList();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.show();
        });
    }

    private void filterList() {
        String searchText = etSearch.getText().toString().toLowerCase(Locale.ROOT).trim();
        String selectedDate = tvDateFilter.getText().toString().contains("Ngày:")
                ? tvDateFilter.getText().toString().replace("Ngày: ", "")
                : "";

        filteredList.clear();
        for (Booking b : fullList) {
            boolean matchName = b.getUser().getName().toLowerCase(Locale.ROOT).contains(searchText)
                    || b.getBookingDetails().get(0).getRoom().getName().toLowerCase(Locale.ROOT).contains(searchText);
            String bookingDateOnly = b.getBookingDate().split("T")[0];
            boolean matchDate = selectedDate.isEmpty() || bookingDateOnly.equals(selectedDate);

            if (matchName && matchDate) filteredList.add(b);
        }
        adapter.notifyDataSetChanged();
    }
}