package com.example.g6podbookingsystem.models;

import java.io.Serializable;

public class Slot implements Serializable {
    private int slotId;
    private String description; // Ví dụ: "06:00-07:00"

    // GETTER
    public int getSlotId() {
        return slotId;
    }

    public String getDescription() {
        return description;
    }

    // SETTER (cần cho Gson/Retrofit)
    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Slot " + slotId + " (" + description + ")";
    }
}