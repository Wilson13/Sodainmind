package com.wilson.sodainmind.others;

import java.util.ArrayList;
import java.util.List;

public class ShareInstance {

    public static final String NEARBY_PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?radius=500&key=AIzaSyBDC22QIgYoQlAbiYgAzqkaBJigRa3Uuvk";
    public static final String PUGS_URL = "http://private-e1b8f4-getimages.apiary-mock.com/getImages";
    public static final String FULL_SCREEN_POSITION_KEY = "FULL_SCREEN_POSITION_KEY";
    public static final String CLIPBOARD_URL = "CLIPBOARD_URL";

    public static final int FULL_SCREEN_REQUEST_CODE = 1;
    public static final String FULL_SCREEN_SCROLLED_KEY = "FULL_SCREEN_SCROLLED_KEY";

    private static final List<String> photoURL = new ArrayList<>();

    public static void setPhotoURL(List<String> url){
        photoURL.clear();
        photoURL.addAll(url);
    }

    public static List<String> getPhotoURL(){
        return photoURL;
    }
}
