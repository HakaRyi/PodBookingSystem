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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchBox;
    private ImageButton btnFilterDate;
    private BookingAdapter adapter;
    private List<Booking> bookingList;
    private Calendar selectedDate = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBookings);
        searchBox = view.findViewById(R.id.editSearchBooking);
        btnFilterDate = view.findViewById(R.id.btnFilterDate);

        bookingList = getMockBookings();
        adapter = new BookingAdapter(bookingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Chọn ngày
        btnFilterDate.setOnClickListener(v -> showDatePicker());

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

    private List<Booking> getMockBookings() {
        List<Booking> list = new ArrayList<>();
        list.add(new Booking(1, "Nguyễn Văn A", "2025-10-31", 150000, "Pending"));
        list.add(new Booking(2, "Trần Thị B", "2025-10-31", 200000, "CheckedIn"));
        list.add(new Booking(3, "Lê Minh C", "2025-10-30", 180000, "CheckedOut"));
        return list;
    }
}