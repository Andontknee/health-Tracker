package com.example.healthtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {
    private ArrayList<DailyLog> logList;
    private OnItemEditListener listener;

    // Interface to handle click events
    public interface OnItemEditListener {
        void onEditClick(int position);
    }

    public JournalAdapter(ArrayList<DailyLog> logList, OnItemEditListener listener) {
        this.logList = logList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_journal_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyLog log = logList.get(position);

        holder.textViewDate.setText(log.getDate());
        holder.textViewSleep.setText("Sleep: " + log.getSleepHours() + " Hrs");
        holder.textViewWater.setText("Water: " + log.getWaterLiters() + " L");

        if (log.getNote() != null && !log.getNote().isEmpty()) {
            holder.textViewNote.setText("Note: " + log.getNote());
            holder.textViewNote.setVisibility(View.VISIBLE);
        } else {
            holder.textViewNote.setVisibility(View.GONE);
        }

        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewSleep, textViewWater, textViewNote;
        ImageButton buttonEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewSleep = itemView.findViewById(R.id.textViewSleep);
            textViewWater = itemView.findViewById(R.id.textViewWater);
            textViewNote = itemView.findViewById(R.id.textViewNote);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }
    }
}