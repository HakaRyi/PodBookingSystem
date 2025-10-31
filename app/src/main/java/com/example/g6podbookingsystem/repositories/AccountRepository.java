package com.example.g6podbookingsystem.repositories;

import com.example.g6podbookingsystem.services.AccountApi;
import com.example.g6podbookingsystem.services.ApiClient;

public class AccountRepository {
    public static AccountApi getAccountService() {
        return ApiClient.getClient().create(AccountApi.class);
    }
}
