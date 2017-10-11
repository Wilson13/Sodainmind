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
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wilson.sodainmind.DBFlow.CountryDBModel;
import com.wilson.sodainmind.DBFlow.DBHelper;
import com.wilson.sodainmind.R;
import com.wilson.sodainmind.adapters.GlobalCountryAdapter;
import com.wilson.sodainmind.others.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlobalWeatherFragment extends Fragment {

    private static final String TAG = "GlobalWeatherFragment";
    private ArrayList<GlobalCountryAdapter.GlobalWeatherItem> weatherList = new ArrayList<>();
    private GlobalCountryAdapter globalCountryAdapter;
    @BindView(R.id.rv_country) RecyclerView countryRV;
    @BindView(R.id.ll_empty_state) LinearLayout emptyStateLL;

    public static final GlobalWeatherFragment newInstance()
    {
        GlobalWeatherFragment globalWeatherFragment = new GlobalWeatherFragment();
        return globalWeatherFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_global_weather, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }

        Log.d(TAG, "onViewCreated");
        globalCountryAdapter = new GlobalCountryAdapter(weatherList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        countryRV.setLayoutManager(mLayoutManager);
        countryRV.setAdapter(globalCountryAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // If no country selected
        if (DBHelper.getSelectedCountriesCount() == 0) {
            emptyStateLL.setVisibility(View.VISIBLE);
            countryRV.setVisibility(View.GONE);
        } else {
            emptyStateLL.setVisibility(View.GONE);
            countryRV.setVisibility(View.VISIBLE);
            weatherList.clear();
            List<CountryDBModel> countryModelList = DBHelper.getSelectedCountries();
            for (CountryDBModel item : countryModelList) {
                getCountryWeather(item.getName());
            }
        }
    }

    private void getCountryWeather(String country) {
        String url = "https://api.apixu.com/v1/current.json?key=a9e935d63c264291955140549172308&q=" + country;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "response: " + response.toString());
                        try {
                            int weatherCode = response.getJSONObject("current").getJSONObject("condition").getInt("code");
                            String weatherCondition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                            String weatherTemp = response.getJSONObject("current").getString("temp_c");
                            String weatherCountry = response.getJSONObject("location").getString("name");

                            GlobalCountryAdapter.GlobalWeatherItem globalWeatherHolder = new GlobalCountryAdapter.GlobalWeatherItem(weatherCode, "", weatherTemp, weatherCountry, weatherCondition);
                            weatherList.add(globalWeatherHolder);
                            globalCountryAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }
}
