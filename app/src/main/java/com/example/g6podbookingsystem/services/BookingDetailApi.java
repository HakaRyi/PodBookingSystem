package com.example.g6podbookingsystem.services;

import com.example.g6podbookingsystem.models.Slot;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookingDetailApi {

    @POST("api/BookingDetail/create/{bookingId}/{roomId}")
    Call<CreateDetailResponse> createDetail(
            @Path("bookingId") int bookingId,
            @Path("roomId") int roomId,
            @Body CreateDetailRequest request
    );
    @DELETE("api/BookingDetail/{detailId}")
    Call<Void> deleteDetail(@Path("detailId") int detailId);
    @GET("api/Slot/available")
    Call<List<Slot>> getAvailableSlots(
            @Query("roomId") int roomId,
            @Query("bookingDate") String bookingDate
    );

    public static class CreateDetailRequest {
        public String startTime;
        public String endTime;
        public String bookingType;
        public List<SlotSelection> slots;

        // THÊM public
        public static class SlotSelection {
            public int slotId;
            public String bookingDate;
        }
    }

    // THÊM public
    public static class CreateDetailResponse {
        public String message;
        public int detailId;
    }
}