package com.example.g6podbookingsystem.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.Room;
import com.google.android.material.button.MaterialButton;
import java.text.NumberFormat;
import java.util.Locale;

public class RoomDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        Room room = (Room) getIntent().getSerializableExtra("room");
        if (room == null) {
            finish();
            return;
        }

        // Toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(room.getName());

        // Bind
        ImageView img = findViewById(R.id.imgRoomDetail);
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvType = findViewById(R.id.tvDetailType);
        TextView tvCapacity = findViewById(R.id.tvDetailCapacity);
        TextView tvPrice = findViewById(R.id.tvDetailPrice);
        TextView tvPriceDay = findViewById(R.id.tvDetailPriceDay);

        tvName.setText(room.getName());
        tvType.setText(room.getType() != null ? room.getType().getName().trim() : "Phòng");
        tvCapacity.setText("Sức chứa: " + room.getCapacity() + " người");
        tvPrice.setText(formatPrice(room.getPrice()) + "/giờ");
        tvPriceDay.setText(formatPrice(room.getPriceDay()) + "/ngày");

        if (room.getImgUrl() != null && !room.getImgUrl().isEmpty()) {
            Glide.with(this).load(room.getImgUrl()).into(img);
        }
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
}