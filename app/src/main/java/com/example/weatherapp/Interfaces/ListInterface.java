package com.example.weatherapp.Interfaces;

import com.example.weatherapp.Model.WList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ListInterface {

    @GET("forecast")
    Call<WList> getWeatherData(@Query("q") String city, @Query("appid") String api);

}
