package com.example.keuangan.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "K3u@ngan";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveLoginDetails(String email, String password) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public void clearLoginDetails() {
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_EMAIL) && sharedPreferences.contains(KEY_PASSWORD);
    }
}
