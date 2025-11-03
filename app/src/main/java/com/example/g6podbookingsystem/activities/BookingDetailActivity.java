package com.example.g6podbookingsystem.activities;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.Booking;
import com.example.g6podbookingsystem.models.BookingDetail;
import com.example.g6podbookingsystem.repositories.BookingRepository;
import com.example.g6podbookingsystem.services.BookingApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailActivity extends AppCompatActivity {

    private TextView tvRoomName, tvCustomerName, tvTime, tvStatus, tvTotalPrice;
    private Button btnCancelBooking;
    private Booking currentBooking;
    private BookingApi bookingApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_detail);

        tvRoomName = findViewById(R.id.tvRoomName);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvTime = findViewById(R.id.tvTime);
        tvStatus = findViewById(R.id.tvStatus);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCancelBooking = findViewById(R.id.btnCancelBooking);

        bookingApi = BookingRepository.getBookingService();

        int bookingId = getIntent().getIntExtra("bookingId", -1);
        if (bookingId != -1) {
            loadBookingDetail(bookingId);
        }
    }

    private void loadBookingDetail(int bookingId) {
        bookingApi.getBookingById(bookingId).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentBooking = response.body();
                    displayBookingDetails(currentBooking);
                } else {
                    Toast.makeText(BookingDetailActivity.this, "Không tìm thấy chi tiết booking", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Toast.makeText(BookingDetailActivity.this, "Lỗi tải dữ liệu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBookingDetails(Booking b) {
        tvRoomName.setText("Phòng: " + (b.getBookingDetails().get(0).getRoom() != null ? b.getBookingDetails().get(0).getRoom().getName() : "Không xác định"));
        tvCustomerName.setText("Khách hàng: " + (b.getUser() != null ? b.getUser().getName() : "Ẩn danh"));
        tvStatus.setText("Trạng thái: " + b.getStatus());

        if (b.getBookingDetails() != null && !b.getBookingDetails().isEmpty()) {
            List<BookingDetail> details = b.getBookingDetails();
            BookingDetail first = details.get(0);
            BookingDetail last = details.get(details.size() - 1);
            tvTime.setText("Thời gian: " + first.getStartTime() + " - " + last.getEndTime());
        } else {
            tvTime.setText("Thời gian: N/A");
        }

        tvTotalPrice.setText("Tổng tiền: " + b.getTotal() + " VND");

        // Nếu booking đã hủy hoặc check-out thì ẩn nút hủy
        if ("CANCELLED".equalsIgnoreCase(b.getStatus()) || "CHECK-OUT".equalsIgnoreCase(b.getStatus())) {
            btnCancelBooking.setEnabled(false);
            btnCancelBooking.setAlpha(0.5f);
        }

        btnCancelBooking.setOnClickListener(v -> showCancelDialog(b));
    }

    private void showCancelDialog(Booking booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập lý do hủy");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setHint("Nhập nguyên nhân hủy đặt phòng...");
        builder.setView(input);

        builder.setPositiveButton("Xác nhận hủy", (dialog, which) -> {
            String reason = input.getText().toString().trim();
            if (reason.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập lý do hủy", Toast.LENGTH_SHORT).show();
                return;
            }

            bookingApi.cancelBooking(booking.getBookingId(), reason).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful() && response.body() != null && response.body() > 0) {
                        Toast.makeText(BookingDetailActivity.this, "✅ Hủy booking thành công", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại màn danh sách
                    } else {
                        Toast.makeText(BookingDetailActivity.this, "Hủy thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Toast.makeText(BookingDetailActivity.this, "Lỗi server", Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}