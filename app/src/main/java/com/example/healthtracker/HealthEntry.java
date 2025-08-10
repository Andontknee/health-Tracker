package com.example.healthtracker;

public class HealthEntry {
    private String entryValue;
    private String entryDate;
    private int iconResourceId;

    // Constructor
    public HealthEntry(String entryValue, String entryDate, int iconResourceId) {
        this.entryValue = entryValue;
        this.entryDate = entryDate;
        this.iconResourceId = iconResourceId;
    }

    // Getters
    public String getEntryValue() {
        return entryValue;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }
}