package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.activities.LoginActivity;
import com.example.g6podbookingsystem.fragments.admin_fragments.HistoryFragment;
import com.example.g6podbookingsystem.fragments.admin_fragments.HomeFragment;
import com.example.g6podbookingsystem.fragments.admin_fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.g6podbookingsystem.utils.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashBoardFragment extends Fragment {

    private TextView tvAdminName, tvAdminEmail;
    private ImageView imgAdminAvatar;
    private Button btnLogout,
            btnManageAccounts, btnManageRooms, btnManageSlots,
            btnManageBookings, btnManagePayments, btnManageFeedback;
    private RecyclerView recyclerBookings;

    private SharedPrefManager pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dash_board, container, false);

        pref = new SharedPrefManager(requireContext());

        // Bind UI
        tvAdminName = view.findViewById(R.id.tvAdminName);
        tvAdminEmail = view.findViewById(R.id.tvAdminEmail);
        imgAdminAvatar = view.findViewById(R.id.imgAdminAvatar);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnManageAccounts = view.findViewById(R.id.btnManageAccounts);
        btnManageRooms = view.findViewById(R.id.btnManageRooms);
        btnManageSlots = view.findViewById(R.id.btnManageSlots);
        btnManageBookings = view.findViewById(R.id.btnManageBookings);
        btnManagePayments = view.findViewById(R.id.btnManagePayments);
        btnManageFeedback = view.findViewById(R.id.btnManageFeedback);
        recyclerBookings = view.findViewById(R.id.recyclerBookings);

        // Hiển thị thông tin admin
        tvAdminEmail.setText(pref.getEmail() != null ? pref.getEmail() : "admin@pod.com");
        tvAdminName.setText(pref.getName() != null ? pref.getName() : "Administrator");

        // Load avatar (nếu có)
//        Glide.with(this)
//                .load(pref.getAvatarUrl())
//                .placeholder(R.drawable.ic_baseline_person_24)
//                .circleCrop()
//                .into(imgAdminAvatar);

        // Đăng xuất
        btnLogout.setOnClickListener(v -> {
            try {
                FirebaseAuth.getInstance().signOut();
            } catch (Exception ignored) {}
            pref.logout();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });

        // Các chức năng quản lý
        btnManageAccounts.setOnClickListener(v -> showToast("🔍 Xem tất cả tài khoản"));
        btnManageRooms.setOnClickListener(v -> showToast("🏢 Quản lý phòng"));
        btnManageSlots.setOnClickListener(v -> showToast("⏰ Quản lý khung giờ"));
        btnManageBookings.setOnClickListener(v -> showToast("📅 Quản lý đặt phòng"));
        btnManagePayments.setOnClickListener(v -> showToast("💳 Quản lý thanh toán"));
        btnManageFeedback.setOnClickListener(v -> showToast("💬 Quản lý phản hồi"));

        return view;
    }

    private void showToast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
}