package com.android.mad.assignments.api;

import com.android.mad.assignments.model.GeoResponse;
import com.android.mad.assignments.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    // 1. Получить координаты города
    @GET("v1/search")
    Call<GeoResponse> getCoordinates(
            @Query("name") String city
    );

    // 2. Получить погоду
    @GET("v1/forecast")
    Call<WeatherResponse> getWeather(
            @Query("latitude") double lat,
            @Query("longitude") double lon,
            @Query("current_weather") boolean current
    );
}