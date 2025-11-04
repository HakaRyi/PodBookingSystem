package com.example.g6podbookingsystem.models;

import java.io.Serializable;

public class RoomType implements Serializable {
    private int typeId;
    private String name;
    private String description;

    public int getTypeId() { return typeId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
