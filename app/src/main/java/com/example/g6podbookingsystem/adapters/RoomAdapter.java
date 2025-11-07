package com.example.g6podbookingsystem.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.Room;
import com.squareup.picasso.Picasso; // Thư viện tuyệt vời để load ảnh, thêm vào build.gradle

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Bạn cần thêm thư viện Picasso:
// implementation 'com.squareup.picasso:picasso:2.71828' (hoặc 2.8)

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList = new ArrayList<>();
    private OnRoomListener onRoomListener;

    public RoomAdapter(OnRoomListener onRoomListener) {
        this.onRoomListener = onRoomListener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view, onRoomListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.bind(room);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public void setRooms(List<Room> rooms) {
        this.roomList = rooms;
        notifyDataSetChanged();
    }

    // ----- ViewHolder -----
    static class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgRoom;
        TextView tvRoomName, tvRoomPrice, tvRoomStatus;
        OnRoomListener onRoomListener;
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));


        public RoomViewHolder(@NonNull View itemView, OnRoomListener onRoomListener) {
            super(itemView);
            imgRoom = itemView.findViewById(R.id.img_room);
            tvRoomName = itemView.findViewById(R.id.tv_room_name);
            tvRoomPrice = itemView.findViewById(R.id.tv_room_price);
            tvRoomStatus = itemView.findViewById(R.id.tv_room_status);
            this.onRoomListener = onRoomListener;

            itemView.setOnClickListener(this);
        }

        public void bind(Room room) {
            tvRoomName.setText(room.getName());
            tvRoomPrice.setText("Giá: " + formatter.format(room.getPrice()));
            tvRoomStatus.setText(room.getStatus());

            // Load ảnh từ URL
            if (room.getImgUrl() != null && !room.getImgUrl().isEmpty()) {
                Picasso.get()
                        .load(room.getImgUrl())
                        .placeholder(R.drawable.ic_baseline_house_24) // Ảnh mặc định
                        .error(R.drawable.ic_baseline_error_24) // Ảnh khi lỗi
                        .into(imgRoom);
            } else {
                imgRoom.setImageResource(R.drawable.ic_baseline_house_24);
            }
        }

        @Override
        public void onClick(View v) {
            onRoomListener.onRoomClick(getAdapterPosition());
        }
    }

    // ----- Interface để xử lý click -----
    public interface OnRoomListener {
        void onRoomClick(int position);
    }
}