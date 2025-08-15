package com.example.healthtracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecentTabFragment extends Fragment {
    // This will hold the data passed from DashboardFragment
    private ArrayList<DailyLog> dailyLogs = new ArrayList<>();

    // We use newInstance pattern to pass data to fragments safely
    public static RecentTabFragment newInstance(ArrayList<DailyLog> logs) {
        RecentTabFragment fragment = new RecentTabFragment();
        Bundle args = new Bundle();
        // Note: DailyLog needs to be Serializable to be passed like this.
        // We will implement this in the next step.
        // args.putSerializable("logs", logs);
        fragment.setArguments(args);
        fragment.dailyLogs = logs; // Direct passing for simplicity now
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_tab, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRecent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // We only show the last 7 days here
        ArrayList<DailyLog> recentLogs = new ArrayList<>();
        if (dailyLogs.size() > 7) {
            recentLogs.addAll(dailyLogs.subList(0, 7));
        } else {
            recentLogs.addAll(dailyLogs);
        }

        RecentLogAdapter adapter = new RecentLogAdapter(recentLogs);
        recyclerView.setAdapter(adapter);

        return view;
    }
}