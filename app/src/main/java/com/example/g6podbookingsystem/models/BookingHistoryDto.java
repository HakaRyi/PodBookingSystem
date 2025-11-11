package com.example.g6podbookingsystem.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookingHistoryDto implements Serializable {
    private int bookingId;
    private String bookingDate;
    private double total;
    private String status;
    private String cancelReason;
    private String cancelDate;
    private String userName;           // THÊM
    private String feedbackComment;
    private List<BookingDetailDto> details;

    public BookingHistoryDto(int bookingId, String bookingDate, double total, String status, String cancelReason, String cancelDate, String userName, String feedbackComment, List<BookingDetailDto> details) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.total = total;
        this.status = status;
        this.cancelReason = cancelReason;
        this.cancelDate = cancelDate;
        this.userName = userName;
        this.feedbackComment = feedbackComment;
        this.details = details;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFeedbackComment() {
        return feedbackComment;
    }

    public void setFeedbackComment(String feedbackComment) {
        this.feedbackComment = feedbackComment;
    }

    public List<BookingDetailDto> getDetails() {
        return details!= null ? details : new ArrayList<>();
    }

    public void setDetails(List<BookingDetailDto> details) {
        this.details = details;
    }
}

