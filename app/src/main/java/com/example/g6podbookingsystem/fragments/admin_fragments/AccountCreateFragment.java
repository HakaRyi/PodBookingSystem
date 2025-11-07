package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.dto.CreateAccountRequest;
import com.example.g6podbookingsystem.models.Account;
import com.example.g6podbookingsystem.services.AccountApi;
import com.example.g6podbookingsystem.services.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountCreateFragment extends Fragment {

    private EditText edtName, edtEmail, edtPhone, edtPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edtName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtPassword = view.findViewById(R.id.edtPassword);

        view.findViewById(R.id.btnSave).setOnClickListener(v -> createAccount());
        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }

    private void createAccount() {
        CreateAccountRequest req = new CreateAccountRequest();
        req.name = edtName.getText().toString();
        req.email = edtEmail.getText().toString();
        req.phone = edtPhone.getText().toString();
        req.password = edtPassword.getText().toString();
        req.roleId = 1;

        ApiClient.getClient()
                .create(AccountApi.class)
                .createAccount(req)
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(), "✅ Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();

                            getParentFragmentManager().setFragmentResult("reload", new Bundle());

                            requireActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(requireContext(), "❌ Lỗi API: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        Toast.makeText(requireContext(), "🚫 Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
