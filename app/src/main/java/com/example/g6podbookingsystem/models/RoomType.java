package com.example.g6podbookingsystem.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class RoomType implements Parcelable, Serializable {
    private int typeId;
    private String name;
    private String description;
    protected RoomType(Parcel in) {
        typeId = in.readInt();
        name = in.readString();
        description = in.readString();
    }
    public static final Creator<RoomType> CREATOR = new Creator<RoomType>() {
        @Override
        public RoomType createFromParcel(Parcel in) {
            return new RoomType(in);
        }

        @Override
        public RoomType[] newArray(int size) {
            return new RoomType[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(typeId);
        dest.writeString(name);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public int getTypeId() { return typeId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
