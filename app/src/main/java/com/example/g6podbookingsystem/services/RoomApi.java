package com.example.g6podbookingsystem.services;

import com.example.g6podbookingsystem.dto.CreateAccountRequest;
import com.example.g6podbookingsystem.models.Account;
import com.example.g6podbookingsystem.models.Room;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RoomApi {


    //Mẹ này dùng để đăng nhập
    @GET("api/Room")
    Call<List<Room>> getAllRooms();

}
