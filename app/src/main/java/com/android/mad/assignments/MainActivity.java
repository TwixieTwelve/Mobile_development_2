package com.android.mad.assignments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    Switch themeSwitch;
    Button btnStartService, btnStopService, btnGetData;

    BoundService boundService;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            BoundService.LocalBinder binder =
                    (BoundService.LocalBinder) service;

            boundService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            boundService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs =
                getSharedPreferences("settings", MODE_PRIVATE);

        boolean isDarkMode =
                prefs.getBoolean("dark_mode", false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        themeSwitch = findViewById(R.id.themeSwitch);
        btnStartService = findViewById(R.id.btnStartService);
        btnStopService = findViewById(R.id.btnStopService);
        btnGetData = findViewById(R.id.btnGetData);

        themeSwitch.setChecked(isDarkMode);

        themeSwitch.setOnCheckedChangeListener((buttonView, checked) -> {

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_mode", checked);
            editor.apply();

            if (checked) {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
            }

            recreate();
        });

        btnStartService.setOnClickListener(v -> {
            Intent intent =
                    new Intent(this, MyService.class);
            startService(intent);

            Toast.makeText(this,
                    "Service запущен",
                    Toast.LENGTH_SHORT).show();
        });

        btnStopService.setOnClickListener(v -> {
            Intent intent =
                    new Intent(this, MyService.class);
            stopService(intent);

            Toast.makeText(this,
                    "Service остановлен",
                    Toast.LENGTH_SHORT).show();
        });

        btnGetData.setOnClickListener(v -> {

            if (boundService != null) {

                String data =
                        boundService.getData();

                Toast.makeText(this,
                        data,
                        Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent =
                new Intent(this, BoundService.class);

        bindService(intent,
                connection,
                Context.BIND_AUTO_CREATE);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            "channel_id",
                            "Service Channel",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(connection);
    }
}