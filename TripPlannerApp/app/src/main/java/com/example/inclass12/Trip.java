package com.example.inclass12;

import java.io.Serializable;
import java.util.ArrayList;

public class Trip  implements Serializable {
    String tripName;
    Location desinationCity;
    ArrayList<Location> places;


    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public Location getDesinationCity() {
        return desinationCity;
    }

    public void setDesinationCity(Location desinationCity) {
        this.desinationCity = desinationCity;
    }

    public ArrayList<Location> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Location> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "Trip{" + "tripName='" + tripName + '\'' + ", desinationCity=" + desinationCity + ", places=" + places + '}';
    }
}
