package com.example.g6podbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.adapters.BookingDetailSummaryAdapter;
import com.example.g6podbookingsystem.models.Booking;
import com.example.g6podbookingsystem.repositories.BookingDetailRepository;
import com.example.g6podbookingsystem.repositories.BookingRepository;
import com.example.g6podbookingsystem.services.BookingApi;
import com.example.g6podbookingsystem.services.BookingDetailApi;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingSummaryActivity extends AppCompatActivity {

    private TextView tvStatus, tvTotal;
    private Button btnAddRoom,btnManagePayments;
    private RecyclerView rvDetails;
    private BookingApi bookingApi;
    private BookingDetailApi detailApi;
    private BookingDetailSummaryAdapter adapter;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initApi();
        loadBookingFromApi();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvStatus = findViewById(R.id.tvStatus);
        tvTotal = findViewById(R.id.tvTotal);
        btnAddRoom = findViewById(R.id.btnAddRoom);
        btnManagePayments = findViewById(R.id.btnManagePayments);
        rvDetails = findViewById(R.id.rvBookingDetails);
        rvDetails.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        btnAddRoom.setOnClickListener(v -> {
            startActivity(new Intent(this, FindRoomActivity.class));
        });
        btnManagePayments.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến thanh toán...", Toast.LENGTH_SHORT).show();
            //Chuyển đến Payment Activity
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Back về Dashboard → Mở lại Home Fragment
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("open_fragment", "home");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initApi() {
        bookingApi = BookingRepository.getBookingService();
        detailApi = BookingDetailRepository.getBookingDetailService();
    }

    private void loadBookingFromApi() {
        Call<BookingApi.PendingBookingResponse> call = bookingApi.getPendingBookingFull();
        call.enqueue(new Callback<BookingApi.PendingBookingResponse>() {
            @Override
            public void onResponse(Call<BookingApi.PendingBookingResponse> call, Response<BookingApi.PendingBookingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookingApi.PendingBookingResponse res = response.body();

                    if (res.getBooking() != null) {
                        // CÓ ĐƠN → HIỂN THỊ BÌNH THƯỜNG
                        Booking booking = res.getBooking();

                        adapter = new BookingDetailSummaryAdapter(
                                BookingSummaryActivity.this,
                                booking.getBookingDetails(),
                                booking.getRoomSlots()
                        );
                        rvDetails.setAdapter(adapter);

                        displayBooking(booking);
                    } else {
                        // KHÔNG CÓ ĐƠN → HIỆN TRẠNG THÁI + TỔNG = 0
                        tvStatus.setText("Trạng thái: Chưa có đơn nào");
                        tvTotal.setText("Tổng tiền: 0đ");

                        // XÓA DỮ LIỆU CŨ
                        if (adapter != null) {
                            adapter = new BookingDetailSummaryAdapter(
                                    BookingSummaryActivity.this,
                                    Collections.emptyList(),
                                    Collections.emptyList()
                            );
                            rvDetails.setAdapter(adapter);
                        }

                        // HIỆN THÔNG BÁO
                        Toast.makeText(BookingSummaryActivity.this, res.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    tvStatus.setText("Trạng thái: Lỗi kết nối");
                    tvTotal.setText("Tổng tiền: 0đ");
                    Toast.makeText(BookingSummaryActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BookingApi.PendingBookingResponse> call, Throwable t) {
                tvStatus.setText("Trạng thái: Lỗi mạng");
                tvTotal.setText("Tổng tiền: 0đ");
                Toast.makeText(BookingSummaryActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayBooking(Booking booking) {
        // 1. Trạng thái + Tổng tiền
        tvStatus.setText("Trạng thái: " + getStatusText(booking.getStatus()));
        tvTotal.setText("Tổng tiền: " + formatPrice(booking.getTotal()));

        // 2. Danh sách chi tiết
        if (booking.getBookingDetails() == null || booking.getBookingDetails().isEmpty()) {
            Toast.makeText(this, "Không có phòng nào được đặt", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter = new BookingDetailSummaryAdapter(this, booking.getBookingDetails(), booking.getRoomSlots());
        adapter.setOnItemDeleteListener((detailId, position) -> showDeleteConfirmDialog(detailId, position));
        rvDetails.setAdapter(adapter);
    }
    private String getStatusText(String status) {
        if (status == null) return "Không rõ";
        switch (status) {
            case "PENDING":
                return "Chưa thanh toán";
            case "BOOKED":
                return "Đã thanh toán";
            case "CHECK-IN":
                return "Đã nhận phòng";
            case "CHECK-OUT":
                return "Đã trả phòng";
            case "CANCELLED":
                return "Đã hủy";
            default:
                return status;
        }
    }
    private void showDeleteConfirmDialog(int detailId, int position) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Xóa phòng khỏi đơn?")
                .setMessage("Bạn có chắc chắn muốn xóa phòng này?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, which) -> deleteDetail(detailId, position))
                .show();
    }

    private void deleteDetail(int detailId, int position) {
        Call<Void> call = detailApi.deleteDetail(detailId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BookingSummaryActivity.this, "Đã xóa phòng!", Toast.LENGTH_SHORT).show();
                    loadBookingFromApi(); // TẢI LẠI TOÀN BỘ
                } else {
                    Toast.makeText(BookingSummaryActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BookingSummaryActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String formatPrice(double price) {
        NumberFormat f = NumberFormat.getInstance(new Locale("vi", "VN"));
        return f.format(price) + "đ";
    }
}