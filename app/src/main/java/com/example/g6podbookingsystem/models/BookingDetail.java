package com.example.g6podbookingsystem.models;

public class BookingDetail {
    private int bookingDetailId;
    private int roomId;
    private int bookingId;
    private String bookingType;
    private double totalPrice;
    private String timestamp;
    private String startTime;
    private String endTime;
    private Room room;

    public int getBookingDetailId() { return bookingDetailId; }
    public int getRoomId() { return roomId; }
    public int getBookingId() { return bookingId; }
    public String getBookingType() { return bookingType; }
    public double getTotalPrice() { return totalPrice; }
    public String getTimestamp() { return timestamp; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public Room getRoom() { return room; }
}
