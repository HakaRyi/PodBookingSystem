package com.example.g6podbookingsystem.repositories;

import com.example.g6podbookingsystem.services.ApiClient;
import com.example.g6podbookingsystem.services.BookingApi;

public class BookingRepository {
    public static BookingApi getBookingService() {
        return ApiClient.getClient().create(BookingApi.class);
    }
}