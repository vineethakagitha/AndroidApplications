package com.example.inclass12;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ShowTrip extends AppCompatActivity implements OnMapReadyCallback {

    Trip trip;
    ArrayList<LatLng> latLngs;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_trip);
        latLngs = new ArrayList<>();
        trip = (Trip) getIntent().getExtras().getSerializable("Trip");
        for(Location p : trip.getPlaces())
        {
            latLngs.add(new LatLng(p.latitude,p.longitude));
        }


        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        ArrayList<Location> places = trip.getPlaces();
        int i=0;
        for(LatLng p : latLngs)
        {
            mMap.addMarker(new MarkerOptions().position(p).title(places.get(i).name));
            i++;
        }
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder  l = new LatLngBounds.Builder();
                for(LatLng p : latLngs)
                {
                    l.include(p);
                }
                LatLngBounds bounds = l.build();
                mMap.setLatLngBoundsForCameraTarget(bounds);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,20));
            }
        });


    }
}


