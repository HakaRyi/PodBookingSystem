package com.example.g6podbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.utils.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;

public class AdminBookingManageActivity extends AppCompatActivity {

    private TextView tvAdminName, tvAdminEmail;
    private ImageView imgAdminAvatar;
    private Button btnLogout,
            btnManageAccounts, btnManageRooms, btnManageSlots,
            btnManageBookings, btnManagePayments, btnManageFeedback;

    private SharedPrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_booking_manage);

        // Initialize Shared Preferences
        pref = new SharedPrefManager(this);

        // Bind UI elements
        tvAdminName = findViewById(R.id.tvAdminName);
        tvAdminEmail = findViewById(R.id.tvAdminEmail);
        imgAdminAvatar = findViewById(R.id.imgAdminAvatar);
        btnLogout = findViewById(R.id.btnLogout);

        btnManageAccounts = findViewById(R.id.btnManageAccounts);
        btnManageRooms = findViewById(R.id.btnManageRooms);
        btnManageSlots = findViewById(R.id.btnManageSlots);
        btnManageBookings = findViewById(R.id.btnManageBookings);
        btnManagePayments = findViewById(R.id.btnManagePayments);
        btnManageFeedback = findViewById(R.id.btnManageFeedback);

        // Load admin info
        tvAdminEmail.setText(pref.getEmail() != null ? pref.getEmail() : "admin@pod.com");
        tvAdminName.setText(pref.getName() != null ? pref.getName() : "Administrator");

        // Logout button
        btnLogout.setOnClickListener(v -> {
            try {
                FirebaseAuth.getInstance().signOut();
            } catch (Exception ignored) {
            }
            pref.logout();
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Button click listeners (for demo, show toast)
        btnManageAccounts.setOnClickListener(v -> showToast("🔍 Xem tất cả tài khoản (Account)"));
        btnManageRooms.setOnClickListener(v -> showToast("🏢 Quản lý phòng (Room)"));
        btnManageSlots.setOnClickListener(v -> showToast("⏰ Quản lý khung giờ (Slot)"));
        btnManageBookings.setOnClickListener(v -> showToast("📅 Quản lý đặt phòng (Booking)"));
        btnManagePayments.setOnClickListener(v -> showToast("💳 Quản lý thanh toán (Payment)"));
        btnManageFeedback.setOnClickListener(v -> showToast("💬 Quản lý phản hồi (Feedback)"));
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
