package com.example.g6podbookingsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.Payment;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<Payment> payments;

    public PaymentAdapter(List<Payment> payments) {
        this.payments = payments;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment p = payments.get(position);
        holder.tvPaymentId.setText("Payment ID: " + p.paymentId);
        holder.tvBookingId.setText("Booking ID: " + p.bookingId);
        holder.tvTotalAmount.setText("Amount: $" + p.totalAmount);
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tvPaymentId, tvBookingId, tvTotalAmount;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPaymentId = itemView.findViewById(R.id.tvPaymentId);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
        }
    }
}
