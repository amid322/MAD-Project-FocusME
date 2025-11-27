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

public class BreakActivity extends AppCompatActivity {

    private TextView tvBreakTimer;
    private Button btnSkipBreak, btnStartFocus;

    private CountDownTimer breakTimer;
    private long breakTimeLeftInMillis = 15 * 60 * 1000L; // 15 minutes
    private boolean breakFinished = false;
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
        }        super.onCreate(savedInstanceState);
        setContentView(R.layout.break_screen);
        SharedPreferences sharedPreferences = getSharedPreferences("FocusMePrefs", MODE_PRIVATE);
        notificationsEnabled = sharedPreferences.getBoolean("notifications", true);
        notificationHelper = new NotificationHelper(this);

        if (notificationsEnabled) {
            notificationHelper.showBreakStarted();
        }

        playBreakSound();
        vibrate(150);


        tvBreakTimer = findViewById(R.id.tvBreakTimer);
        btnSkipBreak = findViewById(R.id.btnSkipBreak);
        btnStartFocus = findViewById(R.id.btnStartFocus);
        btnSkipBreak.setEnabled(true);
        btnStartFocus.setEnabled(true);
        startBreakTimer();
        btnSkipBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (breakTimer != null) {
                    breakTimer.cancel();
                }
                notificationHelper.cancelAll();
                Intent intent = new Intent(BreakActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnStartFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (breakTimer != null) {
                    breakTimer.cancel();
                }

                notificationHelper.cancelAll();
                Intent intent = new Intent(BreakActivity.this, FocusTimeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void startBreakTimer() {
        breakTimer = new CountDownTimer(breakTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                breakTimeLeftInMillis = millisUntilFinished;
                updateBreakTimerText();
            }

            @Override
            public void onFinish() {
                breakFinished = true;
                tvBreakTimer.setText("Break Over!");

                notificationHelper.cancelAll();

                playBreakCompleteSound();
                vibrate(300);


                android.widget.Toast.makeText(BreakActivity.this,
                        "Break time completed! Ready to focus?",
                        android.widget.Toast.LENGTH_SHORT).show();


                // Intent intent = new Intent(BreakActivity.this, HomeActivity.class);
                // startActivity(intent);
                // finish();
            }
        }.start();
    }

    private void updateBreakTimerText() {
        int minutes = (int) (breakTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (breakTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        tvBreakTimer.setText(timeLeftFormatted);
    }

    private void playBreakSound() {
        try {
            android.media.ToneGenerator tg = new android.media.ToneGenerator(
                    android.media.AudioManager.STREAM_NOTIFICATION, 80);
            tg.startTone(android.media.ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300);
        } catch (Exception e) {

        }
    }

    private void playBreakCompleteSound() {
        try {
            android.media.ToneGenerator tg = new android.media.ToneGenerator(
                    android.media.AudioManager.STREAM_NOTIFICATION, 80);
            tg.startTone(android.media.ToneGenerator.TONE_PROP_BEEP, 400);
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
        if (breakTimer != null) {
            breakTimer.cancel();
        }
        notificationHelper.cancelAll();
    }
}