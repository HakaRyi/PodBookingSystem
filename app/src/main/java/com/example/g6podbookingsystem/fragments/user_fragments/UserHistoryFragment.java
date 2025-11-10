package com.example.g6podbookingsystem.fragments.user_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.adapters.BookingHistoryAdapter;
import com.example.g6podbookingsystem.models.Booking;
import com.example.g6podbookingsystem.repositories.BookingRepository;
import com.example.g6podbookingsystem.services.BookingApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHistoryFragment extends Fragment {

    private RecyclerView rvUserBookingHistory;
    private EditText etUserHistorySearch;
    private BookingHistoryAdapter adapter;
    private List<Booking> fullList = new ArrayList<>();
    private List<Booking> filteredList = new ArrayList<>();
    private BookingApi api;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_history, container, false);

        rvUserBookingHistory = view.findViewById(R.id.rvUserBookingHistory);
        etUserHistorySearch = view.findViewById(R.id.etUserHistorySearch);

        rvUserBookingHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingHistoryAdapter(getContext(), filteredList);
        rvUserBookingHistory.setAdapter(adapter);

        api = BookingRepository.getBookingService();
        fetchUserHistory();

        etUserHistorySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void fetchUserHistory() {
        SharedPreferences prefs = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(getContext(), "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }

        api.getUserBookingHistory(userId).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullList.clear();
                    fullList.addAll(response.body());
                    filteredList.clear();
                    filteredList.addAll(fullList);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không có lịch sử đặt phòng.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterList(String keyword) {
        filteredList.clear();
        for (Booking b : fullList) {
            if (b.getBookingDetails().get(0).getRoom().getName().toLowerCase(Locale.ROOT)
                    .contains(keyword.toLowerCase(Locale.ROOT))) {
                filteredList.add(b);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
