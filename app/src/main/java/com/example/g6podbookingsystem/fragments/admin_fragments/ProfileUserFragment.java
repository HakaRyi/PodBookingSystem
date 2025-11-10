package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.activities.LoginActivity;
import com.example.g6podbookingsystem.models.Account;
import com.example.g6podbookingsystem.utils.SharedPrefManager;
import com.google.android.material.button.MaterialButton;

public class ProfileUserFragment extends Fragment {
    private SharedPrefManager pref;
    private TextView tvUserName, tvUserEmail;
    private ImageView imgAvatar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        pref = new SharedPrefManager(requireContext());

        // Ánh xạ view
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        MaterialButton btnEdit = view.findViewById(R.id.btnEditProfile);
        MaterialButton btnLogout = view.findViewById(R.id.btnLogout);

        // LOAD USER AN TOÀN
        Account user = pref.getUser();
        if (user != null) {
            tvUserName.setText(user.name != null && !user.name.isEmpty() ? user.name : "Người dùng POD");
            tvUserEmail.setText(user.email != null ? user.email : "email@pod.com");

            // LOAD AVATAR THẬT QUA GLIDE
            if (user.avatarUrl != null && !user.avatarUrl.isEmpty()) {
                Glide.with(this)
                        .load(user.avatarUrl)
                        .placeholder(R.drawable.ic_user_placeholder)
                        .error(R.drawable.ic_user_placeholder)
                        .circleCrop()
                        .into(imgAvatar);
            } else {
                imgAvatar.setImageResource(R.drawable.ic_user_placeholder);
            }
        } else {
            Toast.makeText(getContext(), "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
        }

        // SỰ KIỆN
        btnEdit.setOnClickListener(v ->
                Toast.makeText(getContext(), "Mở Edit Profile", Toast.LENGTH_SHORT).show()
        );

        view.findViewById(R.id.itemBookingHistory).setOnClickListener(v ->
                Toast.makeText(getContext(), "Mở lịch sử đặt phòng", Toast.LENGTH_SHORT).show()
        );

        view.findViewById(R.id.itemSettings).setOnClickListener(v ->
                Toast.makeText(getContext(), "Mở cài đặt", Toast.LENGTH_SHORT).show()
        );

        btnLogout.setOnClickListener(v -> {
            pref.logout(); // XÓA SẠCH 3 KEY
            Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finishAffinity();
        });

        return view;
    }
}