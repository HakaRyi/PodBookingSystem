package com.example.g6podbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.Booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {
    private final Context context;
    private List<Booking> bookingList;

    public BookingHistoryAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvCustomer.setText(booking.getUser().getName());
        holder.tvRoom.setText("Phòng: " + booking.getBookingDetails().get(0).getRoom().getName());
        holder.tvTime.setText(booking.getBookingDate() + " | " + booking.getBookingDetails().get(0).getStartTime() + " - " + booking.getBookingDetails().get(0).getEndTime());
        holder.tvAmount.setText(String.format(Locale.getDefault(), "%,.0f VND", booking.getTotal()));

        // Màu trạng thái nổi bật
        switch (booking.getStatus()) {
            case "CHECK-OUT":
                holder.tvStatus.setText("Hoàn thành");
                holder.tvStatus.setBackgroundTintList(context.getColorStateList(android.R.color.holo_green_dark));
                break;
            case "CHECK-IN":
                holder.tvStatus.setText("Đang sử dụng");
                holder.tvStatus.setBackgroundTintList(context.getColorStateList(android.R.color.holo_orange_dark));
                break;
            case "CANCELED":
                holder.tvStatus.setText("Đã hủy");
                holder.tvStatus.setBackgroundTintList(context.getColorStateList(android.R.color.holo_red_dark));
                break;
            default:
                holder.tvStatus.setText(booking.getStatus());
                holder.tvStatus.setBackgroundTintList(context.getColorStateList(android.R.color.darker_gray));
                break;
        }

        if (booking.getFeedback() != null) {
            holder.ratingBar.setRating(booking.getFeedback().getRating());
            holder.tvFeedback.setText(booking.getFeedback() != null ? booking.getFeedback().getDescription() : "Không có nhận xét");
        } else {
            holder.ratingBar.setRating(0);
            holder.tvFeedback.setText("Chưa có đánh giá");
        }

        // Nhấn vào để mở/đóng phần đánh giá
        holder.cardView.setOnClickListener(v -> {
            if (holder.layoutRating.getVisibility() == View.VISIBLE) {
                holder.layoutRating.setVisibility(View.GONE);
            } else {
                holder.layoutRating.setVisibility(View.VISIBLE);
            }
        });

        // (Tuỳ chọn) Load avatar người dùng nếu bạn có URL
        holder.imgAvatar.setImageResource(R.drawable.ic_person);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void updateList(List<Booking> newList) {
        this.bookingList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvCustomer, tvRoom, tvTime, tvStatus, tvAmount, tvFeedback;
        RatingBar ratingBar;
        LinearLayout layoutRating;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvCustomer = itemView.findViewById(R.id.tvCustomerName);
            tvRoom = itemView.findViewById(R.id.tvRoomName);
            tvTime = itemView.findViewById(R.id.tvBookingTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAmount = itemView.findViewById(R.id.tvTotal);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            layoutRating = itemView.findViewById(R.id.layoutRating);
            cardView = (CardView) itemView;
            tvFeedback = itemView.findViewById(R.id.tvFeedback);
        }
    }
}
