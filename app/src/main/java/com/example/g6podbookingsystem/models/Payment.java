package com.example.g6podbookingsystem.models;

import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("paymentId")
    public int paymentId;
    @SerializedName("bookingId")
    public int bookingId;
    @SerializedName("totalAmount")
    public double totalAmount;
    // booking omitted for lightweight listing
}
