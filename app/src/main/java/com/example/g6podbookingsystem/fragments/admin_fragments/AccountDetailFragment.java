package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.Account;
import com.example.g6podbookingsystem.services.AccountApi;
import com.example.g6podbookingsystem.services.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountDetailFragment extends Fragment {

    private int accountId;
    private ImageView imgAvatar, btnBack;
    private TextView tvName, tvEmail, tvPhone, tvPassword;
    private EditText edtName, edtEmail, edtPhone, edtPassword;
    private Button btnEdit, btnSave, btnDelete;
    private Account currentAccount;
    private boolean isEditMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater,
                             @Nullable android.view.ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {

        accountId = getArguments().getInt("accountId");

        imgAvatar = v.findViewById(R.id.imgAvatar);
        btnBack = v.findViewById(R.id.btnBack);

        tvName = v.findViewById(R.id.tvName);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvPhone = v.findViewById(R.id.tvPhone);
        tvPassword = v.findViewById(R.id.tvPassword);

        edtName = v.findViewById(R.id.edtName);
        edtEmail = v.findViewById(R.id.edtEmail);
        edtPhone = v.findViewById(R.id.edtPhone);
        edtPassword = v.findViewById(R.id.edtPassword);

        btnEdit = v.findViewById(R.id.btnEdit);
        btnSave = v.findViewById(R.id.btnSave);
        btnDelete = v.findViewById(R.id.btnDelete);

        btnBack.setOnClickListener(v2 -> requireActivity().getSupportFragmentManager().popBackStack());

        loadAccount();

        btnEdit.setOnClickListener(v3 -> switchMode(true));
        btnSave.setOnClickListener(v4 -> saveChanges());
        btnDelete.setOnClickListener(v5 -> confirmDelete());
    }

    private void loadAccount() {
        AccountApi api = ApiClient.getClient().create(AccountApi.class);
        api.getAccountById(accountId).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> r) {
                if (r.isSuccessful() && r.body() != null) {
                    currentAccount = r.body();
                    bindData();
                }
            }
            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(requireContext(), "Load failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindData() {
        tvName.setText(currentAccount.name);
        tvEmail.setText(currentAccount.email);
        tvPhone.setText(currentAccount.phone);
        tvPassword.setText("••••••");

        edtName.setText(currentAccount.name);
        edtEmail.setText(currentAccount.email);
        edtPhone.setText(currentAccount.phone);
        edtPassword.setText(currentAccount.password);

        if (currentAccount.avatarUrl != null)
            Glide.with(requireContext()).load(currentAccount.avatarUrl).circleCrop().into(imgAvatar);
    }

    private void switchMode(boolean editMode) {
        isEditMode = editMode;

        int viewMode = editMode ? View.GONE : View.VISIBLE;
        int editTextMode = editMode ? View.VISIBLE : View.GONE;

        tvName.setVisibility(viewMode);
        tvEmail.setVisibility(viewMode);
        tvPhone.setVisibility(viewMode);
        tvPassword.setVisibility(viewMode);

        edtName.setVisibility(editTextMode);
        edtEmail.setVisibility(editTextMode);
        edtPhone.setVisibility(editTextMode);
        edtPassword.setVisibility(editTextMode);

        btnEdit.setVisibility(viewMode);
        btnSave.setVisibility(editTextMode);
    }

    private void saveChanges() {
        currentAccount.name = edtName.getText().toString();
        currentAccount.email = edtEmail.getText().toString();
        currentAccount.phone = edtPhone.getText().toString();
        currentAccount.password = edtPassword.getText().toString();

        AccountApi api = ApiClient.getClient().create(AccountApi.class);
        api.updateAccount(accountId, currentAccount).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> r) {
                Toast.makeText(requireContext(), "Update Success", Toast.LENGTH_SHORT).show();
                switchMode(false);
                bindData();
            }
            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDelete() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Account")
                .setMessage("Are you sure?")
                .setPositiveButton("YES", (d, i) -> deleteAccount())
                .setNegativeButton("NO", null)
                .show();
    }

    private void deleteAccount() {
        AccountApi api = ApiClient.getClient().create(AccountApi.class);
        api.deleteAccount(accountId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> r) {
                Toast.makeText(requireContext(), "Deleted ✅", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
