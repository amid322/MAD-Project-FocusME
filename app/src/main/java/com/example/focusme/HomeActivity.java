package com.example.focusme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView tvProgressPercent;
    private Button tvRecentSessions, btnStartNewSession, btnSetDailyGoalHome;

    private SharedPreferences sharedPreferences;
    private int sessionsCompleted = 0;
    private int dailyGoal = 4;
    private Button btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("FocusMePrefs", MODE_PRIVATE);
        boolean darkThemeEnabled = prefs.getBoolean("darkTheme", false);

        if (darkThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_dashboard);

        sharedPreferences = getSharedPreferences("FocusMePrefs", MODE_PRIVATE);

        sessionsCompleted = sharedPreferences.getInt("sessionsCompleted", 0);
        dailyGoal = sharedPreferences.getInt("dailyGoal", 4);


        tvProgressPercent = findViewById(R.id.tvProgressPercent);
        tvRecentSessions = findViewById(R.id.tvRecentSessions);
        btnStartNewSession = findViewById(R.id.btnStartNewSession);
        btnSetDailyGoalHome = findViewById(R.id.btnSetDailyGoalHome);

        updateProgress();

        tvRecentSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecentSessions();
            }
        });

        btnStartNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FocusTimeActivity.class);
                startActivity(intent);
            }
        });

        btnSetDailyGoalHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDailyGoal();
            }
        });
        btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateProgress() {
        int progressPercentage = 0;
        if (dailyGoal > 0) {
            progressPercentage = (sessionsCompleted * 100) / dailyGoal;
        }

        if (progressPercentage > 100) {
            progressPercentage = 100;
        }

        tvProgressPercent.setText(progressPercentage + "%\ncompleted");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("sessionsCompleted", sessionsCompleted);
        editor.apply();
    }

    private void showRecentSessions() {
        // Get session history
        int totalSessions = sharedPreferences.getInt("sessionsCompleted", 0);
        String lastSessionTime = sharedPreferences.getString("lastSessionTime", "No sessions yet");

        String message = "Total Sessions: " + totalSessions + "\n" +
                "Today's Goal: " + dailyGoal + " sessions\n" +
                "Last Session: " + lastSessionTime + "\n" +
                "Completed: " + sessionsCompleted + "/" + dailyGoal;

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Session History")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void setDailyGoal() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Set Daily Goal");

        // Create input field
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(dailyGoal));
        builder.setView(input);

        builder.setPositiveButton("Set", (dialog, which) -> {
            String goalText = input.getText().toString();
            if (!goalText.isEmpty()) {
                dailyGoal = Integer.parseInt(goalText);
                if (dailyGoal < 1) dailyGoal = 1;
                if (dailyGoal > 20) dailyGoal = 20;

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("dailyGoal", dailyGoal);
                editor.apply();

                updateProgress();
                android.widget.Toast.makeText(HomeActivity.this,
                        "Daily goal set to " + dailyGoal + " sessions",
                        android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if we're returning from a completed session
        boolean sessionCompleted = sharedPreferences.getBoolean("sessionCompleted", false);
        if (sessionCompleted) {
            // Increment session count
            sessionsCompleted++;

            // Save session time
            String currentTime = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(new Date());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lastSessionTime", currentTime);
            editor.putBoolean("sessionCompleted", false);
            editor.apply();

            updateProgress();

            if (sessionsCompleted <= dailyGoal) {
                android.widget.Toast.makeText(this,
                        "Great! " + sessionsCompleted + "/" + dailyGoal + " sessions completed today!",
                        android.widget.Toast.LENGTH_LONG).show();
            }
        }

        dailyGoal = sharedPreferences.getInt("dailyGoal", 4);
        updateProgress();
    }
}