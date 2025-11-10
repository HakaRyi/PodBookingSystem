package com.example.g6podbookingsystem.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.g6podbookingsystem.models.Account;
import com.google.gson.Gson;

public class SharedPrefManager {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER_OBJECT = "USER_FULL_OBJECT";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson();
    public SharedPrefManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public Account getUser() {
        String json = prefs.getString(KEY_USER_OBJECT, null);
        if (json == null || json.isEmpty()) {
            // Nếu chưa có object → tạo từ email + name cũ
            String email = prefs.getString(KEY_EMAIL, null);
            String name = prefs.getString(KEY_NAME, null);
            if (email == null) return null;
            Account temp = new Account();
            temp.email = email;
            temp.name = name;
            temp.accId = 0;
            temp.phone = "";
            temp.avatarUrl = "";
            temp.roleId = 2;
            return temp;
        }
        try {
            return gson.fromJson(json, Account.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void saveUser(Account account) {
        editor.putString(KEY_EMAIL, account.email);
        editor.putString(KEY_NAME, account.name);
        editor.putString(KEY_USER_OBJECT, gson.toJson(account));
        editor.apply();
    }
    public boolean isLoggedIn() {
        return prefs.getString(KEY_EMAIL, null) != null;
    }
    public void clearUser() {
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_NAME);
        editor.remove(KEY_USER_OBJECT);
        editor.apply();
    }
    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    public void logout() {
        editor.clear().apply();
    }

    public String getName() {
        return prefs.getString(KEY_NAME, null);
    }

}
