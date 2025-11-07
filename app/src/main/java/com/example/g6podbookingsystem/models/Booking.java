package com.example.g6podbookingsystem.models;

import java.io.Serializable;
import java.util.List;

public class Booking implements Serializable {
    private int bookingId;
    private int userId;
    private String bookingDate;
    private double total;
    private String status; // "Pending", "CheckedIn", "CheckedOut"

    private String customerName; // thêm để dễ hiển thị
    private List<BookingDetail> bookingDetails;
    private List<RoomSlot> roomSlots;
    private Account user;
    private Feedback feedback;
    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }



    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }
    public  Booking(){

    }
    public Booking(int bookingId, String customerName, String bookingDate, double total, String status) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.bookingDate = bookingDate;
        this.total = total;
        this.status = status;
    }
    public List<RoomSlot> getRoomSlots() {
        return roomSlots;
    }

    public void setRoomSlots(List<RoomSlot> roomSlots) {
        this.roomSlots = roomSlots;
    }
    // Getter / Setter
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public List<BookingDetail> getBookingDetails() { return bookingDetails; }
    public void setBookingDetails(List<BookingDetail> bookingDetails) { this.bookingDetails = bookingDetails; }

    public boolean isCheckedIn() {
        return "CheckedIn".equalsIgnoreCase(status);
    }

    public boolean isCheckedOut() {
        return "CheckedOut".equalsIgnoreCase(status);
    }
}

