package com.example.g6podbookingsystem.adapters;

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
import com.example.g6podbookingsystem.models.Booking;
import com.example.g6podbookingsystem.models.BookingDetail;

import java.util.List;

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

            btnCheckIn.setEnabled(!b.isCheckedIn() && !"CheckedOut".equalsIgnoreCase(b.getStatus()));
            btnCheckOut.setEnabled(b.isCheckedIn() && !b.isCheckedOut());

            btnCheckIn.setOnClickListener(v -> {
                b.setStatus("CheckedIn");
                notifyItemChanged(getAdapterPosition());
                Toast.makeText(itemView.getContext(), "✅ Check-in thành công", Toast.LENGTH_SHORT).show();
            });

            btnCheckOut.setOnClickListener(v -> {
                b.setStatus("CheckedOut");
                notifyItemChanged(getAdapterPosition());
                Toast.makeText(itemView.getContext(), "🏁 Check-out thành công", Toast.LENGTH_SHORT).show();
            });

            itemView.setOnClickListener(v -> {
                // TODO: mở màn chi tiết Booking nếu cần
                Toast.makeText(itemView.getContext(),
                        "Chi tiết booking #" + b.getBookingId(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
