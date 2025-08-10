package com.example.healthtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textViewWelcome;
    private BarChart barChart; // The chart view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewWelcome = findViewById(R.id.textViewWelcome);
        barChart = findViewById(R.id.barChart); // Get the chart from the layout

        loadWelcomeMessage();
        setupChart();
    }

    private void loadWelcomeMessage() {
        SharedPreferences prefs = getSharedPreferences("user_credentials", MODE_PRIVATE);
        String email = prefs.getString("email", "User");
        String userName = email.split("@")[0];
        userName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
        textViewWelcome.setText("Welcome, " + userName);
    }

    private void setupChart() {
        // --- 1. Create Data Entries ---
        ArrayList<BarEntry> entries = new ArrayList<>();
        // The data for our X-axis labels (dates)
        final ArrayList<String> xAxisLabels = new ArrayList<>();

        // Create the fake data. NOTE: For stacked bars, the Y value is a float array.
        // new BarEntry(x, new float[]{value1, value2})
        entries.add(new BarEntry(0, new float[]{2.5f, 8.0f})); // Oct 26
        xAxisLabels.add("Oct 26");

        entries.add(new BarEntry(1, new float[]{2.1f, 7.5f})); // Oct 25
        xAxisLabels.add("Oct 25");

        entries.add(new BarEntry(2, new float[]{1.8f, 8.5f})); // Oct 24
        xAxisLabels.add("Oct 24");

        entries.add(new BarEntry(3, new float[]{3.0f, 6.0f})); // Oct 23
        xAxisLabels.add("Oct 23");

        entries.add(new BarEntry(4, new float[]{2.2f, 7.0f})); // Oct 22
        xAxisLabels.add("Oct 22");

        // --- 2. Create a DataSet ---
        BarDataSet barDataSet = new BarDataSet(entries, ""); // Title is not needed for the dataset
        barDataSet.setDrawIcons(false);
        barDataSet.setDrawValues(true); // Show values on top of bars

        // Set colors for the stacks (water, sleep)
        barDataSet.setColors(
                ContextCompat.getColor(this, R.color.chart_water_blue),
                ContextCompat.getColor(this, R.color.chart_sleep_yellow)
        );

        // Set labels for the stacks in the legend
        barDataSet.setStackLabels(new String[]{"Water (L)", "Sleep (Hrs)"});

        // --- 3. Create BarData object and set it to the chart ---
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars

        // --- 4. Customize the Chart's Appearance ---
        configureChartAppearance(xAxisLabels);

        // --- 5. Refresh the chart ---
        barChart.invalidate();
    }

    private void configureChartAppearance(final ArrayList<String> xAxisLabels) {
        barChart.getDescription().setEnabled(false); // Hide the description label
        barChart.setDrawGridBackground(false); // Remove the background grid
        barChart.setExtraBottomOffset(30f);

        // --- Configure Legend ---
        Legend legend = barChart.getLegend(); // Get the legend object
        legend.setTextSize(14f);
        legend.setForm(Legend.LegendForm.SQUARE); // Make the color indicator a square for a cleaner look
        legend.setFormSize(10f);                  // Set the size of the color square
        legend.setXEntrySpace(20f);               // Increase space between legend entries (Water and Sleep)
        legend.setYOffset(30f);                   // Add vertical space below the X-axis, pushing the legend down

        // --- Configure X-Axis (Dates) ---
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.BLACK); // Set text color to black
        xAxis.setTextSize(12f);          // Set text size
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        // --- Configure Y-Axis (Values) ---
        barChart.getAxisRight().setEnabled(false); // Hide the right Y-axis
        barChart.getAxisLeft().setAxisMinimum(0f); // Start Y-axis at 0
        barChart.getAxisLeft().setTextSize(12f);   // Set text size
        barChart.getAxisLeft().setTextColor(Color.BLACK); // Set text color to black
    }
    }