package com.example.healthtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class HealthEntryAdapter extends ArrayAdapter<HealthEntry> {

    public HealthEntryAdapter(Context context, ArrayList<HealthEntry> entries) {
        super(context, 0, entries);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        HealthEntry entry = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_health_entry, parent, false);
        }

        // Lookup view for data population
        ImageView imageViewIcon = convertView.findViewById(R.id.imageViewIcon);
        TextView textViewValue = convertView.findViewById(R.id.textViewValue);
        TextView textViewDate = convertView.findViewById(R.id.textViewDate);

        // Populate the data into the template view using the data object
        imageViewIcon.setImageResource(entry.getIconResourceId());
        textViewValue.setText(entry.getEntryValue());
        textViewDate.setText(entry.getEntryDate());

        // Return the completed view to render on screen
        return convertView;
    }
}