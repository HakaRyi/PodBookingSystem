package com.example.g6podbookingsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.Account;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.VH> {

    private List<Account> items;
    private OnAccountClickListener listener;

    public interface OnAccountClickListener {
        void onAccountClick(Account account);
    }

    public AccountAdapter(List<Account> items, OnAccountClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Account a = items.get(position);
        holder.tvTitle.setText(a.name != null ? a.name : "No name");
        holder.tvSubtitle.setText(a.email != null ? a.email : a.phone != null ? a.phone : "");

        if (a.avatarUrl != null && !a.avatarUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(a.avatarUrl)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .circleCrop()
                    .into(holder.img);
        } else {
            holder.img.setImageResource(R.drawable.ic_baseline_person_24);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onAccountClick(a);
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvTitle, tvSubtitle;
        VH(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgAvatar);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
        }
    }
}
