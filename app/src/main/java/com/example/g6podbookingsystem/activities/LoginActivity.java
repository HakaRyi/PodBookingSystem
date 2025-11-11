package com.example.g6podbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.Account;
import com.example.g6podbookingsystem.repositories.AccountRepository;
import com.example.g6podbookingsystem.services.AccountApi;
import com.example.g6podbookingsystem.utils.LoadingDialog;
import com.example.g6podbookingsystem.utils.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth mAuth;
    private AccountApi accountApi;
    private SharedPrefManager pref;
    private LoadingDialog loadingDialog;

    private static final String ADMIN_EMAIL = "1";
    private static final String ADMIN_PASSWORD = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        accountApi = AccountRepository.getAccountService();
        pref = new SharedPrefManager(this);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        loadingDialog = new LoadingDialog(this);
        btnLogin.setOnClickListener(v -> login());
        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void login() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1) Nếu là hardcoded admin -> bỏ qua Firebase, lưu và chuyển thẳng
        if (email.equalsIgnoreCase(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
            Account admin = new Account();
            admin.accId = 0;
            admin.email = ADMIN_EMAIL;
            admin.name = "Administrator";
            admin.phone = "";
            admin.avatarUrl = "";
            admin.roleId = 1;

            pref.saveUser(admin);

            Toast.makeText(this, "Đăng nhập admin thành công (local)", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, AdminBookingManageActivity.class));
            finish();
            return;
        }

        // 2) Thực hiện Firebase auth + backend lookup (flow cũ)
        loadingDialog.show("Đang đăng nhập...");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    loadingDialog.hide();
                    if (task.isSuccessful()) {
                        accountApi.getAccountByEmail(email).enqueue(new Callback<Account>() {
                            @Override
                            public void onResponse(Call<Account> call, Response<Account> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    pref.saveUser(response.body());
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    // chuyển sang màn hình chính
                                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));  // ← Thêm
                                    finish();  // ← Thêm
                                } else {
                                    Toast.makeText(LoginActivity.this, "Không tìm thấy hồ sơ!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Account> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "Lỗi server!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        loadingDialog.hide();
                        String err = (task.getException() != null) ? task.getException().getMessage() : "Sai email hoặc mật khẩu";
                        Toast.makeText(LoginActivity.this, "Firebase: " + err, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}