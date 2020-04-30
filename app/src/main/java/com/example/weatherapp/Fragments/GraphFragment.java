package com.example.weatherapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.weatherapp.Common;
import com.example.weatherapp.Interfaces.ListInterface;
import com.example.weatherapp.Model.Result;
import com.example.weatherapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GraphFragment extends Fragment {

    private LineChart chart;
    private LottieAnimationView tempAnim;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        initViews(view);
        tempAnim.setVisibility(View.GONE);
        chart.setVisibility(View.GONE);
        if(checkNetwork()){
            graphDetails();
        }
        return view;
    }

    private void graphDetails() {
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setPinchZoom(true);
        chart.getLegend().setTextColor(Color.WHITE);
        final XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.WHITE);
        YAxis yRAxis = chart.getAxisRight();
        yRAxis.setEnabled(false);
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ListInterface listInterface = retrofit.create(ListInterface.class);
        assert Common.location != null;
        Log.e("GraphFragment cityName", Common.cityName);
        String lat, lon;
        if(Common.searchLatLng == null){
            lat = String.valueOf(Common.location.getLatitude());
            lon = String.valueOf(Common.location.getLongitude());
        }else{
            lat = String.valueOf(Common.searchLatLng.latitude);
            lon = String.valueOf(Common.searchLatLng.longitude);
        }
        final Call<Result> weatherData = listInterface.getWeatherData(lat, lon, Common.APP_ID);
        weatherData.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.code()==200){
                    Result weatherResponse = response.body();
                    ArrayList<Integer> positions;
                    positions = getPositions(weatherResponse.list.get(0).dt, weatherResponse);
                    ArrayList<Entry> yValues = new ArrayList<>();
                    final String[] days = new String[5];
                    for(int i = 0;i<5;i++){
                        yValues.add(new Entry(i, weatherResponse.list
                                        .get(positions.get(i)).main.temp.floatValue()-273.15f));
                        days[i] = Common.convertUnixToDay(weatherResponse.list
                                    .get(positions.get(i)).dt);
                    }
                    ValueFormatter formatter = new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            return days[(int)value];
                        }
                    };
                    xAxis.setValueFormatter(formatter);
                    LineDataSet lineDataSet = new LineDataSet(yValues, "Temperature in °C");
                    lineDataSet.setColor(Color.RED);
                    lineDataSet.setValueTextColor(Color.rgb(255, 165, 0));
                    lineDataSet.setValueTextSize(10f);
                    lineDataSet.setLineWidth(1.5f);
                    LineData data = new LineData(lineDataSet);
                    chart.setData(data);
                    tempAnim.setVisibility(View.GONE);
                    chart.setVisibility(View.VISIBLE);
                    chart.animateX(2500);
                    chart.invalidate();

                }else{
                    tempAnim.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Sorry, No data is available for selected city",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                tempAnim.setVisibility(View.GONE);
                Log.e("Today Data", "Failure", t);
            }
        });

    }

    private ArrayList<Integer> getPositions(Integer date, Result response) {
        int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0;
        ArrayList<Integer> positions = new ArrayList<>();
        int size = response.list.size();
        for(int i=0;i<size;i++){
            if(response.list.get(i).dt == date && count1 == 0){
                positions.add(i);
                count1++;
            }
            if(response.list.get(i).dt == date+86400 && count2 == 0){
                positions.add(i);
                count2++;
            }
            if(response.list.get(i).dt == date+172800 && count3 == 0){
                positions.add(i);
                count3++;
            }
            if(response.list.get(i).dt == date+259200 && count4 == 0){
                positions.add(i);
                count4++;
            }
            if(response.list.get(i).dt == date+345600 && count5 == 0){
                positions.add(i);
                count5++;
            }
        }
        return positions;
    }

    private boolean checkNetwork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity())
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert  connectivityManager != null;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo==null || networkInfo.isFailover()){
                Toast.makeText(getContext(), "Please check your network connection",
                        Toast.LENGTH_LONG).show();
                return false;
            }else{
                tempAnim.setVisibility(View.VISIBLE);
                tempAnim.playAnimation();
                return true;
            }
        }
        return true;
    }

    private void initViews(View v) {
        chart = v.findViewById(R.id.line_chart);
        tempAnim = v.findViewById(R.id.anime_temp);
    }
}
