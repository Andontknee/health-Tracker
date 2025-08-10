package com.example.healthtracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    // Regex for password validation (at least one uppercase, one special character)
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[A-Z])" +         // at least 1 upper case letter
                    "(?=.*[!@#$%^&*+=])" +  // at least 1 special character
                    ".{1,}" +               // any character, at least 1
                    "$");

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextConfirmPassword;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        // Set the click listener for the Sign Up button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        // Get the input text and remove leading/trailing whitespace
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // --- VALIDATION ---
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            editTextPassword.setError("Password must contain an uppercase letter and a special character");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            editTextConfirmPassword.requestFocus();
            return;
        }

        // --- If all validation passes, save the user data ---
        // SECURITY NOTE: In a real-world app, you should NEVER store passwords in plain text.
        // You should hash them first. For this student project, this is acceptable.

        // Use SharedPreferences to store the credentials.
        // "user_credentials" is the name of our mini-database.
        // MODE_PRIVATE means only this app can access this data.
        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save the email and password using key-value pairs.
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply(); // Use apply() to save the data in the background.

        // Give feedback to the user and close the activity
        Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show();
        finish(); // Closes the SignUpActivity and returns to the previous screen (WelcomeActivity)
    }
}