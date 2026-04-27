package com.android.mad.assignments;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        Notification notification =
                new NotificationCompat.Builder(this, "channel_id")
                        .setContentTitle("Foreground Service")
                        .setContentText("Service is running...")
                        .setSmallIcon(android.R.drawable.ic_media_play)
                        .build();

        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(() -> {

            for (int i = 1; i <= 5; i++) {

                Log.d("MyService", "Работает сервис: " + i);

                SystemClock.sleep(1000);
            }

            stopSelf();

        }).start();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}