package com.example.weatherapp.Model;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    public Double speed;
    @SerializedName("deg")
    public Integer deg;

}
