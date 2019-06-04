package com.example.inclass12;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TripAdaptor extends ArrayAdapter {
    ArrayList<Location>places;

    public TripAdaptor( Context context, int resource, ArrayList<Location> places) {
        super(context, resource, places);
        this.places = places;
    }

    @Override
    public int getCount() {
        return places.size();
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_list_view,parent,false);//set layout for displaying items
        TextView tv = (TextView)convertView.findViewById(R.id.placeName);
        tv.setText(places.get(position).name);
        return convertView;

    }
}
