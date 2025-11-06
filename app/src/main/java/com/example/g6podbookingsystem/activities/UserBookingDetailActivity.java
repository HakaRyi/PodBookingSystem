package com.example.g6podbookingsystem.activities;
import android.util.SparseBooleanArray;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.fragments.admin_fragments.BookingUserFragment;
import com.example.g6podbookingsystem.models.Room;
import com.example.g6podbookingsystem.models.Slot;
import com.example.g6podbookingsystem.repositories.BookingDetailRepository;
import com.example.g6podbookingsystem.services.BookingDetailApi;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBookingDetailActivity extends AppCompatActivity {

    private Room room;
    private int bookingId;
    private BookingDetailApi api;

    private List<Slot> allSlots = new ArrayList<>();
    private List<Slot> selectedSlots = new ArrayList<>();

    private TextInputEditText etDate, etStartTime, etEndTime, etHours;
    private MaterialButton btnHourMode, btnDayMode, btnContinue;
    private ListView listViewSlots;
    private LinearLayout layoutSlots;
    private TextView tvRoomName;

    private String bookingType = "HOUR";
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_booking_detail);

        room = (Room) getIntent().getSerializableExtra("room");
        bookingId = getIntent().getIntExtra("bookingId", -1);
        if (room == null || bookingId == -1) {
            finish();
            return;
        }

        api = BookingDetailRepository.getBookingDetailService();
        initViews();
        setupToolbar();
        setupModeButtons();
        setupDatePicker();
        loadAvailableSlots();
    }

    private void initViews() {
        tvRoomName = findViewById(R.id.tvBookingStatus);
        tvRoomName.setText("Đặt phòng: " + room.getName());

        etDate = findViewById(R.id.etBookingDate);
        etStartTime = findViewById(R.id.etStartTime);
        etEndTime = findViewById(R.id.etEndTime);
        etHours = findViewById(R.id.etHours);

        btnHourMode = findViewById(R.id.btnHourMode);
        btnDayMode = findViewById(R.id.btnDayMode);
        btnContinue = findViewById(R.id.btnContinue);

        listViewSlots = findViewById(R.id.listViewSlots);
        layoutSlots = findViewById(R.id.layoutSlots);

        btnContinue.setOnClickListener(v -> goToSummary());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Nhập thông tin đặt phòng");
        }
    }

    private void setupModeButtons() {
        btnHourMode.setOnClickListener(v -> setMode("HOUR"));
        btnDayMode.setOnClickListener(v -> setMode("DAY"));
        setMode("HOUR");
    }

    private void setMode(String mode) {
        bookingType = mode;
        int active = getColor(R.color.primary);
        int inactive = getColor(R.color.white);

        btnHourMode.setBackgroundTintList(ColorStateList.valueOf(mode.equals("HOUR") ? active : inactive));
        btnDayMode.setBackgroundTintList(ColorStateList.valueOf(mode.equals("DAY") ? active : inactive));

        layoutSlots.setVisibility(mode.equals("HOUR") ? View.VISIBLE : View.GONE);
        etStartTime.setEnabled(false);
        etEndTime.setEnabled(false);

        if (mode.equals("DAY")) {
            // SET GIỜ TRƯỚC
            etStartTime.setText("06:00:00");
            etEndTime.setText("22:00:00");
            etHours.setText("16");
            selectedSlots.clear();

        } else {
            selectedSlots.clear();
            updateTimeFromSlots();
        }
    }

    private void setupDatePicker() {
        etDate.setOnClickListener(v -> showDatePicker());
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        etDate.setText(today);
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, y, m, d) -> {
            String date = String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y);
            etDate.setText(date);
            selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", y, m + 1, d);
            loadAvailableSlots();
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadAvailableSlots() {
        Call<List<Slot>> call = api.getAvailableSlots(room.getRoomId(), selectedDate);
        call.enqueue(new Callback<List<Slot>>() {
            @Override
            public void onResponse(Call<List<Slot>> call, Response<List<Slot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allSlots = response.body();
                    updateSlotListView();
                }
            }

            @Override
            public void onFailure(Call<List<Slot>> call, Throwable t) {
                Toast.makeText(UserBookingDetailActivity.this, "Lỗi tải slot", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSlotListView() {
        List<String> slotNames = allSlots.stream()
                .map(s -> s.getDescription() + " (Slot " + s.getSlotId() + ")")
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, slotNames);
        listViewSlots.setAdapter(adapter);

        // XÓA CHECK CŨ
        listViewSlots.clearChoices();
        adapter.notifyDataSetChanged();

        listViewSlots.setOnItemClickListener((parent, view, position, id) -> {
            selectedSlots.clear();

            SparseBooleanArray checked = listViewSlots.getCheckedItemPositions();
            for (int i = 0; i < checked.size(); i++) {
                if (checked.valueAt(i)) {
                    selectedSlots.add(allSlots.get(checked.keyAt(i)));
                }
            }

            selectedSlots.sort((a, b) -> Integer.compare(a.getSlotId(), b.getSlotId()));

            if (!isConsecutive(selectedSlots)) {
                Toast.makeText(this, "Vui lòng chọn các slot liên tiếp!", Toast.LENGTH_LONG).show();
                listViewSlots.setItemChecked(position, false);
                selectedSlots.removeIf(s -> s.getSlotId() == allSlots.get(position).getSlotId());
                updateTimeFromSlots();
            } else {
                updateTimeFromSlots();
            }
        });
    }

    private boolean isConsecutive(List<Slot> slots) {
        if (slots.isEmpty()) return true;
        for (int i = 1; i < slots.size(); i++) {
            if (slots.get(i).getSlotId() != slots.get(i - 1).getSlotId() + 1) {
                return false;
            }
        }
        return true;
    }

    private void updateTimeFromSlots() {
        if (selectedSlots.isEmpty()) {
            etStartTime.setText("");
            etEndTime.setText("");
            etHours.setText("0");
            return;
        }

        try {
            Slot first = selectedSlots.get(0);
            Slot last = selectedSlots.get(selectedSlots.size() - 1);

            // "07:00:00-08:00:00" → ["07:00:00", "08:00:00"]
            String startTime = first.getDescription().split("-")[0].trim();  // "07:00:00"
            String endTime = last.getDescription().split("-")[1].trim();     // "09:00:00"

            // LẤY GIỜ TỪ "07:00:00" → 7
            int startHour = Integer.parseInt(startTime.split(":")[0]);
            int endHour = Integer.parseInt(endTime.split(":")[0]);

            // HIỂN THỊ ĐÚNG ĐỊNH DẠNG: 07:00:00
            etStartTime.setText(String.format(Locale.getDefault(), "%02d:00:00", startHour));
            etEndTime.setText(String.format(Locale.getDefault(), "%02d:00:00", endHour));
            etHours.setText(String.valueOf(selectedSlots.size()));

        } catch (Exception e) {
            Toast.makeText(this, "Lỗi định dạng slot", Toast.LENGTH_SHORT).show();
            etStartTime.setText("");
            etEndTime.setText("");
            etHours.setText("0");
        }
    }

    private void goToSummary() {
        if (bookingType.equals("HOUR") && selectedSlots.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 slot", Toast.LENGTH_SHORT).show();
            return;
        }

        // GỌI API TẠO DETAIL TRƯỚC
        createBookingDetail();
    }

    private void createBookingDetail() {
        BookingDetailApi.CreateDetailRequest request = new BookingDetailApi.CreateDetailRequest();
        request.startTime = etStartTime.getText().toString();
        request.endTime = etEndTime.getText().toString();
        request.bookingType = bookingType;

        if (bookingType.equals("HOUR")) {
            request.slots = new ArrayList<>();
            for (Slot s : selectedSlots) {
                BookingDetailApi.CreateDetailRequest.SlotSelection sel = new BookingDetailApi.CreateDetailRequest.SlotSelection();
                sel.slotId = s.getSlotId();
                sel.bookingDate = selectedDate;
                request.slots.add(sel);
            }
        }

        Call<BookingDetailApi.CreateDetailResponse> call = api.createDetail(bookingId, room.getRoomId(), request);
        call.enqueue(new Callback<BookingDetailApi.CreateDetailResponse>() {
            @Override
            public void onResponse(Call<BookingDetailApi.CreateDetailResponse> call, Response<BookingDetailApi.CreateDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // CHUYỂN SANG TRANG TỔNG HỢP
                    Intent intent = new Intent(UserBookingDetailActivity.this, BookingSummaryActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UserBookingDetailActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingDetailApi.CreateDetailResponse> call, Throwable t) {
                Toast.makeText(UserBookingDetailActivity.this, "Lỗi mạng", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}