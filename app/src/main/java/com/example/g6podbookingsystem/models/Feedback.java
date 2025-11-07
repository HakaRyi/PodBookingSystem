package com.example.g6podbookingsystem.models;

import java.io.Serializable;
import java.util.Date;

public class Feedback implements Serializable {

    private int feedbackId;
    private int bookingId;
    private String description;
    private Integer rating; // Có thể null
    private String timestamp;

    // Nếu bạn có model Booking riêng (như Booking.java), có thể thêm:
    private Booking booking;

    // === Constructors ===
    public Feedback() {
    }

    public Feedback(int feedbackId, int bookingId, String description, Integer rating, String timestamp) {
        this.feedbackId = feedbackId;
        this.bookingId = bookingId;
        this.description = description;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    // === Getters và Setters ===
    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}