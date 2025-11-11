// File: SlotAdapter.java
package com.example.g6podbookingsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.Slot;

import java.util.ArrayList;
import java.util.List;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.SlotViewHolder> {

    private List<Slot> slots;
    private List<Slot> selectedSlots;
    private OnSlotSelectionListener listener;

    public interface OnSlotSelectionListener {
        void onSelectionChanged();
    }

    public SlotAdapter(List<Slot> slots, List<Slot> selectedSlots, OnSlotSelectionListener listener) {
        this.slots = slots;
        this.selectedSlots = selectedSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        Slot slot = slots.get(position);
        String text = slot.getDescription() + " (Slot " + slot.getSlotId() + ")";
        holder.checkedTextView.setText(text);
        holder.checkedTextView.setChecked(selectedSlots.contains(slot));

        holder.itemView.setOnClickListener(v -> {
            if (selectedSlots.contains(slot)) {
                selectedSlots.remove(slot);
            } else {
                selectedSlots.add(slot);
            }
            notifyDataSetChanged(); // Cập nhật UI
            if (listener != null) {
                listener.onSelectionChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    static class SlotViewHolder extends RecyclerView.ViewHolder {
        CheckedTextView checkedTextView;

        SlotViewHolder(View itemView) {
            super(itemView);
            checkedTextView = (CheckedTextView) itemView;
        }
    }
}