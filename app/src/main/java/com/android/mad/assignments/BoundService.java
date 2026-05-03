package com.android.mad.assignments;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BoundService extends Service {

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public BoundService getService() {
            return BoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // 🔥 ГЛАВНЫЙ МЕТОД — получаем новую фразу каждый раз
    public void getNewJoke(Callback callback) {

        new Thread(() -> {
            try {
                URL url = new URL("https://api.chucknorris.io/jokes/random");

                HttpURLConnection conn =
                        (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                JSONObject json = new JSONObject(response.toString());

                String joke = json.getString("value");

                callback.onResult(joke);

            } catch (Exception e) {
                e.printStackTrace();
                callback.onResult("Ошибка API 😢");
            }
        }).start();
    }

    // интерфейс для возврата данных (как Promise в вебе)
    public interface Callback {
        void onResult(String data);
    }
}