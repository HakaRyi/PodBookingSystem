package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.adapters.AccountAdapter;
import com.example.g6podbookingsystem.models.Account;
import com.example.g6podbookingsystem.services.AccountApi;
import com.example.g6podbookingsystem.services.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountListFragment extends Fragment {

    private RecyclerView recycler;
    private AccountAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recycler = view.findViewById(R.id.recyclerList);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        FloatingActionButton btnAdd = view.findViewById(R.id.btnAddAccount);
        btnAdd.setOnClickListener(v -> openFragment(new AccountCreateFragment()));

        getParentFragmentManager().setFragmentResultListener("reload", this, (key, bundle) -> loadAccounts());
        loadAccounts();
    }

    private void loadAccounts() {
        AccountApi api = ApiClient.getClient().create(AccountApi.class);
        api.getAccounts().enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new AccountAdapter(response.body(), account -> {
                        Bundle args = new Bundle();
                        args.putInt("accountId", account.getAccId());
                        Fragment detail = new AccountDetailFragment();
                        detail.setArguments(args);
                        openFragment(detail);
                    });
                    recycler.setAdapter(adapter);
                } else {
                    Toast.makeText(requireContext(), "Không tải được accounts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
