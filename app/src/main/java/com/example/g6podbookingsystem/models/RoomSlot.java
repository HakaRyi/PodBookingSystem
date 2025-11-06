package com.example.g6podbookingsystem.models;

import java.io.Serializable;

public class RoomSlot implements Serializable {
    private int slotId;
    private int roomId;
    private Integer bookingId; // nullable
    private String bookingDate; // "2025-11-06"

    // Getters and Setters
    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
}