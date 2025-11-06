package com.example.g6podbookingsystem.repositories;

import com.example.g6podbookingsystem.services.ApiClient;
import com.example.g6podbookingsystem.services.BookingApi;
import com.example.g6podbookingsystem.services.BookingDetailApi;

public class BookingDetailRepository {
    public static BookingDetailApi getBookingDetailService() {
        return ApiClient.getClient().create(BookingDetailApi.class);
    }
}
