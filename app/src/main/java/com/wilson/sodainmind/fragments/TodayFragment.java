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

public class TodayFragment extends Fragment {

    public static final String USER_COUNTRY = "USER_COUNTRY";
    private static final String TAG = "TodayFragment";
    private String userCountry;
    @BindView(R.id.rv_info) RecyclerView infoRV;

    public static final TodayFragment newInstance()
    {
        return new TodayFragment();
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
        String url = "https://api.apixu.com/v1/forecast.json?key=a9e935d63c264291955140549172308&q=" + country;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "response getWeatherForecast: " + response.toString());
                        //Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            // Get current time from JSON
                            String currentTimeStr = response.getJSONObject("location").getString("localtime");
                            // Convert String object into Date object
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
                            Date currentDate = format.parse(currentTimeStr);
                            // Use Calendar object to get current hour
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(currentDate);

                            // Get forecast hour array from JSON
                            JSONArray hourArray = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour");
                            // Prepare JsonArray to be passed to RecyclerAdapter
                            ArrayList<ForecastAdapter.WeatherItem> weatherArray = new ArrayList<>();

                            for (int i = 0; i < hourArray.length(); i++) {
                                // Loop through and get each forecast time, weather code, and temperature (Celcius) from JSON
                                JSONObject hourObj = hourArray.getJSONObject(i);
                                // Time
                                String forecastTimeStr = hourObj.getString("time");
                                // Temperature in celcius
                                String forecastTemp = hourObj.getString("temp_c").substring(0, 2);
                                // Weather code
                                int weatherCode = hourObj.getJSONObject("condition").getInt("code");
                                // Convert String object into Date object
                                Date forecastDate = format.parse(forecastTimeStr);
                                // Use Calendar object to get forecast hour
                                cal.setTime(forecastDate);
                                // Time in 12-hour format
                                int forecastHourAMPM = cal.get(Calendar.HOUR);

                                // Get forecast am/pm
                                String amPM = (int)cal.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
                                // Get forecast hour in 12-hour clock and set to 12 if it's 0
                                String displayHour = forecastHourAMPM == 0 ? "12" : String.valueOf(forecastHourAMPM);
                                // Concatenate
                                String displayTime = displayHour + " " + amPM;

                                weatherArray.add(new ForecastAdapter.WeatherItem(weatherCode, displayTime, forecastTemp));
                                //}
                            }
                            // Assign adapter to RecyclerView and update its data
                            ForecastAdapter forecastAdapter = new ForecastAdapter(weatherArray);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            infoRV.setLayoutManager(mLayoutManager);
                            infoRV.setAdapter(forecastAdapter);
                            forecastAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
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

    public void refreshTodayWeather() {
    }

}
