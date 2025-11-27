package com.example.focusme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SessionCompleteActivity extends AppCompatActivity {

    private TextView tvAchievement;
    private Button btnContinueToBreak;
    private int focusTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("FocusMePrefs", MODE_PRIVATE);
        boolean darkThemeEnabled = prefs.getBoolean("darkTheme", false);
        if (darkThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_complete);
        focusTime = getIntent().getIntExtra("FOCUS_TIME", 25);
        tvAchievement = findViewById(R.id.tvAchievement);
        btnContinueToBreak = findViewById(R.id.btnContinueToBreak);
        tvAchievement.setText("You focused for " + focusTime + " minutes!");
        btnContinueToBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SessionCompleteActivity.this, BreakActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}