package model;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Location {
    private double locationLat;
    private double locationLng;

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }

    @JsonSetter("")
    public void setLocationLatString(){

    }
}
