package com.wilson.sodainmind.others;

import android.app.Application;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.wilson.sodainmind.DBFlow.CountryDBModel;
import com.wilson.sodainmind.DBFlow.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        initDatabase();
    }

    private void initDatabase() {
        long rowSize = DBHelper.getAllCountriesCount();

        // If no data inserted yet
        if (rowSize == 0) {
            try {
                // Get json string from assets
                String json;
                InputStream is = getAssets().open("country_iso3166_full.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");

                // Get country name based on countryISO
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("iso-codes");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject curObj = jsonArray.getJSONObject(i);

                    // Get country properties
                    int number = curObj.getInt("number");
                    int order = -1; // Initialize order as -1
                    String name = curObj.getString("name");
                    String alpha2 = curObj.getString("alpha2");
                    String alpha3 = curObj.getString("alpha3");
                    Log.d("MyApp", "name: " + name);

                    // Create and insert model into database
                    CountryDBModel countryModel = new CountryDBModel();
                    countryModel.insertRow(number, name, alpha2, alpha3, false);
                    countryModel.save();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
