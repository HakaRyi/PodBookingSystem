package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.activities.LoginActivity;
import com.example.g6podbookingsystem.models.Account;
import com.example.g6podbookingsystem.models.Booking;
import com.example.g6podbookingsystem.models.Room;
import com.example.g6podbookingsystem.services.AccountApi;
import com.example.g6podbookingsystem.services.ApiClient;
import com.example.g6podbookingsystem.services.BookingApi;
import com.example.g6podbookingsystem.services.RoomApi;
import com.example.g6podbookingsystem.utils.SharedPrefManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashBoardFragment extends Fragment {

    private TextView tvAdminName, tvAdminEmail;
    private ImageView imgAdminAvatar;
    private Button btnLogout,
            btnManageAccounts, btnManageRooms,
            btnManagePayments, btnManageBooking;

    //Biểu Đồ nha MN
    private BarChart barChart;
    private Integer totalAccounts = null;
    private Integer totalRooms = null;
    private Integer totalBookings = null;

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
        btnManagePayments = view.findViewById(R.id.btnManagePayments);
        btnManageBooking = view.findViewById(R.id.btnManageBooking);

        barChart = view.findViewById(R.id.barChartStats);

        // Hiển thị thông tin admin
        tvAdminEmail.setText(pref.getEmail() != null ? pref.getEmail() : "admin@pod.com");
        tvAdminName.setText(pref.getName() != null ? pref.getName() : "Administrator");

        // Đăng xuất
        btnLogout.setOnClickListener(v -> {
            try {
                FirebaseAuth.getInstance().signOut();
            } catch (Exception ignored) {}
            pref.logout();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });

        // Mở danh sách Account
        btnManageAccounts.setOnClickListener(v -> {
            openFragment(new AccountListFragment());
        });


        // Mở danh sách Room
        btnManageRooms.setOnClickListener(v -> openFragment(new RoomListFragment()));

        // Mở danh sách Payment
        btnManagePayments.setOnClickListener(v -> openFragment(new PaymentListFragment()));

        // Mở danh sách Booking
        btnManageBooking.setOnClickListener(v -> openFragment(new BookingListFragment()));

        loadDashboardData();

        return view;
    }

    private void loadDashboardData() {
        // Khởi tạo lại giá trị (dùng null để biết API nào chưa chạy xong)
        totalAccounts = null;
        totalRooms = null;
        totalBookings = null;

        // 1. Tải tổng số Accounts
        AccountApi accountApi = ApiClient.getAccountApi();
        accountApi.getAccounts().enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                totalAccounts = (response.isSuccessful() && response.body() != null) ? response.body().size() : 0;
                trySetupChart(); // Thử vẽ
            }
            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                totalAccounts = 0; // Đặt là 0 nếu lỗi mạng
                trySetupChart();
            }
        });

        // 2. Tải tổng số Rooms
        RoomApi roomApi = ApiClient.getRoomApi();
        roomApi.getAllRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                totalRooms = (response.isSuccessful() && response.body() != null) ? response.body().size() : 0;
                trySetupChart(); // Thử vẽ
            }
            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                totalRooms = 0;
                trySetupChart();
            }
        });

        // 3. Tải tổng số Bookings
        BookingApi bookingApi = ApiClient.getBookingApi();
        bookingApi.getAllBookings().enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                totalBookings = (response.isSuccessful() && response.body() != null) ? response.body().size() : 0;
                trySetupChart(); // Thử vẽ
            }
            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                totalBookings = 0;
                trySetupChart();
            }
        });
    }

    // --- THÊM HÀM VẼ BIỂU ĐỒ ---
    private void trySetupChart() {
        // Nếu một trong 3 API chưa trả về (vẫn còn null), thì thoát
        if (totalAccounts == null || totalRooms == null || totalBookings == null) {
            return;
        }

        // --- Nếu cả 3 đã tải xong, tiến hành vẽ biểu đồ ---
        Log.d("AdminDashBoard", "Vẽ biểu đồ: Accounts=" + totalAccounts + ", Rooms=" + totalRooms + ", Bookings=" + totalBookings);

        // 1. Tạo các cột dữ liệu
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, totalAccounts.floatValue())); // Cột 0: Accounts
        entries.add(new BarEntry(1f, totalRooms.floatValue()));    // Cột 1: Rooms
        entries.add(new BarEntry(2f, totalBookings.floatValue())); // Cột 2: Bookings

        // 2. Tạo DataSet (bộ dữ liệu)
        BarDataSet dataSet = new BarDataSet(entries, "Thống kê");

        // Đặt màu cho từng cột (Giống màu 3 nút bấm)
        dataSet.setColors(
                Color.parseColor("#3498DB"), // Màu Accounts
                Color.parseColor("#1ABC9C"), // Màu Rooms
                Color.parseColor("#2ECC71")  // Màu Bookings
        );
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(14f); // Kích thước số trên đầu cột

        // 3. Tạo Dữ liệu Biểu đồ
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f); // Độ rộng của cột
        barChart.setData(barData);

        // 4. Tùy chỉnh (Labels cho trục X: "Accounts", "Rooms", "Bookings")
        String[] labels = new String[]{"Accounts", "Rooms", "Bookings"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt nhãn ở dưới
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false); // Tắt lưới
        xAxis.setTextSize(12f); // Kích thước chữ của nhãn

        // 5. Tùy chỉnh (Tắt các thứ rườm rà)
        barChart.getAxisLeft().setAxisMinimum(0f); // Bắt đầu từ 0
        barChart.getAxisRight().setEnabled(false); // Tắt trục Y bên phải
        barChart.getDescription().setEnabled(false); // Tắt mô tả
        barChart.getLegend().setEnabled(false); // Tắt chú thích
        barChart.setDoubleTapToZoomEnabled(false); // Tắt zoom

        // 6. Hiệu ứng và vẽ
        barChart.animateY(1000);
        barChart.invalidate(); // Yêu cầu biểu đồ tự vẽ lại
    }

    private void openFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

}
