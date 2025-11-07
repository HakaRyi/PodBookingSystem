package com.example.g6podbookingsystem.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri data = getIntent().getData();
        String status = data != null ? data.getQueryParameter("status") : "unknown";

        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if ("success".equals(status)) {
            Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
            intent.putExtra("open_fragment", "bookings");
        } else {
//            Toast.makeText(this, "Thanh toán thất bại hoặc bị hủy.", Toast.LENGTH_LONG).show();
            intent.putExtra("open_fragment", "home");
        }

        startActivity(intent);
        finish();
    }
}
