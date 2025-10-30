package com.example.g6podbookingsystem.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //Thay số sau 10.0.2.2: bằng địa chỉ backend của mn nha
    private static String baseURL = "http://10.0.2.2:5143/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
