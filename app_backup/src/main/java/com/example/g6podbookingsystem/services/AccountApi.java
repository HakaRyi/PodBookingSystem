package com.example.g6podbookingsystem.services;

import com.example.g6podbookingsystem.dto.CreateAccountRequest;
import com.example.g6podbookingsystem.models.Account;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountApi {
    //Dùng mẹ này để đăng ký
    @POST("api/account")
    Call<Void> createAccount(@Body CreateAccountRequest request);

    //Mẹ này dùng để đăng nhập
    @GET("api/account/by-email/{email}")
    Call<Account> getAccountByEmail(@Path("email") String email);

}
