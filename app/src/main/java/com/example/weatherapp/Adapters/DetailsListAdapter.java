package com.example.weatherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Common;
import com.example.weatherapp.Model.Result;
import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DetailsListAdapter extends RecyclerView.Adapter<DetailsListAdapter.ViewHolder> {

    Context context;
    Result forecastResult;
    ArrayList<Integer> positions;

    public DetailsListAdapter(Context context, Result forecastResult, ArrayList<Integer> positions) {
        this.context = context;
        this.forecastResult = forecastResult;
        this.positions = positions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(forecastResult.list.get(positions.get(position)).weather.get(0).getMain().equalsIgnoreCase("Clouds")){
            Picasso.get().load(R.drawable.cloudy).into(holder.forecastWeather);
        }else if(forecastResult.list.get(positions.get(position)).weather.get(0).getMain().equalsIgnoreCase("Rain")){
            Picasso.get().load(R.drawable.rainy).into(holder.forecastWeather);
        }else{
            Picasso.get().load(R.drawable.sunny).into(holder.forecastWeather);
        }
        int i = positions.get(position);
        holder.forecastDay.setText(Common.convertUnixToDay(forecastResult.list.get(i).dt));
        holder.forecastWeatherDesc.setText(new StringBuilder(forecastResult.list.get(i).weather.get(0).getMain()));
        holder.forecastTemp.setText(String.valueOf(forecastResult.list.get(i).main.temp.intValue()-273)+"°C");
        holder.forecastHumidity.setText(forecastResult.list.get(i).main.humidity.toString() + "%");
        holder.forecastPressure.setText(forecastResult.list.get(i).main.pressure.toString() + " hpa");
        holder.forecastWind.setText((int)(forecastResult.list.get(i).wind.speed * 3.6)+" km/h");

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView forecastWeatherDesc, forecastWind, forecastHumidity, forecastDay,
                forecastTemp, forecastPressure;
        ImageView forecastWeather;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            forecastDay = itemView.findViewById(R.id.tv_day);
            forecastTemp = itemView.findViewById(R.id.list_temp);
            forecastWeatherDesc = itemView.findViewById(R.id.list_weather_desc);
            forecastWeather = itemView.findViewById(R.id.img_list_weather);
            forecastHumidity = itemView.findViewById(R.id.forecast_humidity);
            forecastPressure = itemView.findViewById(R.id.forecast_pressure);
            forecastWind = itemView.findViewById(R.id.forecast_wind);
        }
    }
}
