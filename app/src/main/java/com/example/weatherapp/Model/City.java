package com.example.weatherapp.Model;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("coord")
    public Coord coord;
    @SerializedName("country")
    public String country;
    @SerializedName("population")
    public Integer population;
    @SerializedName("timezone")
    public Integer timezone;
    @SerializedName("sunrise")
    public Integer sunrise;
    @SerializedName("sunset")
    public Integer sunset;

}
