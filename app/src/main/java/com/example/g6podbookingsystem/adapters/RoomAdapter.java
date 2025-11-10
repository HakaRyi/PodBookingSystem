package com.example.g6podbookingsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.activities.RoomDetailActivity;
import com.example.g6podbookingsystem.models.Room;
import com.google.android.material.button.MaterialButton;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private Context context;
    private OnItemClickListener listener;

    public RoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pod_card, parent, false);
        return new RoomViewHolder(view);
    }
    public interface OnItemClickListener {
        void onItemClick(Room room);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        holder.tvRoomName.setText(room.getName());
        holder.tvRoomType.setText(room.getType() != null ? room.getType().getName().trim() : "Phòng");
        holder.tvPrice.setText(formatPrice(room.getPrice()) + "/giờ");

        // Load ảnh (nếu có)
        if (room.getImgUrl() != null && !room.getImgUrl().isEmpty()) {
            Glide.with(context).load(room.getImgUrl()).placeholder(R.drawable.ic_pod_placeholder).into(holder.imgPod);
        }

        // Click card → chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RoomDetailActivity.class);
            intent.putExtra("room", room);
            context.startActivity(intent);
        });

        // Nút Book
        holder.btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(context, RoomDetailActivity.class);
            intent.putExtra("room", room);
            context.startActivity(intent);
        });
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(roomList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    private String formatPrice(Double price) {
        if (price == null) return "0đ";
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPod;
        TextView tvRoomName, tvRoomType, tvPrice;
        MaterialButton btnBook;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPod = itemView.findViewById(R.id.imgPod);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}