package com.example.focusme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class FocusTaskActivity extends AppCompatActivity {

    private TextView InputLabel;
    private EditText etFocusTask;
    private Button btnStartFocusing;
    private TextView tvQuote;
    private int selectedTime;

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
        setContentView(R.layout.task_input);
        selectedTime = getIntent().getIntExtra("SELECTED_TIME", 25);
        InputLabel =findViewById(R.id.tvInputLabel);
        etFocusTask = findViewById(R.id.etFocusTask);
        btnStartFocusing = findViewById(R.id.btnStartFocusing);
        tvQuote = findViewById(R.id.tvQuote);
        tvQuote.setText(QuotesManager.getRandomMotivationalQuote());

        btnStartFocusing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = etFocusTask.getText().toString().trim();
                if (task.isEmpty()) {
                    etFocusTask.setError("Please enter a focus task");
                    etFocusTask.requestFocus();
                    return;
                }
                if (task.length() < 3) {
                    etFocusTask.setError("Task should be at least 3 characters");
                    etFocusTask.requestFocus();
                    Toast.makeText(FocusTaskActivity.this,
                            "Please enter a more descriptive task",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(FocusTaskActivity.this, FocusingActivity.class);
                intent.putExtra("TASK_NAME", task);
                intent.putExtra("FOCUS_TIME", selectedTime);
                startActivity(intent);
            }
        });

        etFocusTask.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etFocusTask.setError(null);
                }
            }
        });
    }
}