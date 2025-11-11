package com.example.g6podbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.Booking;
import com.example.g6podbookingsystem.models.Room;
import com.example.g6podbookingsystem.repositories.BookingRepository;
import com.example.g6podbookingsystem.services.BookingApi;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomDetailActivity extends AppCompatActivity {

    private MaterialButton btnBookBottom;
    private BookingApi bookingApi;
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        room = (Room) getIntent().getParcelableExtra("room");
        if (room == null) {
            finish();
            return;
        }

        // Khởi tạo API
        bookingApi = BookingRepository.getBookingService();

        // Bind UI
        bindUI();
        setupToolbar();
        setupBookButton();
    }

    private void bindUI() {
        ImageView img = findViewById(R.id.imgRoomDetail);
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvType = findViewById(R.id.tvDetailType);
        TextView tvCapacity = findViewById(R.id.tvDetailCapacity);
        TextView tvPrice = findViewById(R.id.tvDetailPrice);
        TextView tvPriceDay = findViewById(R.id.tvDetailPriceDay);
        btnBookBottom = findViewById(R.id.btnBookBottom);

        tvName.setText(room.getName());
        tvType.setText(room.getType() != null ? room.getType().getName().trim() : "Phòng");
        tvCapacity.setText("Sức chứa: " + room.getCapacity() + " người");
        tvPrice.setText(formatPrice(room.getPrice()) + "/giờ");
        tvPriceDay.setText(formatPrice(room.getPriceDay()) + "/ngày");

        if (room.getImgUrl() != null && !room.getImgUrl().isEmpty()) {
            Glide.with(this).load(room.getImgUrl()).into(img);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(room.getName());
    }

    private void setupBookButton() {
        btnBookBottom.setOnClickListener(v -> {
            btnBookBottom.setEnabled(false);
            checkPendingAndProceed();
        });
    }

    private void checkPendingAndProceed() {
        Call<BookingApi.PendingResponse> call = bookingApi.checkPendingBooking();
        call.enqueue(new Callback<BookingApi.PendingResponse>() {
            @Override
            public void onResponse(Call<BookingApi.PendingResponse> call, Response<BookingApi.PendingResponse> response) {
                btnBookBottom.setEnabled(true);
                if (!response.isSuccessful() || response.body() == null) {
                    createNewBooking();
                    return;
                }

                if (response.body().bookingId != null) {
                    // Có booking pending → chuyển sang detail
                    goToBookingDetail(response.body().bookingId);
                } else {
                    createNewBooking();
                }
            }

            @Override
            public void onFailure(Call<BookingApi.PendingResponse> call, Throwable t) {
                btnBookBottom.setEnabled(true);
                createNewBooking(); // fallback
            }
        });
    }

    private void createNewBooking() {
        Call<BookingApi.CreateBookingResponse> call = bookingApi.createBooking();
        call.enqueue(new Callback<BookingApi.CreateBookingResponse>() {
            @Override
            public void onResponse(Call<BookingApi.CreateBookingResponse> call, Response<BookingApi.CreateBookingResponse> response) {
                btnBookBottom.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    goToBookingDetail(response.body().bookingId);
                } else {
                    Toast.makeText(RoomDetailActivity.this, "Tạo thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingApi.CreateBookingResponse> call, Throwable t) {
                btnBookBottom.setEnabled(true);
                Toast.makeText(RoomDetailActivity.this, "Lỗi mạng", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goToBookingDetail(int bookingId) {
        Intent intent = new Intent(RoomDetailActivity.this, UserBookingDetailActivity.class);
        intent.putExtra("room", room);
        intent.putExtra("bookingId", bookingId);
        startActivity(intent);
    }

    private String formatPrice(Double price) {
        if (price == null) return "0đ";
        NumberFormat f = NumberFormat.getInstance(new Locale("vi", "VN"));
        return f.format(price);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private void getFirebaseToken(TokenCallback callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            callback.onResult(null);
            return;
        }

        user.getIdToken(true).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String token = task.getResult().getToken();
                callback.onResult(token);
            } else {
                callback.onResult(null);
            }
        });
    }

    interface TokenCallback {
        void onResult(String token);
    }
}
