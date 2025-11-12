package com.example.g6podbookingsystem.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Room implements Parcelable {

        @SerializedName("roomId")
        private int roomId;

        @SerializedName("name")
        private String name;

        @SerializedName("capacity")
        private int capacity;

        @SerializedName("status")
        private String status;

        @SerializedName("price")
        private double price; // Backend dùng decimal, Java dùng double

        @SerializedName("priceDay")
        private double priceDay;

        @SerializedName("typeId")
        private int typeId;

        @SerializedName("description")
        private String description;

        @SerializedName("imgUrl")
        private String imgUrl;

        private RoomType type;


        // Các navigation properties (BookingDetails, RoomSlots, Type)
        // thường được bỏ qua trong các request POST/PUT
        // và chỉ dùng để hiển thị (nếu cần).
        // Chúng ta giữ cho model đơn giản để CRUD.

        // Constructor (có thể cần cho GSON)
        public Room() {
        }

        // Các hàm getter/setter
        // (Bạn có thể dùng Generate -> Getters and Setters trong Android Studio)
        public int getRoomId() {
                return roomId;
        }

        public void setRoomId(int roomId) {
                this.roomId = roomId;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public int getCapacity() {
                return capacity;
        }

        public void setCapacity(int capacity) {
                this.capacity = capacity;
        }

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public double getPrice() {
                return price;
        }

        public void setPrice(double price) {
                this.price = price;
        }

        public double getPriceDay() {
                return priceDay;
        }

        public void setPriceDay(double priceDay) {
                this.priceDay = priceDay;
        }

        public int getTypeId() {
                return typeId;
        }

        public void setTypeId(int typeId) {
                this.typeId = typeId;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public String getImgUrl() {
                return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
        }

        public RoomType getType() { return type; }


        // ----- Triển khai Parcelable -----

        protected Room(Parcel in) {
                roomId = in.readInt();
                name = in.readString();
                capacity = in.readInt();
                status = in.readString();
                price = in.readDouble();
                priceDay = in.readDouble();
                typeId = in.readInt();
                description = in.readString();
                imgUrl = in.readString();
                type = in.readParcelable(RoomType.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(roomId);
                dest.writeString(name);
                dest.writeInt(capacity);
                dest.writeString(status);
                dest.writeDouble(price);
                dest.writeDouble(priceDay);
                dest.writeInt(typeId);
                dest.writeString(description);
                dest.writeString(imgUrl);
                dest.writeParcelable(type, flags);
        }

        @Override
        public int describeContents() {
                return 0;
        }

        public static final Creator<Room> CREATOR = new Creator<Room>() {
                @Override
                public Room createFromParcel(Parcel in) {
                        return new Room(in);
                }

                @Override
                public Room[] newArray(int size) {
                        return new Room[size];
                }
        };
}