package com.example.event_retrofit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.event_retrofit.activity.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    String CHANNEL_ID = "Chanel EVENT";
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Override
    public void onReceive(Context context, Intent intent) {

        String message = intent.getStringExtra("todo");

        Intent actionIntent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, actionIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ok_emoji)
                .setLargeIcon(BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.ok_emoji
                ))
                .setContentTitle("Event!")
                .setContentText(message)
                .setVibrate(new long[]{500, 500, 500, 500, 500, 500, 500, 500, 500})
                .setPriority(2)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(alarmSound)
                .addAction(R.drawable.ok_emoji, "action",
                        pendingIntent);
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))

        createNotificationChannel(context);
        NotificationManager notificationManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            notificationManager = context.getSystemService(NotificationManager.class);
        }
        int notificationID = 12324;
        notificationManager.notify(notificationID, builder.build());

    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Chanel Name";
            String description = "Chanel Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
