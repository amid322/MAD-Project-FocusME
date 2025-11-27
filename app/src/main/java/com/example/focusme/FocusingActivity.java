package com.example.focusme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class FocusingActivity extends AppCompatActivity {

    private TextView tvFocusingOn, tvTimer;
    private Button btnPauseResume, btnStopFocus;

    private CountDownTimer focusTimer;
    private long timeLeftInMillis;
    private boolean timerRunning;
    private String taskName;
    private int focusTimeMinutes;

    private NotificationHelper notificationHelper;
    private boolean notificationsEnabled;

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
        setContentView(R.layout.active_focus_session);

        SharedPreferences sharedPreferences = getSharedPreferences("FocusMePrefs", MODE_PRIVATE);
        notificationsEnabled = sharedPreferences.getBoolean("notifications", true);
        notificationHelper = new NotificationHelper(this);
        taskName = getIntent().getStringExtra("TASK_NAME");
        focusTimeMinutes = getIntent().getIntExtra("FOCUS_TIME", 25);
        timeLeftInMillis = focusTimeMinutes * 60 * 1000L;

        tvFocusingOn = findViewById(R.id.tvFocusingOn);
        tvTimer = findViewById(R.id.tvTimer);
        btnPauseResume = findViewById(R.id.btnPauseResume);
        btnStopFocus = findViewById(R.id.btnStopFocus);

        tvFocusingOn.setText("Focusing on: " + taskName);
        if (notificationsEnabled) {
            notificationHelper.showFocusStarted(taskName, focusTimeMinutes);
        }
        playStartSound();
        vibrate(400);

        startTimer();

        btnPauseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    resumeTimer();
                }
            }
        });

        btnStopFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                notificationHelper.cancelAll();
                Intent intent = new Intent(FocusingActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void startTimer() {
        focusTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                if (notificationsEnabled) {
                    notificationHelper.showFocusCompleted(focusTimeMinutes);
                }
                playCompleteSound();
                vibrate(500);

                SharedPreferences sharedPreferences = getSharedPreferences("FocusMePrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("sessionCompleted", true);
                String currentTime = new java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault()).format(new java.util.Date());
                editor.putString("lastSessionTime", currentTime);
                int totalSessions = sharedPreferences.getInt("totalSessions", 0);
                editor.putInt("totalSessions", totalSessions + 1);

                editor.apply();
                Intent intent = new Intent(FocusingActivity.this, SessionCompleteActivity.class);
                intent.putExtra("FOCUS_TIME", focusTimeMinutes);
                startActivity(intent);
                finish();
            }
        }.start();

        timerRunning = true;
        btnPauseResume.setText("Pause");
    }

    private void pauseTimer() {
        if (focusTimer != null) {
            focusTimer.cancel();
        }
        timerRunning = false;
        btnPauseResume.setText("Resume");
    }

    private void resumeTimer() {
        startTimer();
    }

    private void stopTimer() {
        if (focusTimer != null) {
            focusTimer.cancel();
        }
        timerRunning = false;
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText(timeLeftFormatted);
    }
    private void playStartSound() {
        try {
            android.media.ToneGenerator tg = new android.media.ToneGenerator(
                    android.media.AudioManager.STREAM_NOTIFICATION, 200);
            tg.startTone(android.media.ToneGenerator.TONE_PROP_BEEP2, 300);
        } catch (Exception e) {

        }
    }

    private void playCompleteSound() {
        try {
            android.media.ToneGenerator tg = new android.media.ToneGenerator(
                    android.media.AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(android.media.ToneGenerator.TONE_PROP_ACK, 500);
        } catch (Exception e) {

        }
    }

    private void vibrate(long milliseconds) {
        try {
            android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(android.os.VibrationEffect.createOneShot(milliseconds,
                            android.os.VibrationEffect.DEFAULT_AMPLITUDE));
                } else {

                    vibrator.vibrate(milliseconds);
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (focusTimer != null) {
            focusTimer.cancel();
        }

        notificationHelper.cancelAll();
    }
}