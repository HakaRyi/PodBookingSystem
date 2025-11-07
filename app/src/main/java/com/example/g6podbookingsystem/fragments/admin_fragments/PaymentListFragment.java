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
import android.widget.Button;
import android.widget.Toast;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.adapters.PaymentAdapter;
import com.example.g6podbookingsystem.models.Payment;
import com.example.g6podbookingsystem.services.ApiClient;
import com.example.g6podbookingsystem.services.PaymentApi;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentListFragment extends Fragment {

    private RecyclerView rvPayments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_list, container, false);

        rvPayments = view.findViewById(R.id.rvPayments);
        rvPayments.setLayoutManager(new LinearLayoutManager(requireContext()));
        Button btnBack = view.findViewById(R.id.btnBackPayment);
        btnBack.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager().popBackStack());


        loadPayments();
        return view;
    }

    private void loadPayments() {
        PaymentApi api = ApiClient.getClient().create(PaymentApi.class);

        api.getPayments().enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                if(response.isSuccessful() && response.body() != null){
                    rvPayments.setAdapter(new PaymentAdapter(response.body()));
                } else {
                    Toast.makeText(getContext(), "No data!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                Toast.makeText(getContext(), "API Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
