package com.example.weatherapp.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.Common;
import com.example.weatherapp.Interfaces.ListInterface;
import com.example.weatherapp.Model.WList;
import com.example.weatherapp.R;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListFragment extends Fragment {

    private ImageView weather;
    private TextView weatherDesc, temp, maxMinTemp, wind, pressure, humidity, time;
    private RecyclerView listForecast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        initViews(view);
        checkNetwork();
        getWeatherDetails();

        return view;
    }

    private void checkNetwork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity())
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert  connectivityManager != null;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo==null || networkInfo.isFailover()){
                Toast.makeText(getContext(), "Please check your network connection",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    private void getWeatherDetails() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ListInterface listInterface = retrofit.create(ListInterface.class);
        assert Common.cityName != null;
        Log.e("fragment cityName", Common.cityName);
        Call<WList> weatherData = listInterface.getWeatherData(Common.cityName, Common.APP_ID);
        weatherData.enqueue(new Callback<WList>() {
            @Override
            public void onResponse(Call<WList> call, Response<WList> response) {
                if(response.isSuccessful()){
                    weatherDesc.setText(response.body().getWeather().get(0).getDescription());
                    temp.setText(response.body().getMain().getTemp().toString());
                    maxMinTemp.setText(response.body().getMain().getTempMin().toString()
                        + "/" + response.body().getMain().getTempMax().toString());
                    humidity.setText(response.body().getMain().getHumidity().toString() + " %");
                    pressure.setText(response.body().getMain().getPressure().toString() + " hpa");
                    time.setText(response.body().getDtTxt());
                    wind.setText(response.body().getWind().getSpeed().toString());
                }else{
                    Toast.makeText(getContext(), "Sorry, No data is available for selected city",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WList> call, Throwable t) {
                Log.e("Today Data", "Failure", t);
            }
        });

    }

    private void initViews(View v) {
        weather = v.findViewById(R.id.img_weather);
        weatherDesc = v.findViewById(R.id.tv_weather_desc);
        temp = v.findViewById(R.id.tv_temp);
        maxMinTemp = v.findViewById(R.id.tv_max_min);
        wind = v.findViewById(R.id.tv_wind);
        pressure = v.findViewById(R.id.tv_pressure);
        humidity = v.findViewById(R.id.tv_humidity);
        time = v.findViewById(R.id.tv_last_time);
        listForecast = v.findViewById(R.id.rv_forecast);
    }
}
