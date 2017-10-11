package com.wilson.sodainmind.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wilson.sodainmind.R;
import com.wilson.sodainmind.others.ShareInstance;
import com.wilson.sodainmind.others.VolleySingleton;
import com.wilson.sodainmind.adapters.ForecastAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForecastFragment extends Fragment {

    public static final String USER_COUNTRY = "USER_COUNTRY";
    private static final String TAG = "ForecastFragment";
    private String userCountry = "Malaysia";

    @BindView(R.id.rv_info) RecyclerView infoRV;

    public static final ForecastFragment newInstance()
    {
        return new ForecastFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        userCountry = ShareInstance.getUserCountry();
        getWeatherForecast(userCountry);
    }

    private void getWeatherForecast(String country) {
        String url = "https://api.apixu.com/v1/forecast.json?key=a9e935d63c264291955140549172308&q=" + country + "&days=10";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "response getWeatherForecast: " + response.toString());
                        try {

                            // Get forecast hour array from JSON
                            JSONArray dayArray = response.getJSONObject("forecast").getJSONArray("forecastday");
                            // Prepare JsonArray to be passed to RecyclerAdapter
                            ArrayList<ForecastAdapter.WeatherItem> weatherArray = new ArrayList<>();

                            for (int i = 0; i < dayArray.length(); i++) {
                                // Loop through and get each forecast time, weather code, and temperature (Celcius) from JSON
                                JSONObject dayObj = dayArray.getJSONObject(i);

                                Log.d(TAG, "dayObj: " + dayObj.toString());
                                String weatherDate = dayObj.getString("date");
                                int weatherCode = dayObj.getJSONObject("day").getJSONObject("condition").getInt("code");
                                String weatherTemp = dayObj.getJSONObject("day").getString("avgtemp_c").substring(0,2);
                                Log.d(TAG, "date: " + weatherDate + "day in weekL " + getDayOfWeek(weatherDate) + " avg temp: " + dayObj.getJSONObject("day").getString("avgtemp_c"));

                                if (i == 0)
                                    weatherArray.add(new ForecastAdapter.WeatherItem(weatherCode, "Today", weatherTemp));
                                else
                                    weatherArray.add(new ForecastAdapter.WeatherItem(weatherCode, getDayOfWeek(weatherDate), weatherTemp));
                            }
                            // Assign adapter to RecyclerView and update its data
                            ForecastAdapter forecastAdapter = new ForecastAdapter(weatherArray);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            infoRV.setLayoutManager(mLayoutManager);
                            infoRV.setAdapter(forecastAdapter);
                            forecastAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }

    private String getDayOfWeek(String timeStr) {

        if (getContext() != null) {
            // Convert String object into Date object
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date currentDate = null;
            try {
                currentDate = format.parse(timeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Use Calendar object to get day in week
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            String dayInWeek;
            Log.d(TAG, "currentDate: " + currentDate);
            Log.d(TAG, "Calendar.DAY_OF_WEEK: " + cal.get(Calendar.DAY_OF_WEEK_IN_MONTH));
            switch (cal.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    dayInWeek = getString(R.string.day_monday);
                    break;
                case Calendar.TUESDAY:
                    dayInWeek = getString(R.string.day_tuesday);
                    break;
                case Calendar.WEDNESDAY:
                    dayInWeek = getString(R.string.day_wednesday);
                    break;
                case Calendar.THURSDAY:
                    dayInWeek = getString(R.string.day_thursday);
                    break;
                case Calendar.FRIDAY:
                    dayInWeek = getString(R.string.day_friday);
                    break;
                case Calendar.SATURDAY:
                    dayInWeek = getString(R.string.day_saturday);
                    break;
                case Calendar.SUNDAY:
                    dayInWeek = getString(R.string.day_sunday);
                    break;
                default:
                    dayInWeek = getString(R.string.day_monday);
            }
            return dayInWeek;
        }
        return null;
    }

    public void refreshWeather() {
        getWeatherForecast(userCountry);
    }
}
