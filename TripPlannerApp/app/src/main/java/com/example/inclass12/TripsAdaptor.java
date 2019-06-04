package com.example.inclass12;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TripsAdaptor extends ArrayAdapter {
    ArrayList<Trip> trips;

    public TripsAdaptor( Context context, int resource, ArrayList<Trip> trips) {
        super(context, resource, trips);
        this.trips = trips;
    }

    @Override
    public int getCount() {
        return trips.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trips_view,parent,false);//set layout for displaying items
        TextView tv = (TextView)convertView.findViewById(R.id.tripName);
        tv.setText(trips.get(position).tripName);
        return convertView;

    }
}
