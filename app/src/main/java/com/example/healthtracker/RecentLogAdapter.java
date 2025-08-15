package com.example.healthtracker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecentLogAdapter extends RecyclerView.Adapter<RecentLogAdapter.ViewHolder> {
    private ArrayList<DailyLog> dailyLogs;

    public RecentLogAdapter(ArrayList<DailyLog> dailyLogs) {
        this.dailyLogs = dailyLogs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recent_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyLog log = dailyLogs.get(position);

        holder.textViewDate.setText(log.getDate());

        // --- Sleep Logic ---
        double sleepHours = log.getSleepHours();
        holder.textViewSleepValue.setText(String.format("%.1f Hrs", sleepHours));
        holder.progressBarSleep.setMax(10); // Target 10 hours max
        holder.progressBarSleep.setProgress((int) sleepHours);
        if (sleepHours < 6) {
            holder.textViewSleepStatus.setText("Poor");
            holder.textViewSleepStatus.setTextColor(Color.RED);
        } else if (sleepHours <= 7.5) {
            holder.textViewSleepStatus.setText("Average");
            holder.textViewSleepStatus.setTextColor(Color.parseColor("#FFA500")); // Orange
        } else {
            holder.textViewSleepStatus.setText("Good");
            holder.textViewSleepStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
        }

        // --- Water Logic ---
        double waterLiters = log.getWaterLiters();
        holder.textViewWaterValue.setText(String.format("%.1f L", waterLiters));
        holder.progressBarWater.setMax(4); // Target 4 Liters max
        holder.progressBarWater.setProgress((int) waterLiters);
        if (waterLiters < 2.0) {
            holder.textViewWaterStatus.setText("Poor");
            holder.textViewWaterStatus.setTextColor(Color.RED);
        } else if (waterLiters < 3.0) {
            holder.textViewWaterStatus.setText("Average");
            holder.textViewWaterStatus.setTextColor(Color.parseColor("#FFA500")); // Orange
        } else {
            holder.textViewWaterStatus.setText("Good");
            holder.textViewWaterStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
        }

        // --- Note Logic ---
        if (log.getNote() != null && !log.getNote().isEmpty()) {
            holder.textViewNote.setText("Note: " + log.getNote());
            holder.textViewNote.setVisibility(View.VISIBLE);
        } else {
            holder.textViewNote.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dailyLogs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewSleepValue, textViewSleepStatus, textViewWaterValue, textViewWaterStatus, textViewNote;
        ProgressBar progressBarSleep, progressBarWater;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewSleepValue = itemView.findViewById(R.id.textViewSleepValue);
            textViewSleepStatus = itemView.findViewById(R.id.textViewSleepStatus);
            textViewWaterValue = itemView.findViewById(R.id.textViewWaterValue);
            textViewWaterStatus = itemView.findViewById(R.id.textViewWaterStatus);
            textViewNote = itemView.findViewById(R.id.textViewNote);
            progressBarSleep = itemView.findViewById(R.id.progressBarSleep);
            progressBarWater = itemView.findViewById(R.id.progressBarWater);
        }
    }
}