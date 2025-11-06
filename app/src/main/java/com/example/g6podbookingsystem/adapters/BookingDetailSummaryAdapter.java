package com.example.g6podbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.BookingDetail;
import com.example.g6podbookingsystem.models.Room;
import com.example.g6podbookingsystem.models.RoomSlot;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BookingDetailSummaryAdapter extends RecyclerView.Adapter<BookingDetailSummaryAdapter.ViewHolder> {

    private List<BookingDetail> details;
    private List<RoomSlot> roomSlots;
    private Context context;
    private OnItemDeleteListener deleteListener;
    public interface OnItemDeleteListener {
        void onDelete(int detailId, int position);
    }
    public void setOnItemDeleteListener(OnItemDeleteListener listener) {
        this.deleteListener = listener;
    }
    public BookingDetailSummaryAdapter(Context context, List<BookingDetail> details,List<RoomSlot> roomSlots) {
        this.context = context;
        this.details = details;
        this.roomSlots = roomSlots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingDetail detail = details.get(position);
        Room room = detail.getRoom();

        if (room != null) {
            holder.tvRoomName.setText(room.getName());
            if (room.getImgUrl() != null && !room.getImgUrl().isEmpty()) {
                Glide.with(context)
                        .load(room.getImgUrl())
                        .placeholder(R.drawable.ic_pod_placeholder)
                        .error(R.drawable.ic_pod_placeholder)
                        .into(holder.imgRoom);
            }
        }

        // Thời gian: 06:00:00 → 06:00
        String start = detail.getStartTime().substring(0, 5);
        String end = detail.getEndTime().substring(0, 5);
        holder.tvTime.setText(start + " - " + end);

        // Lấy ngày từ RoomSlot (nếu có)
        String date = getDateForDetail(detail, roomSlots);
        holder.tvDate.setText("Ngày: " + date);

        // Giá + loại
        String type = detail.getBookingType().equals("DAY") ? "CẢ NGÀY" : "THEO GIỜ";
        holder.tvPrice.setText(formatPrice(detail.getTotalPrice()) + " (" + type + ")");

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(detail.getBookingDetailId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return details.size();
    }
    private String getDateForDetail(BookingDetail detail, List<RoomSlot> roomSlots) {
        if (roomSlots == null || detail.getRoom() == null) return "Chưa xác định";
        for (RoomSlot rs :  roomSlots) {
            if (rs.getBookingId() != null &&
                    rs.getBookingId() == detail.getBookingId() && // DÙNG bookingId từ detail
                    rs.getRoomId() == detail.getRoomId()) {
                return rs.getBookingDate();
            }
        }
        return "Chưa xác định";
    }
    private String formatPrice(double price) {
        NumberFormat f = NumberFormat.getInstance(new Locale("vi", "VN"));
        return f.format(price) + "đ";
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRoom;
        TextView tvRoomName, tvTime, tvPrice,tvDate;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRoom = itemView.findViewById(R.id.imgRoom);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}