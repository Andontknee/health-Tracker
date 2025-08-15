package com.example.healthtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView textViewWelcome, textViewYourWater, textViewGoalWater, textViewYourSleep, textViewGoalSleep;
    private BarChart barChart;
    private Button buttonEditJournal;
    private TextView textViewEmptyChart; // To show when there is no data

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textViewWelcome = view.findViewById(R.id.textViewWelcome);
        barChart = view.findViewById(R.id.barChart);
        textViewYourWater = view.findViewById(R.id.textViewYourWater);
        textViewGoalWater = view.findViewById(R.id.textViewGoalWater);
        textViewYourSleep = view.findViewById(R.id.textViewYourSleep);
        textViewGoalSleep = view.findViewById(R.id.textViewGoalSleep);
        buttonEditJournal = view.findViewById(R.id.buttonEditJournal);

        // Find the empty state TextView - you will need to add this to fragment_home.xml
        // For now, we will handle its visibility programmatically. Let's assume you've added an ID for it.
        // As a fallback, we can use a Toast message.

        loadWelcomeMessage();

        buttonEditJournal.setOnClickListener(v -> {
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.nav_journal);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the chart and summary every time the user comes to this screen.
        setupChartAndSummary();
    }

    private void loadWelcomeMessage() {
        // ... (this method remains the same) ...
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "User");
        String userName = email.split("@")[0];
        userName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
        textViewWelcome.setText("Welcome, " + userName);
    }

    private void setupChartAndSummary() {
        // Get the live data from JournalFragment
        ArrayList<DailyLog> liveLogs = JournalFragment.allLogs;

        if (liveLogs.isEmpty() || liveLogs.size() == 1 && liveLogs.get(0).getNote().contains("Welcome")) {
            barChart.setVisibility(View.GONE);
            // Here you would make a "No data to display" TextView visible
            Toast.makeText(getContext(), "No journal entries yet. Add one!", Toast.LENGTH_SHORT).show();
            updateSummaryCard(new ArrayList<>()); // Update with zero values
            return;
        }

        barChart.setVisibility(View.VISIBLE);

        // Sort logs to get the most recent ones for the chart
        Collections.sort(liveLogs, (log1, log2) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                return sdf.parse(log2.getDate()).compareTo(sdf.parse(log1.getDate()));
            } catch (Exception e) {
                return 0;
            }
        });

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> xAxisLabels = new ArrayList<>();

        // Get up to the last 5 entries for the chart
        int count = Math.min(liveLogs.size(), 5);
        for (int i = 0; i < count; i++) {
            DailyLog log = liveLogs.get(i);
            entries.add(new BarEntry(i, new float[]{(float) log.getWaterLiters(), (float) log.getSleepHours()}));
            // Format date for X-axis label
            xAxisLabels.add(log.getDate().substring(0, log.getDate().indexOf(",")));
        }

        // The chart requires data to be in ascending order for X-axis (0, 1, 2...)
        // so we reverse the lists after creating them from descending sorted logs.
        Collections.reverse(entries);
        Collections.reverse(xAxisLabels);

        updateSummaryCard(liveLogs);

        // ... (The rest of the chart setup code remains the same as before) ...
        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setValueTextSize(10f);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setDrawValues(true);
        barDataSet.setColors(
                ContextCompat.getColor(requireContext(), R.color.chart_water_blue),
                ContextCompat.getColor(requireContext(), R.color.chart_sleep_yellow)
        );
        barDataSet.setStackLabels(new String[]{"Water (L)", "Sleep (Hrs)"});
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.setFitBars(true);
        configureChartAppearance(xAxisLabels);
        barChart.invalidate();
    }

    private void updateSummaryCard(ArrayList<DailyLog> logsForSummary) {
        // Calculate totals from the last 7 days only
        double totalWater = 0;
        double totalSleep = 0;
        int count = Math.min(logsForSummary.size(), 7);

        for(int i = 0; i < count; i++) {
            totalWater += logsForSummary.get(i).getWaterLiters();
            totalSleep += logsForSummary.get(i).getSleepHours();
        }

        textViewYourWater.setText(String.format("Your: %.1f L", totalWater));
        textViewYourSleep.setText(String.format("Your: %.1f Hrs", totalSleep));
        textViewGoalWater.setText("Goal: 23.6 L");
        textViewGoalSleep.setText("Goal: 56.0 Hrs");
    }

    private void configureChartAppearance(final ArrayList<String> xAxisLabels) {
        // ... (this method remains the same) ...
        barChart.setExtraBottomOffset(30f);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        Legend legend = barChart.getLegend();
        legend.setTextSize(14f);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(10f);
        legend.setXEntrySpace(20f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisLeft().setTextSize(12f);
        barChart.getAxisLeft().setTextColor(Color.BLACK);
    }
}