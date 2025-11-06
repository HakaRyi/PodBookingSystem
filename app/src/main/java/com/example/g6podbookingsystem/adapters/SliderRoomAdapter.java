package com.example.g6podbookingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.Room;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SliderRoomAdapter extends RecyclerView.Adapter<SliderRoomAdapter.SliderViewHolder> {

    private List<Room> rooms;
    private Context context;

    public SliderRoomAdapter(Context context, List<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slider_room, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Room room = rooms.get(position % rooms.size());

        holder.tvName.setText(room.getName());
        holder.tvType.setText(room.getType() != null ? room.getType().getName().trim() : "Phòng");
        holder.tvPrice.setText(formatPrice(room.getPrice()) + "/giờ");

        if (room.getImgUrl() != null && !room.getImgUrl().isEmpty()) {
            Glide.with(context)
                    .load(room.getImgUrl())
                    .placeholder(R.drawable.ic_pod_placeholder)
                    .into(holder.imgBg);
        }
    }

    @Override
    public int getItemCount() {
        return rooms.isEmpty() ? 0 : Integer.MAX_VALUE; // Vòng lặp vô hạn
    }

    private String formatPrice(Double price) {
        if (price == null) return "0đ";
        NumberFormat f = NumberFormat.getInstance(new Locale("vi", "VN"));
        return f.format(price);
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBg;
        TextView tvName, tvType, tvPrice;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBg = itemView.findViewById(R.id.imgSliderBg);
            tvName = itemView.findViewById(R.id.tvSliderRoomName);
            tvType = itemView.findViewById(R.id.tvSliderRoomType);
            tvPrice = itemView.findViewById(R.id.tvSliderPrice);
        }
    }
}