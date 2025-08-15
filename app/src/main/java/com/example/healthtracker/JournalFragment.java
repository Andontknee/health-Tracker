package com.example.healthtracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;


import com.example.healthtracker.DataStorageHelper;

public class JournalFragment extends Fragment {

    public static ArrayList<DailyLog> allLogs = new ArrayList<>();
    private static boolean isDataLoaded = false;

    private RecyclerView recyclerViewJournal;
    private JournalAdapter journalAdapter;
    private FloatingActionButton fabAddEntry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        if (!isDataLoaded) {
            allLogs = DataStorageHelper.loadLogs(requireContext());
            if (allLogs.isEmpty()) {
                allLogs.add(new DailyLog("October 26, 2025", 8.0, 2.5, "Welcome! Add your first entry."));
                // Also save this initial log so it doesn't reappear every time
                DataStorageHelper.saveLogs(requireContext(), allLogs);
            }
            isDataLoaded = true;
        }

        recyclerViewJournal = view.findViewById(R.id.recyclerViewJournal);
        fabAddEntry = view.findViewById(R.id.fabAddEntry);

        setupRecyclerView();

        fabAddEntry.setOnClickListener(v -> showAddEditDialog(null, -1));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshJournalList();
    }

    private void setupRecyclerView() {
        recyclerViewJournal.setLayoutManager(new LinearLayoutManager(getContext()));
        journalAdapter = new JournalAdapter(allLogs, position -> {
            showAddEditDialog(allLogs.get(position), position);
        });
        recyclerViewJournal.setAdapter(journalAdapter);
    }

    private void refreshJournalList() {
        allLogs.sort((log1, log2) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                return sdf.parse(log2.getDate()).compareTo(sdf.parse(log1.getDate()));
            } catch (Exception e) {
                return 0;
            }
        });

        if (journalAdapter != null) {
            journalAdapter.notifyDataSetChanged();
        }
    }

    private void showAddEditDialog(final DailyLog logToEdit, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_entry, null);

        TextView tvTitle = dialogView.findViewById(R.id.textViewDialogTitle);
        Button btnDate = dialogView.findViewById(R.id.buttonSelectDate);
        TextInputEditText etSleep = dialogView.findViewById(R.id.editTextSleepHours);
        TextInputEditText etWater = dialogView.findViewById(R.id.editTextWaterLiters);
        TextInputEditText etNote = dialogView.findViewById(R.id.editTextNote);

        final Calendar selectedDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);

        if (logToEdit != null) {
            tvTitle.setText("Edit Entry");
            etSleep.setText(String.valueOf(logToEdit.getSleepHours()));
            etWater.setText(String.valueOf(logToEdit.getWaterLiters()));
            etNote.setText(logToEdit.getNote());
            btnDate.setText(logToEdit.getDate());
            try {
                selectedDate.setTime(dateFormat.parse(logToEdit.getDate()));
            } catch (Exception e) {}
            builder.setNeutralButton("Delete", (dialog, id) -> showDeleteConfirmation(position));
        } else {
            tvTitle.setText("Add New Entry");
            btnDate.setText(dateFormat.format(selectedDate.getTime()));
        }

        btnDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year, month, dayOfMonth) -> {
                        selectedDate.set(year, month, dayOfMonth);
                        btnDate.setText(dateFormat.format(selectedDate.getTime()));
                    },
                    selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        builder.setView(dialogView)
                .setPositiveButton(logToEdit == null ? "Add" : "Save", (dialog, id) -> {
                    try {
                        String sleepStr = etSleep.getText().toString();
                        String waterStr = etWater.getText().toString();
                        double sleep = sleepStr.isEmpty() ? 0.0 : Double.parseDouble(sleepStr);
                        double water = waterStr.isEmpty() ? 0.0 : Double.parseDouble(waterStr);
                        String note = etNote.getText().toString();
                        String date = btnDate.getText().toString();

                        DailyLog newLog = new DailyLog(date, sleep, water, note);

                        if (position == -1) {
                            allLogs.add(newLog);
                        } else {
                            allLogs.set(position, newLog);
                        }

                        DataStorageHelper.saveLogs(requireContext(), allLogs);
                        refreshJournalList();

                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Please enter valid numbers.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        builder.create().show();
    }

    private void showDeleteConfirmation(final int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this log?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    allLogs.remove(position);
                    DataStorageHelper.saveLogs(requireContext(), allLogs);
                    refreshJournalList();
                    Toast.makeText(getContext(), "Entry deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}