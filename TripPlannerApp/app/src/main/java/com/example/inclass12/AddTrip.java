package com.example.inclass12;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddTrip extends Activity {

    EditText tn;
    EditText dc;
    ImageButton search;
    ListView places;
    Location[] locs;
    Location city;
    String category="";
    String cityResp = null;
    TripAdaptor adaptor;
    ArrayList<Location> nearPlaces;
    String[] cities;
    String[] placeNames;
    ArrayList<Location> allPlaces;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_view);

        // AIzaSyC1qNWLC-29M3duWnNZorjpkHymotMcfQw

        tn = (EditText) findViewById(R.id.editTripName);
        dc = (EditText)findViewById(R.id.editDestCity);
        city = new Location();
        places = (ListView)findViewById(R.id.placesList);

        nearPlaces = new ArrayList<Location>();
        allPlaces = new ArrayList<Location>();
        adaptor = new TripAdaptor(getApplicationContext(), R.layout.trip_list_view, nearPlaces);
        adaptor.setNotifyOnChange(true);
        places.setAdapter(adaptor);

        search = (ImageButton)findViewById(R.id.search);
        search.setImageResource(R.drawable.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=
                if(dc.getText().toString().equals(""))
                {
                    dc.setError("Enter city to search");
                    return;
                }
                new Cities().execute(dc.getText().toString());
            }
        });

        Spinner cat = (Spinner)findViewById(R.id.category);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        cat.setAdapter(adapter);
        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(city == null || city.name == null)
                {
                    dc.setError("Select Destination city");
                    return;
                }
                category = (String)parent.getItemAtPosition(position);
                new Places().execute(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = (String)parent.getItemAtPosition(0);
                new Places().execute(category);
            }
    });


        Button save = (Button)findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trip t = new Trip();
                if(tn.getText().toString().equals(""))
                {
                    tn.setError("Enter Trip name");
                    return;
                }
                t.tripName = tn.getText().toString();
                if(city == null || city.name == null)
                {
                    dc.setError("Select Destination city");
                    return;
                }
                t.desinationCity = city;
                if(category.equals("") || nearPlaces.size() == 0)
                {
                    Toast.makeText(getApplicationContext(),"Select category and select near by places", Toast.LENGTH_LONG).show();
                    return;
                }
                t.places = nearPlaces;
                DatabaseReference ref = myRef.child("trips");
                String key = ref.push().getKey();
                ref.child(key).setValue(t);
                finish();
            }
        });

        Button cancel = (Button)findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    class Cities extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {

            String resp = null;
            String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?" +
                    "key=AIzaSyBy7ENott3vROABHBWhrola9aX1zGUvMtI&" +
                    "inputtype=textquery&" +
                    "fields=geometry,name&" +
                    "input="+strings[0];
            Log.d("demo","URL:"+url);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                resp = response.body().string();
            }catch (IOException e) {
                    Log.d("demo","Exception in response");
                    e.printStackTrace();
            }
            Log.d("demo","response : "+resp);
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            cityResp = s;
            Log.d("demo","Processing: "+s);
            try{
                JSONObject root = new JSONObject(cityResp);
                Log.d("demo","JSON object created");
                JSONArray arr = root.getJSONArray("candidates");
                Log.d("demo","candidates array");
                locs = new Location[arr.length()];
                cities = new String[arr.length()];
                for(int i=0;i<arr.length();i++){
                    JSONObject jo = arr.getJSONObject(i);
                    JSONObject loc = jo.getJSONObject("geometry").getJSONObject("location");
                    Log.d("demo","get geometry and location");
                    Location l = new Location();
                    l.latitude = loc.getDouble("lat");
                    l.longitude = loc.getDouble("lng");
                    l.name = jo.getString("name");
                    Log.d("demo","lat,long and name");
                    locs[i] = l;
                    cities[i] = l.name;
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(AddTrip.this);
            builder.setTitle("Choose city")
                    .setItems(cities, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            city = locs[which];
                            dc.setText(city.name);
                        }
                    });
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    class Places extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String resp = null;
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                    "key=AIzaSyBy7ENott3vROABHBWhrola9aX1zGUvMtI&" +
                    "location="+city.latitude+","+city.longitude+"&"+
                    "radius=15000&"+
                    "type="+strings[0];
            Log.d("demo","places url: "+url);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url
            ).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                resp = response.body().string();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("demo","Near by places : "+resp);
            return resp;

        }

        @Override
        protected void onPostExecute(String s)
            {
                try {
                    JSONObject root = new JSONObject(s);
                    JSONArray arr = root.getJSONArray("results");
                    allPlaces.clear();
                    nearPlaces.clear();
                    placeNames = new String[arr.length()];
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jo = arr.getJSONObject(i);
                        JSONObject loc = jo.getJSONObject("geometry").getJSONObject("location");
                        Location l = new Location();
                        l.latitude = loc.getDouble("lat");
                        l.longitude = loc.getDouble("lng");
                        l.name = jo.getString("name");
                        allPlaces.add(l);
                        placeNames[i] = l.name;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTrip.this);
                // Set the dialog title
                builder.setTitle("Select places")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setMultiChoiceItems(placeNames, null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {
                                        if (isChecked) {
                                            // If the user checked the item, add it to the selected items
                                            if(nearPlaces.size() == 15)
                                            {
                                                ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                                                Toast.makeText(getApplicationContext(),"Cannot select more than 15 places",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                nearPlaces.add(allPlaces.get(which));
                                            }
                                        }
                                        else if (nearPlaces.contains(allPlaces.get(which))) {
                                            // Else, if the item is already in the array, remove it
                                            nearPlaces.remove(which);
                                        }
                                    }


                                })
                        // Set the action buttons
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK, so save the selectedItems results somewhere
                                // or return them to the component that opened the dialog

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                nearPlaces.clear();
                            }
                        }).create().show();
                Log.d("demo","Notifying places List view");
                adaptor.notifyDataSetChanged();


            }
    }
}
