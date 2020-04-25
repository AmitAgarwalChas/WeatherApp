package com.example.weatherapp.Model;

import com.google.gson.annotations.SerializedName;

public class Example {
    @SerializedName("cod")
    private String cod;
    @SerializedName("message")
    private Integer message;
    @SerializedName("cnt")
    private Integer cnt;
    @SerializedName("list")
    private java.util.List<WList> WList = null;
    @SerializedName("city")
    private City city;

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public java.util.List<WList> getWList() {
        return WList;
    }

    public void setWList(java.util.List<WList> WList) {
        this.WList = WList;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
