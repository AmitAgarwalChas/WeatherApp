package com.example.weatherapp;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID = "ad9f043ce5acc6783666d462c6960531";
    public static String cityName = null;
    public static Location location = null;

    public static String convertUnixToDay(long dt){
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
        String day = simpleDateFormat.format(date);
        return day;
    }
}
