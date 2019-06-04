package com.example.inclass12;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    ListView tripList;
    ArrayList<Trip> trips;
    ArrayList<String> tripnames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AIzaSyBy7ENott3vROABHBWhrola9aX1zGUvMtI

        ImageButton add = (ImageButton)findViewById(R.id.addTrip);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AddTrip.class);
                startActivity(i);
            }
        });

        trips = new ArrayList<>();
       tripList = (ListView)findViewById(R.id.activityListView);
       final TripsAdaptor adap = new TripsAdaptor(this,R.layout.trips_view,trips);
       adap.setNotifyOnChange(true);
       tripList.setAdapter(adap);



        DatabaseReference ref = myRef.child("trips");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trips.clear();
                for(DataSnapshot d : dataSnapshot.getChildren())
                {
                    String key = d.getKey();
                    Trip p = d.getValue(Trip.class);
                    trips.add(p);

                }
                adap.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this,ShowTrip.class);
                Trip t = (Trip)parent.getItemAtPosition(position);
                i.putExtra("Trip",t);
                startActivity(i);
            }
        });

    }
}
