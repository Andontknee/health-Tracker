package com.example.healthtracker;

import android.content.Context;
import android.content.SharedPreferences;

// ADD THESE THREE IMPORT STATEMENTS
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.ArrayList;

public class DataStorageHelper {

    private static final String PREFERENCES_FILE_NAME = "app_data";
    private static final String LOGS_KEY = "journal_logs";

    public static void saveLogs(Context context, ArrayList<DailyLog> logs) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(logs);

        editor.putString(LOGS_KEY, json);
        editor.apply();
    }

    public static ArrayList<DailyLog> loadLogs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString(LOGS_KEY, null);

        Type type = new TypeToken<ArrayList<DailyLog>>() {}.getType();
        ArrayList<DailyLog> logs = gson.fromJson(json, type);

        if (logs == null) {
            logs = new ArrayList<>();
        }

        return logs;
    }
}