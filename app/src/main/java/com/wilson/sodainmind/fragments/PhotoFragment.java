package com.wilson.sodainmind.fragments;

import android.content.Intent;
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
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.wilson.sodainmind.Pojo.PugsPOJO;
import com.wilson.sodainmind.R;
import com.wilson.sodainmind.activities.FullScreenActivity;
import com.wilson.sodainmind.adapters.PhotoAdapter;
import com.wilson.sodainmind.others.ShareInstance;
import com.wilson.sodainmind.others.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.wilson.sodainmind.others.ShareInstance.FULL_SCREEN_POSITION_KEY;
import static com.wilson.sodainmind.others.ShareInstance.FULL_SCREEN_REQUEST_CODE;
import static com.wilson.sodainmind.others.ShareInstance.FULL_SCREEN_SCROLLED_KEY;

public class PhotoFragment extends Fragment implements PhotoAdapter.PhotoadapterListener {

    private static final String TAG = "PhotoFragment";
    private PhotoAdapter photoAdapter;
    private List<String> urlList = new ArrayList<>();
    @BindView(R.id.rv_photo) RecyclerView photoRV;
    @BindView(R.id.ll_empty_state) LinearLayout emptyLL;

    public static PhotoFragment newInstance()
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
        photoAdapter = new PhotoAdapter(urlList, getContext());
        photoAdapter.setPhotoAdapterListener(this);
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
        final String url = ShareInstance.PUGS_URL;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        String json = response.toString();
                        PugsPOJO pojo = gson.fromJson(json, PugsPOJO.class);

                        // Get URLs string from JSON
                        urlList.clear();
                        urlList.addAll(Arrays.asList(pojo.getPugs()));
                        // Save into shareinstance
                        ShareInstance.setPhotoURL(urlList);
                        photoAdapter.notifyDataSetChanged();

                        // Show/hide empty state information
                        showHideEmptyState();
                        Log.d(TAG, "urlList size(): " + urlList.size());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: " + error);
                        showHideEmptyState();
                    }
                });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }

    private void showHideEmptyState() {
        // Show/hide empty state information
        if (urlList.size() > 0) {
            emptyLL.setVisibility(View.GONE);
        } else {
            emptyLL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void fullScreenClick(int pos) {
        Intent fullScreenIntent = new Intent(getContext(), FullScreenActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putInt(FULL_SCREEN_POSITION_KEY, pos);
        fullScreenIntent.putExtras(mBundle);
        startActivityForResult(fullScreenIntent, FULL_SCREEN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FULL_SCREEN_REQUEST_CODE && resultCode == RESULT_OK) {
            int pos = data.getIntExtra(FULL_SCREEN_SCROLLED_KEY, 0);
            photoRV.scrollToPosition(pos);
        }
    }
}
