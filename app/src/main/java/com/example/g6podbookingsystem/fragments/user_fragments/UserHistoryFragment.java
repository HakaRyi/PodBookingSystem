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
import com.example.g6podbookingsystem.adapters.BookingHistoryUserAdapter;
import com.example.g6podbookingsystem.models.Booking;
import com.example.g6podbookingsystem.models.BookingHistoryDto;
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
    private BookingHistoryUserAdapter adapter;
    private List<BookingHistoryDto> fullList = new ArrayList<>();
    private List<BookingHistoryDto> filteredList = new ArrayList<>();
    private BookingApi api;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_history, container, false);

        rvUserBookingHistory = view.findViewById(R.id.rvUserBookingHistory);
        etUserHistorySearch = view.findViewById(R.id.etUserHistorySearch);

        rvUserBookingHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingHistoryUserAdapter(getContext(), filteredList);
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

        api.getUserBookingHistory2().enqueue(new Callback<List<BookingHistoryDto>>() {
            @Override
            public void onResponse(Call<List<BookingHistoryDto>> call, Response<List<BookingHistoryDto>> response) {
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
            public void onFailure(Call<List<BookingHistoryDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterList(String keyword) {
        filteredList.clear();
        String lowerKeyword = keyword.toLowerCase(Locale.ROOT);

        for (BookingHistoryDto b : fullList) {
            String roomName = "Không xác định";
            if (b.getDetails() != null && !b.getDetails().isEmpty()) {
                roomName = b.getDetails().get(0).getRoomName();
            }

            if (roomName.toLowerCase(Locale.ROOT).contains(lowerKeyword)) {
                filteredList.add(b);
            }
        }
        adapter.updateList(filteredList);
    }
}
