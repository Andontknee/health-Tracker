package com.example.healthtracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;

public class DashboardPagerAdapter extends FragmentStateAdapter {
    private ArrayList<DailyLog> allLogs;

    public DashboardPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<DailyLog> logs) {
        super(fragmentActivity);
        this.allLogs = logs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return RecentTabFragment.newInstance(allLogs);
            case 1:
                return MonthTabFragment.newInstance(allLogs);
            default:
                return new Fragment(); // Should not happen
        }
    }

    @Override
    public int getItemCount() {
        return 2; // We have two tabs
    }
}