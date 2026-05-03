package com.android.mad.assignments;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvJoke;
    Button btnGetJoke;

    OkHttpClient client = new OkHttpClient(); // клиент (как fetch)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvJoke = findViewById(R.id.tvJoke);
        btnGetJoke = findViewById(R.id.btnGetJoke);

        btnGetJoke.setOnClickListener(v -> {
            getJokeFromApi();
        });
    }

    private void getJokeFromApi() {

        String url = "https://api.chucknorris.io/jokes/random";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(() -> {
                    tvJoke.setText("Ошибка: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {

                    String responseData = response.body().string();

                    try {
                        JSONObject json = new JSONObject(responseData);

                        String joke = json.getString("value");

                        runOnUiThread(() -> {
                            tvJoke.setText(joke);
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}