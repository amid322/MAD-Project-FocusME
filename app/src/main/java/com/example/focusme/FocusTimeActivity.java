package com.example.focusme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class FocusTimeActivity extends AppCompatActivity {

    private Button btnTime1, btnTime2, btnTime3, btnTime4, btnNext;
    private EditText etCustom;
    private int selectedTime = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("FocusMePrefs", MODE_PRIVATE);
        boolean darkThemeEnabled = prefs.getBoolean("darkTheme", false);
        if (darkThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_selection);
        btnTime1 = findViewById(R.id.btnTime1);
        btnTime2 = findViewById(R.id.btnTime2);
        btnTime3 = findViewById(R.id.btnTime3);
        btnTime4 = findViewById(R.id.btnTime4);
        btnNext = findViewById(R.id.btnNext);
        etCustom = findViewById(R.id.etCustom);
        btnTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTime = 25;
                navigateToTaskInput();
            }
        });

        btnTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTime = 30;
                navigateToTaskInput();
            }
        });

        btnTime3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTime = 45;
                navigateToTaskInput();
            }
        });

        btnTime4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTime = 60;
                navigateToTaskInput();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customTime = etCustom.getText().toString().trim();
                if (!customTime.isEmpty()) {
                    try {
                        selectedTime = Integer.parseInt(customTime);
                        if (selectedTime < 1) selectedTime = 1;
                        if (selectedTime > 120) selectedTime = 120;
                    } catch (NumberFormatException e) {
                        selectedTime = 25;
                    }
                }
                navigateToTaskInput();
            }
        });
    }

    private void navigateToTaskInput() {
        Intent intent = new Intent(FocusTimeActivity.this, FocusTaskActivity.class);
        intent.putExtra("SELECTED_TIME", selectedTime);
        startActivity(intent);
    }
}