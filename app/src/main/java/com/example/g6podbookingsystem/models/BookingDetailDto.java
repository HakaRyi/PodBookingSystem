package com.example.g6podbookingsystem.models;

import java.io.Serializable;

public class BookingDetailDto implements Serializable {
    private int roomId;
    private String roomName;
    private String roomType;
    private String startTime;
    private String endTime;
    private double totalPrice;

    public BookingDetailDto(int roomId, String roomName, String roomType, String startTime, String endTime, double totalPrice) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomType = roomType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
