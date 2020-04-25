package com.example.weatherapp.Model;

import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("3h")
    private Double _3h;

    public Double get_3h() {
        return _3h;
    }

    public void set_3h(Double _3h) {
        this._3h = _3h;
    }
}
