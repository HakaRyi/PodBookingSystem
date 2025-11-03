package com.example.g6podbookingsystem.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.activities.BookingDetailActivity;
import com.example.g6podbookingsystem.models.Booking;
import com.example.g6podbookingsystem.models.BookingDetail;
import com.example.g6podbookingsystem.repositories.BookingRepository;
import com.example.g6podbookingsystem.services.BookingApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookings;

    public BookingAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        holder.bind(bookings.get(position));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomer, tvTime, tvStatus;
        Button btnCheckIn, btnCheckOut;
        ImageView imgAvatar;

        BookingApi bookingApi;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnCheckIn = itemView.findViewById(R.id.btnCheckIn);
            btnCheckOut = itemView.findViewById(R.id.btnCheckOut);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }

        public void bind(Booking b) {

            String customerName = (b.getUser() != null)
                    ? b.getUser().getName()
                    : "Khách #" + b.getUserId();

            // Lấy thời gian đầu tiên trong BookingDetail (nếu có)
            String time = "N/A";
            if (b.getBookingDetails() != null && !b.getBookingDetails().isEmpty()) {
                BookingDetail d = b.getBookingDetails().get(0);
                time = d.getStartTime() + " - " + d.getEndTime();
            }

            if (b.getUser() != null && b.getUser().getAvatarUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(b.getUser().getAvatarUrl())
                        .placeholder(R.drawable.ic_account) // ảnh mặc định
                        .circleCrop()
                        .into(imgAvatar);
            } else {
                imgAvatar.setImageResource(R.drawable.ic_account);
            }

            tvCustomer.setText(customerName + " (" + b.getBookingId() + ")");
            tvTime.setText(time);
            tvStatus.setText("Trạng thái: " + b.getStatus());

            switch (b.getStatus().toUpperCase()) {
                case "BOOKED":
                    btnCheckIn.setVisibility(View.VISIBLE);
                    btnCheckOut.setVisibility(View.GONE);
                    break;

                case "CHECK-IN":
                    btnCheckIn.setVisibility(View.GONE);
                    btnCheckOut.setVisibility(View.VISIBLE);
                    break;

                case "CHECK-OUT":
                default:
                    btnCheckIn.setVisibility(View.GONE);
                    btnCheckOut.setVisibility(View.GONE);
                    break;
            }

            btnCheckIn.setOnClickListener(v -> {
                bookingApi = BookingRepository.getBookingService();
                bookingApi.checkIn(b.getBookingId()).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful() && response.body() != null && response.body() > 0){
                            b.setStatus("CHECK-IN");
                            notifyItemChanged(getAdapterPosition());
                            Toast.makeText(itemView.getContext(), "✅ Check-in thành công", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(itemView.getContext(), "check-in thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(itemView.getContext(), "Lỗi server", Toast.LENGTH_SHORT).show();
                    }
                });


            });

            btnCheckOut.setOnClickListener(v -> {
                bookingApi = BookingRepository.getBookingService();
                bookingApi.checkOut(b.getBookingId()).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful() && response.body() != null && response.body() > 0){
                            b.setStatus("CHECK-OUT");
                            notifyItemChanged(getAdapterPosition());
                            Toast.makeText(itemView.getContext(), "✅ Check-out thành công", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(itemView.getContext(), "check-out thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(itemView.getContext(), "Lỗi server", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), BookingDetailActivity.class);
                intent.putExtra("bookingId", b.getBookingId());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
