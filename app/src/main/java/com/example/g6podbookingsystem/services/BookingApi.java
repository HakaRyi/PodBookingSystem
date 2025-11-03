package com.example.g6podbookingsystem.services;

import com.example.g6podbookingsystem.models.Booking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookingApi {
    @GET("api/Booking")
    Call<List<Booking>> getAllBookings();
    @GET("api/Booking/{id}")
    Call<Booking> getBookingById(@Path("id") int bookingId);
    @PUT("api/Booking/Check-in/{id}")
    Call<Integer> checkIn(@Path("id") int bookingId);

    @PUT("api/Booking/Checkout/{id}")
    Call<Integer> checkOut(@Path("id") int bookingId);
    @PUT("api/Booking/Cancel/{id}")
    Call<Integer> cancelBooking(@Path("id") int bookingId, @Query("message") String reason);
}
