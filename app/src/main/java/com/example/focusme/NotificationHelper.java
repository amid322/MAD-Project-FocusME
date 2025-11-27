package com.example.focusme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "focusme_channel";
    private NotificationManager notificationManager;
    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "FocusME Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Focus session and break notifications");
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void showFocusStarted(String taskName, int duration) {
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                    .setContentTitle("🚀 Focus Session Started")
                    .setContentText("Focusing on: " + taskName)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            notificationManager.notify(1, builder.build());
        } catch (Exception e) {

        }
    }

    public void showFocusCompleted(int duration) {
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("🎉 Focus Session Complete!")
                    .setContentText("Great job! You focused for " + duration + " minutes")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            notificationManager.notify(2, builder.build());
        } catch (Exception e) {

        }
    }

    public void showBreakStarted() {
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_notification_overlay)
                    .setContentTitle("☕ Break Time!")
                    .setContentText("Take a 5-minute break to recharge")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            notificationManager.notify(3, builder.build());
        } catch (Exception e) {

        }
    }

    public void cancelAll() {
        try {
            notificationManager.cancelAll();
        } catch (Exception e) {

        }
    }
}