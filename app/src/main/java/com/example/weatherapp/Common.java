package com.example.weatherapp;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID = "MY_API_KEY";
    public static String cityName = null;
    public static Location location = null;
    public static String searchCityName = null;
    public static LatLng searchLatLng = null;

    public static String convertUnixToDay(long dt){
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
        String day = simpleDateFormat.format(date);
        return day;
    }
}
