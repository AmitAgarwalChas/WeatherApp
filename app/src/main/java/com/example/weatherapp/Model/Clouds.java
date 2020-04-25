package com.example.weatherapp.Model;

import com.google.gson.annotations.SerializedName;

public class Clouds {
    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    @SerializedName("all")
    private Integer all;
}
