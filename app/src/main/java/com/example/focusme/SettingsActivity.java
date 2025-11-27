package com.example.focusme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchNotifications, switchTheme;
    private Button btnSaveSettings, btnAbout; 
    private SharedPreferences sharedPreferences;
    private boolean notificationsEnabled, darkThemeEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        sharedPreferences = getSharedPreferences("FocusMePrefs", MODE_PRIVATE);
        notificationsEnabled = sharedPreferences.getBoolean("notifications", true);
        darkThemeEnabled = sharedPreferences.getBoolean("darkTheme", false);

        switchNotifications = findViewById(R.id.switchNotifications);
        switchTheme = findViewById(R.id.switchTheme);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);
        btnAbout = findViewById(R.id.btnAbout);
        switchNotifications.setChecked(notificationsEnabled);
        switchTheme.setChecked(darkThemeEnabled);
        applyTheme(darkThemeEnabled);
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notificationsEnabled = isChecked;
            }
        });

        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                darkThemeEnabled = isChecked;
            }
        });

        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notifications", notificationsEnabled);
        editor.putBoolean("darkTheme", darkThemeEnabled);
        editor.apply();

        if (darkThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        android.widget.Toast.makeText(this, "Settings saved successfully!", android.widget.Toast.LENGTH_SHORT).show();
        finish();
    }

    private void applyTheme(boolean darkTheme) {
        if (darkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}