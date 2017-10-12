package com.wilson.sodainmind.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.wilson.sodainmind.Pojo.PugsPOJO;
import com.wilson.sodainmind.R;
import com.wilson.sodainmind.adapters.PhotoAdapter;
import com.wilson.sodainmind.others.Constants;
import com.wilson.sodainmind.others.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoFragment extends Fragment {

    private static final String TAG = "PhotoFragment";
    private PhotoAdapter photoAdapter;
    private List<String> urlList = new ArrayList<>();
    @BindView(R.id.rv_photo) RecyclerView photoRV;

    public static final PhotoFragment newInstance()
    {
        return new PhotoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoAdapter = new PhotoAdapter(urlList);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), calculateNoOfColumns());
        photoRV.setLayoutManager(mLayoutManager);
        photoRV.setAdapter(photoAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getPugsImage();
    }

    private int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 100);
        return noOfColumns;
    }

    private void getPugsImage() {
        final String url = Constants.PUGS_URL;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        String json = response.toString();
                        PugsPOJO pojo = gson.fromJson(json, PugsPOJO.class);
                        urlList.clear();
                        urlList.addAll(Arrays.asList(pojo.getPugs()));
                        photoAdapter.notifyDataSetChanged();

                        Log.d(TAG, "urlList size(): " + urlList.size());
                        for (String item : urlList){
                            Log.d(TAG, "item url: " + item);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: " + error);
                    }
                });

        // Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }
}
