package com.example.g6podbookingsystem.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.fragments.admin_fragments.BookingUserFragment;
import com.example.g6podbookingsystem.fragments.admin_fragments.HomeUserFragment;
import com.example.g6podbookingsystem.fragments.admin_fragments.ProfileUserFragment;
import com.example.g6podbookingsystem.fragments.admin_fragments.HomeUserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);
        // Kiểm tra Intent có yêu cầu mở fragment nào không
        if (savedInstanceState == null) {
            String openFragment = getIntent().getStringExtra("open_fragment");

            if ("bookings".equals(openFragment)) {
                bottomNav.setSelectedItemId(R.id.nav_bookings);
            } else if ("home".equals(openFragment)) {
                bottomNav.setSelectedItemId(R.id.nav_home);
            } else {
                // Mặc định
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeUserFragment())
                        .commit();
            }
        }


    }

    private final BottomNavigationView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeUserFragment();
                } else if (itemId == R.id.nav_bookings) {
                    selectedFragment = new BookingUserFragment();
                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = new ProfileUserFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }
                return true;
            };
}