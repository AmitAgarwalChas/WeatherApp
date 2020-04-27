package com.example.weatherapp.Model;

import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("cod")
    public String cod;
    @SerializedName("message")
    public Integer message;
    @SerializedName("cnt")
    public Integer cnt;
    @SerializedName("list")
    public java.util.List<WList> list = null;
    @SerializedName("city")
    public City city;
}
