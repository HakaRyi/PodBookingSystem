package com.example.g6podbookingsystem.services;

import com.example.g6podbookingsystem.models.Booking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookingApi {
    @GET("api/Booking")
    Call<List<Booking>> getAllBookings();
    @GET("api/Booking/{id}")
    Call<Booking> getBookingById(@Path("id") int bookingId);
    @POST("api/Booking/createBooking")
    Call<CreateBookingResponse> createBooking();
    @GET("api/Booking/pendingBooking")
    Call<PendingResponse> checkPendingBooking();
    class PendingResponse {
        public String message;
        public Integer bookingId;
    }
    class CreateBookingResponse {
        public int bookingId;
    }
    @GET("api/Booking/pendingBooking2")
    Call<PendingBookingResponse> getPendingBookingFull();
    public class PendingBookingResponse {
        private Booking booking;
        private String message;


        public Booking getBooking() {
            return booking;
        }

        public void setBooking(Booking booking) {
            this.booking = booking;
        }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    @PUT("api/Booking/Check-in/{id}")
    Call<Integer> checkIn(@Path("id") int bookingId);
    @POST("api/Booking/pay/{bookingId}")
    Call<PayOsResponse> createPayment(@Path("bookingId") int bookingId);

    public static class PayOsResponse {
        public String checkoutUrl;
    }
    @PUT("api/Booking/Checkout/{id}")
    Call<Integer> checkOut(@Path("id") int bookingId);
    @PUT("api/Booking/Cancel/{id}")
    Call<Integer> cancelBooking(@Path("id") int bookingId, @Query("message") String reason);
}
