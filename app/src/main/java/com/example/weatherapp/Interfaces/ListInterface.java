package com.example.weatherapp.Interfaces;

import com.example.weatherapp.Model.Result;
import com.example.weatherapp.Model.WList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ListInterface {

    @GET("forecast")
    Call<Result> getWeatherData(@Query("lat") String lat, @Query("lon") String lon,
                                @Query("appid") String api);

}
