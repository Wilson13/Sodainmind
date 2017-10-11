package com.wilson.sodainmind.others;

public class ShareInstance {

    public static final String NEARBY_PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?radius=500&key=AIzaSyBDC22QIgYoQlAbiYgAzqkaBJigRa3Uuvk";

    public static String userCountry = "Malaysia";

    public static String getUserCountry() {
        return userCountry;
    }

    public static void setUserCountry(String country) {
        userCountry = country;
    }
}