package com.example.healthtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class ProfileFragment extends Fragment {

    private TextView textViewUserName, textViewUserEmail, textViewVersion;
    private SwitchMaterial switchDarkMode;
    private Button buttonLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewUserName = view.findViewById(R.id.textViewUserName);
        textViewUserEmail = view.findViewById(R.id.textViewUserEmail);
        textViewVersion = view.findViewById(R.id.textViewVersion);
        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        buttonLogout = view.findViewById(R.id.buttonLogout);

        // --- Load and Display User Info ---
        loadUserInfo();

        // --- Handle Dark Mode Toggle ---
        // First, check the saved mode and set the switch's state
        boolean isNightMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        switchDarkMode.setChecked(isNightMode);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Turn on Dark Mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                // Turn off Dark Mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // --- Handle Logout Button ---
        buttonLogout.setOnClickListener(v -> showLogoutConfirmationDialog());

        // --- App Version ---
        // In a real app, this is retrieved dynamically. For this project, hardcoding is fine.
        textViewVersion.setText("1.0.0");

        return view;
    }

    private void loadUserInfo() {
        // Retrieve the logged-in user's email from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "user@example.com");

        // Derive a username from the email
        String userName = email.split("@")[0];
        userName = userName.substring(0, 1).toUpperCase() + userName.substring(1);

        textViewUserName.setText(userName);
        textViewUserEmail.setText(email);
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    // Navigate back to the WelcomeActivity
                    Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                    // Clear all previous activities from the stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}