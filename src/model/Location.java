package model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Location {
    private String lat;
    private String lng;

    public Location(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Location() {
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    //    public double getLocationLat() {
//        return locationLat;
//    }
//
//    public void setLocationLat(double locationLat) {
//        this.locationLat = locationLat;
//    }
//
//    public double getLocationLng() {
//        return locationLng;
//    }
//
//    public void setLocationLng(double locationLng) {
//        this.locationLng = locationLng;
//    }
//
//    @JsonSetter("lat")
//    public void setLocationLatString(String lat){
//        this.locationLat = Double.parseDouble(lat);
//    }
//
//    @JsonSetter("lng")
//    public void setLocationLngString(String lng){
//        this.locationLng = Double.parseDouble(lng);
//    }
//
//    @JsonGetter("lat")
//    public String getLocationLatString() {
//        return Double.toString(this.locationLat);
//    }
}
