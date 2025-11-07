package com.example.g6podbookingsystem.services;

import com.example.g6podbookingsystem.models.Payment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PaymentApi {
    @GET("api/Payment")
    Call<List<Payment>> getPayments();
}
