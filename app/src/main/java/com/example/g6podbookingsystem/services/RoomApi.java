package com.example.g6podbookingsystem.services;

import com.example.g6podbookingsystem.models.Room;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RoomApi {
    @GET("api/Room")
    Call<List<Room>> getAllRooms();

    @GET("api/Room/{id}")
    Call<Room> getRoomById(@Path("id") int roomId);

    @POST("api/Room")
    Call<Integer> createRoom(@Body Room room); // Backend của bạn trả về int

    @PUT("api/Room/{id}")
    Call<Integer> updateRoom(@Path("id") int roomId, @Body Room room); // Backend trả về int

    @DELETE("api/Room/{id}")
    Call<Void> deleteRoom(@Path("id") int roomId); // Backend trả về 204 No Content (Boolean sẽ lỗi)
}
