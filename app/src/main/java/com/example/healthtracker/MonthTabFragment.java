package com.example.healthtracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class MonthTabFragment extends Fragment {
    private ArrayList<DailyLog> dailyLogs = new ArrayList<>();

    public static MonthTabFragment newInstance(ArrayList<DailyLog> logs) {
        MonthTabFragment fragment = new MonthTabFragment();
        fragment.dailyLogs = logs; // Direct passing
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month_tab, container, false);

        TextView textViewAvgSleep = view.findViewById(R.id.textViewAvgSleep);
        TextView textViewAvgWater = view.findViewById(R.id.textViewAvgWater);
        TextView textViewFeedback = view.findViewById(R.id.textViewFeedback);

        // --- Calculation Logic ---
        double totalSleep = 0;
        double totalWater = 0;
        for (DailyLog log : dailyLogs) {
            totalSleep += log.getSleepHours();
            totalWater += log.getWaterLiters();
        }

        // --- Goal Constants ---
        double sleepGoal = 30 * 8; // 240 hours
        double waterGoal = 30 * 2.4; // 72 Liters

        textViewAvgSleep.setText(String.format("%.1f / %.1f Hrs", totalSleep, sleepGoal));
        textViewAvgWater.setText(String.format("%.1f / %.1f L", totalWater, waterGoal));

        // --- Feedback Logic ---
        StringBuilder feedback = new StringBuilder();
        if (totalSleep < (sleepGoal * 0.75)) {
            feedback.append("Your average sleep is quite low this month. Prioritizing a consistent sleep schedule could greatly improve your energy levels.\n\n");
        } else {
            feedback.append("Great job on maintaining a consistent sleep schedule this month!\n\n");
        }

        if (totalWater < (waterGoal * 0.75)) {
            feedback.append("Your water intake appears to be below the recommended goal. Try carrying a water bottle with you to make it easier to stay hydrated.");
        } else {
            feedback.append("Excellent work on staying hydrated this month! Keep it up.");
        }

        textViewFeedback.setText(feedback.toString());

        return view;
    }
}