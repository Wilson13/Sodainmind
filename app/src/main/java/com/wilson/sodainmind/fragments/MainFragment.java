package com.wilson.sodainmind.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.wilson.sodainmind.Pojo.NearbyPlacesPOJO;
import com.wilson.sodainmind.Pojo.NearbyPlacesPOJO.Results;
import com.wilson.sodainmind.R;
import com.wilson.sodainmind.others.Constants;
import com.wilson.sodainmind.others.VolleySingleton;

import org.json.JSONObject;

import butterknife.ButterKnife;

public class MainFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnPoiClickListener {

    private static final String TAG = "MainFragment";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    private static final float ZOOM_LEVEL = 16.0f; //This goes up to 21
    private GoogleMap mMap;
    private Marker mMarker;
    private FusedLocationProviderClient mFusedLocationClient;
    private MainFragmentInterface delegate;

    public static final MainFragment newInstance() {
        return new MainFragment();
    }

    public interface MainFragmentInterface {
        void onGoogleMapReady();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.f_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null)
            zoomInCurrentLocation();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnPoiClickListener(this);
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        zoomInLocation(latLng, getString(R.string.current_location));
        getNearbyRestaurant(latLng);
        getNearbyATM(latLng);
        getNearbyHospital(latLng);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                // Check for ACCESS_FINE_LOCATION
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, proceed to the normal flow.
                    if (getActivity() != null)
                        zoomInCurrentLocation();
                } else if (getActivity() != null){
                    // Permission Denied
                    Toast.makeText(getActivity(), R.string.permission_location, Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void zoomInLocation(LatLng latLng, String name) {
        if (mMarker != null) {
            // Remove previous marker
            mMarker.remove();
        }
        // Add a marker to current location
        mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(name));
        // Zoom in to current location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
    }

    // This function is also called in MainActivity
    private void zoomInCurrentLocation() {

        // Permission check is required for getting current/last location
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showAlertDialog(getActivity(), R.string.permission_location,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Don't call AppcompatActivity.requestpermissions to prevent
                                // onRequestPermissionsResult callback getting to activity but not here.
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        });
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            zoomInLocation(latLng, getString(R.string.current_location));
                            getNearbyRestaurant(latLng);
                            getNearbyATM(latLng);
                            getNearbyHospital(latLng);
                        }
                    }
                });
    }

    private void getNearbyRestaurant(LatLng latLng) {
        String urlParameter = "&type=restaurant&location=" + latLng.latitude + "," + latLng.longitude;
        String url = Constants.NEARBY_PLACES_URL + urlParameter;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        String json = response.toString();
                        NearbyPlacesPOJO pojo = gson.fromJson(json, NearbyPlacesPOJO.class);
                        Results[] results = pojo.getResults();

                        for (Results item : results){
                            double lat = Double.valueOf(item.getGeometry().getLocation().getLat());
                            double lng = Double.valueOf(item.getGeometry().getLocation().getLng());
                            LatLng latLng = new LatLng(lat, lng);
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(item.getName())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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

    private void getNearbyATM(LatLng latLng) {
        String urlParameter = "&type=atm&location=" + latLng.latitude + "," + latLng.longitude;
        String url = Constants.NEARBY_PLACES_URL + urlParameter;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        String json = response.toString();
                        NearbyPlacesPOJO pojo = gson.fromJson(json, NearbyPlacesPOJO.class);
                        Results[] results = pojo.getResults();

                        for (Results item : results){
                            double lat = Double.valueOf(item.getGeometry().getLocation().getLat());
                            double lng = Double.valueOf(item.getGeometry().getLocation().getLng());
                            LatLng latLng = new LatLng(lat, lng);
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(item.getName())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
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

    private void getNearbyHospital(LatLng latLng) {
        String urlParameter = "&type=hospital&location=" + latLng.latitude + "," + latLng.longitude;
        String url = Constants.NEARBY_PLACES_URL + urlParameter;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        String json = response.toString();
                        NearbyPlacesPOJO pojo = gson.fromJson(json, NearbyPlacesPOJO.class);
                        Results[] results = pojo.getResults();

                        for (Results item : results){
                            double lat = Double.valueOf(item.getGeometry().getLocation().getLat());
                            double lng = Double.valueOf(item.getGeometry().getLocation().getLng());
                            LatLng latLng = new LatLng(lat, lng);
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(item.getName())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
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

    private void showAlertDialog(FragmentActivity activity, int msgId, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(msgId)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}