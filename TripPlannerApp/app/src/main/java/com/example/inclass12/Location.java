package com.example.inclass12;

import java.io.Serializable;

public class Location implements Serializable {

    String name;
    double latitude;
    double longitude;

    public Location(){
        this.name = null;
        this.latitude= 0;
        this.longitude = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" + "latitude=" + latitude + ", longitude=" + longitude + '}';
    }
}
