package com.example.g6podbookingsystem.services;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //Thay số sau 10.0.2.2: bằng địa chỉ backend của mn nha
    private static String baseURL = "http://10.0.2.2:5143/";
    private static Retrofit retrofit;

//    public static Retrofit getClient() {
//        if(retrofit == null){
//            retrofit = new Retrofit.Builder().baseUrl(baseURL)
//                    .addConverterFactory(GsonConverterFactory.create()).build();
//        }
//        return retrofit;
//    }
    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            // Thêm Interceptor: tự động thêm Bearer token
            httpClient.addInterceptor(chain -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String token = null;

                if (user != null) {
                    try {
                        token = Tasks.await(user.getIdToken(true)).getToken();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Request request = chain.request();
                if (token != null) {
                    request = request.newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                }

                return chain.proceed(request);
            });

            // Log request/response
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
