package com.wilson.sodainmind.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.sodainmind.R;

import java.util.ArrayList;

public class GlobalCountryAdapter extends RecyclerView.Adapter<GlobalCountryAdapter.GlobalWeatherHolder> {

    private static final String TAG = "GlobalCountryAdapter";
    private ArrayList<GlobalWeatherItem> globalWeatherItems;

    public GlobalCountryAdapter(ArrayList<GlobalWeatherItem> globalWeatherItems) {
        this.globalWeatherItems = globalWeatherItems;
    }

    @Override
    public GlobalWeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_global_weather_item, parent, false);
        return new GlobalWeatherHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(GlobalWeatherHolder holder, int position) {

        // Get date from list of global weather itesm at corresponding position
        int weatherCode = globalWeatherItems.get(position).getCode();
        String weatherTime = globalWeatherItems.get(position).getTime();
        String weatherTemp = globalWeatherItems.get(position).getTemp();
        String weatherCountry = globalWeatherItems.get(position).getCountry();
        String weatherCondition =globalWeatherItems.get(position).getCondition();

        // Display the data
        holder.tempTV.setText(weatherTemp + "\u00B0c");
        holder.countryTV.setText(weatherCountry);
        holder.weatherTV.setText(weatherCondition);

        switch (weatherCode) {
            // Sunny
            case 1000:
                holder.weatherIV.setImageResource(R.drawable.ic_sunny);
                //holder.timeTV.setText(weatherTime);
                break;
            // Partly cloudy
            case 1003:
            case 1006:
                holder.weatherIV.setImageResource(R.drawable.ic_cloudy);
                break;
            // Overcast
            case 1009:
                holder.weatherIV.setImageResource(R.drawable.ic_overcast);
                break;
            // Patchy rain
            case 1063:
            // Light rain
            case 1183:
                holder.weatherIV.setImageResource(R.drawable.ic_light_rain);
                break;
            case 1273:
                holder.weatherTV.setText(R.string.patchy_light_rain_with_thunder);
                holder.weatherIV.setImageResource(R.drawable.ic_thunder_rain);
                break;
            // Moderate or heavy rain with thunder
            case 1276:
                holder.weatherTV.setText(R.string.moderate_or_heavy_rain_with_thunder);
                holder.weatherIV.setImageResource(R.drawable.ic_thunder_rain);
        }
    }

    @Override
    public int getItemCount() {
        return globalWeatherItems.size();
    }

    class GlobalWeatherHolder extends RecyclerView.ViewHolder {

        TextView countryTV;
        TextView tempTV;
        TextView weatherTV;
        ImageView weatherIV;

        GlobalWeatherHolder(View itemView) {
            super(itemView);
            countryTV = itemView.findViewById(R.id.tv_country);
            tempTV = itemView.findViewById(R.id.tv_temp);
            weatherTV = itemView.findViewById(R.id.tv_weather);
            weatherIV = itemView.findViewById(R.id.iv_weather);
        }
    }

    public static class GlobalWeatherItem {
        int weatherCode;
        String weatherTime;
        String weatherTempC;
        String weatherCountry;
        String weatherCondition;

        public GlobalWeatherItem(int weatherCode, String weatherTime, String weatherTempC, String weatherCountry, String weatherCondition) {
            this.weatherCode = weatherCode;
            this.weatherTime = weatherTime;
            this.weatherTempC = weatherTempC;
            this.weatherCountry = weatherCountry;
            this.weatherCondition = weatherCondition;
        }

        private int getCode() {
            return this.weatherCode;
        }

        private String getTime() {
            return this.weatherTime;
        }

        private String getTemp() {
            return this.weatherTempC;
        }

        private String getCountry() {
            return this.weatherCountry;
        }

        private String getCondition() {
            return this.weatherCondition;
        }

    }
}
