package com.example.weatherapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
import com.example.weatherapp.Model.Result;
import com.example.weatherapp.Model.WList;
import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListFragment extends Fragment {

    CardView todayDetails;
    private ImageView weather;
    private TextView weatherDesc, temp, maxMinTemp, wind, pressure, humidity, time;
    private RecyclerView listForecast;
    ProgressDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        loading = new ProgressDialog(getContext());
        loading.setMessage("Loading...");
        initViews(view);
        todayDetails.setVisibility(View.INVISIBLE);
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
            }else{
                loading.show();
            }
        }
    }

    private void getWeatherDetails() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ListInterface listInterface = retrofit.create(ListInterface.class);
        assert Common.location != null;
        Log.e("fragment cityName", Common.cityName);
        Call<Result> weatherData = listInterface.getWeatherData(String.valueOf(Common.location.getLatitude()),
                String.valueOf(Common.location.getLongitude()), Common.APP_ID);
        weatherData.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.code()==200){
                    Result weatherResponse = response.body();
                    assert weatherResponse != null;

                    if(weatherResponse.list.get(0).weather.get(0).getMain().equalsIgnoreCase("Clouds")){
                        Picasso.get().load(R.drawable.cloudy).into(weather);
                    }else if(weatherResponse.list.get(0).weather.get(0).getMain().equalsIgnoreCase("Rainy")){
                        Picasso.get().load(R.drawable.rainy).into(weather);
                    }else{
                        Picasso.get().load(R.drawable.sunny).into(weather);
                    }

                    weatherDesc.setText(new StringBuilder(weatherResponse.list.get(0).weather.get(0).getMain()));
                    temp.setText(String.valueOf(weatherResponse.list.get(0).main.temp.intValue()-273)+"°C");
                    maxMinTemp.setText(String.valueOf(weatherResponse.list.get(0).main.tempMin.intValue()-273)
                            +"°C"+ " / "+ String.valueOf(weatherResponse.list.get(0).main.tempMax.intValue()-273)
                            +"°C");
                    humidity.setText(weatherResponse.list.get(0).main.humidity.toString() + " %");
                    pressure.setText(weatherResponse.list.get(0).main.pressure.toString() + " hpa");
                    time.setText(weatherResponse.list.get(0).dtTxt);
                    wind.setText((int)(weatherResponse.list.get(0).wind.speed * 3.6)+" km/h");
                    loading.dismiss();
                    todayDetails.setVisibility(View.VISIBLE);
                }else{
                    loading.dismiss();
                    Toast.makeText(getContext(), "Sorry, No data is available for selected city",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                loading.dismiss();
                Log.e("Today Data", "Failure", t);
            }
        });

    }

    private void initViews(View v) {
        todayDetails = v.findViewById(R.id.cv_today);
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
