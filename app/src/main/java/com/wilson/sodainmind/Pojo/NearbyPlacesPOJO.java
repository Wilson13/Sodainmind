package com.wilson.sodainmind.Pojo;

/**
 * Created by xyconnect on 11/10/17.
 */

public class NearbyPlacesPOJO {

        private Results[] results;

        private String[] html_attributions;

        private String status;

        public Results[] getResults() {
            return results;
        }

        public void setResults(Results[] results) {
            this.results = results;
        }

        public String[] getHtml_attributions() {
            return html_attributions;
        }

        public void setHtml_attributions(String[] html_attributions) {
            this.html_attributions = html_attributions;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "ClassPojo [results = " + results + ", html_attributions = " + html_attributions + ", status = " + status + "]";
        }

    public class Results {

        private String icon;

        private String place_id;

        private String scope;

        private String reference;

        private Geometry geometry;

        private Opening_hours opening_hours;

        private String id;

        private Photos[] photos;

        private String price_level;

        private String vicinity;

        private String name;

        private String rating;

        private String[] types;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public Opening_hours getOpening_hours() {
            return opening_hours;
        }

        public void setOpening_hours(Opening_hours opening_hours) {
            this.opening_hours = opening_hours;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Photos[] getPhotos() {
            return photos;
        }

        public void setPhotos(Photos[] photos) {
            this.photos = photos;
        }

        public String getPrice_level() {
            return price_level;
        }

        public void setPrice_level(String price_level) {
            this.price_level = price_level;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String[] getTypes() {
            return types;
        }

        public void setTypes(String[] types) {
            this.types = types;
        }

        @Override
        public String toString() {
            return "ClassPojo [icon = " + icon + ", place_id = " + place_id + ", scope = " + scope + ", reference = " + reference + ", geometry = " + geometry + ", opening_hours = " + opening_hours + ", id = " + id + ", photos = " + photos + ", price_level = " + price_level + ", vicinity = " + vicinity + ", name = " + name + ", rating = " + rating + ", types = " + types + "]";
        }
    }

    public class Geometry
    {
        private Viewport viewport;

        private Location location;

        public Viewport getViewport ()
        {
            return viewport;
        }

        public void setViewport (Viewport viewport)
        {
            this.viewport = viewport;
        }

        public Location getLocation ()
        {
            return location;
        }

        public void setLocation (Location location)
        {
            this.location = location;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [viewport = "+viewport+", location = "+location+"]";
        }
    }

    public class Opening_hours
    {
        private String open_now;

        private String[] weekday_text;

        public String getOpen_now ()
        {
            return open_now;
        }

        public void setOpen_now (String open_now)
        {
            this.open_now = open_now;
        }

        public String[] getWeekday_text ()
        {
            return weekday_text;
        }

        public void setWeekday_text (String[] weekday_text)
        {
            this.weekday_text = weekday_text;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [open_now = "+open_now+", weekday_text = "+weekday_text+"]";
        }
    }

    public class Photos
    {
        private String photo_reference;

        private String height;

        private String[] html_attributions;

        private String width;

        public String getPhoto_reference ()
        {
            return photo_reference;
        }

        public void setPhoto_reference (String photo_reference)
        {
            this.photo_reference = photo_reference;
        }

        public String getHeight ()
        {
            return height;
        }

        public void setHeight (String height)
        {
            this.height = height;
        }

        public String[] getHtml_attributions ()
        {
            return html_attributions;
        }

        public void setHtml_attributions (String[] html_attributions)
        {
            this.html_attributions = html_attributions;
        }

        public String getWidth ()
        {
            return width;
        }

        public void setWidth (String width)
        {
            this.width = width;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [photo_reference = "+photo_reference+", height = "+height+", html_attributions = "+html_attributions+", width = "+width+"]";
        }
    }

    public class Viewport
    {
        private Southwest southwest;

        private Northeast northeast;

        public Southwest getSouthwest ()
        {
            return southwest;
        }

        public void setSouthwest (Southwest southwest)
        {
            this.southwest = southwest;
        }

        public Northeast getNortheast ()
        {
            return northeast;
        }

        public void setNortheast (Northeast northeast)
        {
            this.northeast = northeast;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [southwest = "+southwest+", northeast = "+northeast+"]";
        }
    }

    public class Location
    {
        private String lng;

        private String lat;

        public String getLng ()
        {
            return lng;
        }

        public void setLng (String lng)
        {
            this.lng = lng;
        }

        public String getLat ()
        {
            return lat;
        }

        public void setLat (String lat)
        {
            this.lat = lat;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [lng = "+lng+", lat = "+lat+"]";
        }
    }

    public class Southwest
    {
        private String lng;

        private String lat;

        public String getLng ()
        {
            return lng;
        }

        public void setLng (String lng)
        {
            this.lng = lng;
        }

        public String getLat ()
        {
            return lat;
        }

        public void setLat (String lat)
        {
            this.lat = lat;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [lng = "+lng+", lat = "+lat+"]";
        }
    }

    public class Northeast
    {
        private String lng;

        private String lat;

        public String getLng ()
        {
            return lng;
        }

        public void setLng (String lng)
        {
            this.lng = lng;
        }

        public String getLat ()
        {
            return lat;
        }

        public void setLat (String lat)
        {
            this.lat = lat;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [lng = "+lng+", lat = "+lat+"]";
        }
    }
}