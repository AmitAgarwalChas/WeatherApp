package com.example.weatherapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WList {
    @SerializedName("dt")
    public Integer dt;
    @SerializedName("main")
    public Main main;
    @SerializedName("weather")
    public ArrayList<Weather> weather = new ArrayList<>();
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("dt_txt")
    public String dtTxt;
    @SerializedName("rain")
    public Rain rain;
}
