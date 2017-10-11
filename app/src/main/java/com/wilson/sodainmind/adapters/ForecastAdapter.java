package com.wilson.sodainmind.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.sodainmind.R;

import java.util.ArrayList;

import static com.wilson.sodainmind.others.Constants.Constants.CONDITION_MODERATE_RAIN_AT_TIMES;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastHolder> {

    private static final String TAG = "ForecastAdapter";
    private ArrayList<WeatherItem> weatherItems;

    public ForecastAdapter(ArrayList<WeatherItem> weatherItems) {
        this.weatherItems = weatherItems;
    }

    @Override
    public ForecastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_forecast_item, parent, false);
        return new ForecastHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ForecastHolder holder, int position) {
        int weatherCode = weatherItems.get(position).getCode();
        Log.d(TAG, "position: " + position + " weatherCode: " + weatherCode);
        String weatherTime = weatherItems.get(position).getTime();
        String weatherTemp = weatherItems.get(position).getTemp();
        holder.timeTV.setText(String.valueOf(weatherTime));
        holder.weatherTV.setText(weatherTemp + "\u00B0c");
        switch (weatherCode) {
            // Sunny
            case 1000:
                holder.weatherIV.setImageResource(R.drawable.ic_sunny);
                holder.timeTV.setText(weatherTime);
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
                // Moderate rain at times
            case CONDITION_MODERATE_RAIN_AT_TIMES:
                holder.weatherIV.setImageResource(R.drawable.ic_light_rain);
                break;
            // Moderate or heavy rain with thunder
            case 1276:
                holder.timeTV.setText(weatherTime);
                holder.weatherIV.setImageResource(R.drawable.ic_thunder_rain);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return weatherItems.size();
    }

    class ForecastHolder extends RecyclerView.ViewHolder {

        TextView timeTV;
        TextView weatherTV;
        ImageView weatherIV;

        ForecastHolder(View itemView) {
            super(itemView);
            timeTV = (TextView) itemView.findViewById(R.id.tv_date);
            weatherTV = (TextView) itemView.findViewById(R.id.tv_temp);
            weatherIV = (ImageView) itemView.findViewById(R.id.iv_weather);
        }
    }

    public static class WeatherItem {
        int weatherCode;
        String weatherTime;
        String weatherTempC;

        public WeatherItem(int weatherCode, String weatherTime, String weatherTempC) {
            this.weatherCode = weatherCode;
            this.weatherTime = weatherTime;
            this.weatherTempC = weatherTempC;
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

    }
}
