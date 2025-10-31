package com.example.g6podbookingsystem.models;

public class Room {
        private int roomId;
        private String name;
        private int capacity;
        private String status;
        private Double price;
        private Double priceDay;
        private int typeId;
        private String description;
        private String imgUrl;
        private RoomType type;

        public int getRoomId() { return roomId; }
        public String getName() { return name; }
        public int getCapacity() { return capacity; }
        public String getStatus() { return status; }
        public Double getPrice() { return price; }
        public Double getPriceDay() { return priceDay; }
        public int getTypeId() { return typeId; }
        public String getDescription() { return description; }
        public String getImgUrl() { return imgUrl; }
        public RoomType getType() { return type; }
}
