package ru.smarthome.utils;


import android.content.Context;
import android.content.SharedPreferences;

import ru.smarthome.constants.Constants;

public class SharedPreferencesHelper {

    private final static String SERVER_URL = "server_url";
    private final static String USER_ID = "user_id";

    private final SharedPreferences sharedPref;

    public SharedPreferencesHelper(Context context) {
        sharedPref = context.getSharedPreferences("", Context.MODE_PRIVATE);
    }

    public String getServerUrl() {
        return sharedPref.getString(SERVER_URL, Constants.SERVER_URL);
    }

    public void setServerUrl(String url) {
        sharedPref.edit().putString(SERVER_URL, url).apply();
    }

    public int getUserId() {
        return sharedPref.getInt(USER_ID, 0);
    }

    public void setUserId(int id) {
        sharedPref.edit().putInt(USER_ID, id).apply();
    }
}
