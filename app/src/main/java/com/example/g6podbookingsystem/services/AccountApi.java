package com.example.g6podbookingsystem.services;

import com.example.g6podbookingsystem.dto.CreateAccountRequest;
import com.example.g6podbookingsystem.models.Account;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountApi {

    // Create account
    @POST("api/Account")
    Call<Account> createAccount(@Body CreateAccountRequest request);

    // Login by email (optional)
    @GET("api/Account/by-email/{email}")
    Call<Account> getAccountByEmail(@Path("email") String email);

    // ✅ Get all accounts
    @GET("api/Account")
    Call<List<Account>> getAccounts();

    // ✅ Get account by ID → dùng cho AccountDetail
    @GET("api/Account/{id}")
    Call<Account> getAccountById(@Path("id") int id);

    // ✅ Update account → dùng cho EditAccount
    @PUT("api/Account/{id}")
    Call<Account> updateAccount(@Path("id") int id, @Body Account account);

    // ✅ Delete account by ID → dùng cho nút Delete Admin
    @DELETE("api/Account/{id}")
    Call<Void> deleteAccount(@Path("id") int id);
}
