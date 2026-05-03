package com.android.mad.assignments.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static Retrofit getGeoClient() {
        return new Retrofit.Builder()
                .baseUrl("https://geocoding-api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getWeatherClient() {
        return new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}