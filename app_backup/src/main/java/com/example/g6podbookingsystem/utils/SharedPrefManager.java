package com.example.g6podbookingsystem.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.g6podbookingsystem.models.Account;

public class SharedPrefManager {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveUser(Account account) {
        editor.putString(KEY_EMAIL, account.email);
        editor.putString(KEY_NAME, account.name);
        editor.apply();
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    public void logout() {
        editor.clear().apply();
    }
}
