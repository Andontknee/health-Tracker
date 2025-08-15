package com.example.healthtracker;

public class DailyLog {
    private String date;
    private double sleepHours;
    private double waterLiters;
    private String note;

    public DailyLog(String date, double sleepHours, double waterLiters, String note) {
        this.date = date;
        this.sleepHours = sleepHours;
        this.waterLiters = waterLiters;
        this.note = note;
    }

    // Getters for all fields
    public String getDate() { return date; }
    public double getSleepHours() { return sleepHours; }
    public double getWaterLiters() { return waterLiters; }
    public String getNote() { return note; }
}