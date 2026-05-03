package com.android.mad.assignments;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mad.assignments.api.ApiService;
import com.android.mad.assignments.api.RetrofitClient;
import com.android.mad.assignments.model.GeoResponse;
import com.android.mad.assignments.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText etCity;
    Button btnGetWeather;
    TextView tvResult;

    ApiService geoService;
    ApiService weatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        btnGetWeather = findViewById(R.id.btnGetWeather);
        tvResult = findViewById(R.id.tvResult);

        geoService = RetrofitClient.getGeoClient().create(ApiService.class);
        weatherService = RetrofitClient.getWeatherClient().create(ApiService.class);

        btnGetWeather.setOnClickListener(v -> {
            String city = etCity.getText().toString();
            getCoordinates(city);
        });
    }

    private void getCoordinates(String city) {

        geoService.getCoordinates(city).enqueue(new Callback<GeoResponse>() {

            @Override
            public void onResponse(Call<GeoResponse> call, Response<GeoResponse> response) {

                if (response.isSuccessful() && response.body().results != null) {

                    double lat = response.body().results.get(0).latitude;
                    double lon = response.body().results.get(0).longitude;

                    getWeather(lat, lon);
                } else {
                    tvResult.setText("Город не найден");
                }
            }

            @Override
            public void onFailure(Call<GeoResponse> call, Throwable t) {
                tvResult.setText("Ошибка: " + t.getMessage());
            }
        });
    }

    private void getWeather(double lat, double lon) {

        weatherService.getWeather(lat, lon, true)
                .enqueue(new Callback<WeatherResponse>() {

                    @Override
                    public void onResponse(Call<WeatherResponse> call,
                                           Response<WeatherResponse> response) {

                        if (response.isSuccessful()) {

                            double temp = response.body().current_weather.temperature;
                            double wind = response.body().current_weather.windspeed;

                            tvResult.setText(
                                    "Температура: " + temp + "°C\n" +
                                            "Ветер: " + wind + " м/с"
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        tvResult.setText("Ошибка: " + t.getMessage());
                    }
                });
    }
}