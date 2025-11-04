package com.example.g6podbookingsystem.repositories;

import com.example.g6podbookingsystem.services.AccountApi;
import com.example.g6podbookingsystem.services.ApiClient;
import com.example.g6podbookingsystem.services.RoomApi;

public class RoomRepository {
    public static RoomApi getRoomService() {
        return ApiClient.getClient().create(RoomApi.class);
    }
}
