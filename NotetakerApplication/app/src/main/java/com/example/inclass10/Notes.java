package com.example.inclass10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class Notes extends Activity implements NotesAdaptor.DeleteNoteI
{
    String token;
    OkHttpClient client;
    String resp;
    RecyclerView rv;
    NotesAdaptor adap;
    ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);

        notes = new ArrayList<>();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPref.getString("Token", "");

        rv = (RecyclerView)findViewById(R.id.notesList);
        rv.setHasFixedSize(true); //for efficiency purpose
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adap = new NotesAdaptor(notes,this);
        rv.setAdapter(adap);

        adap.notifyDataSetChanged();

        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/me")
                .addHeader("x-access-token", token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
             resp = response.body().string();
             Log.d("demo","me -> response ,"+resp);
            JSONObject jo = new JSONObject(resp);
            String name = jo.getString("name");
            TextView nm = (TextView)findViewById(R.id.loginNameTextView);
            nm.setText("Hey "+name+"!!!");

           // new getNotesAsync().execute("");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/me

        Button addn = (Button)findViewById(R.id.addNoteButton);
        addn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Notes.this,AddNote.class);
                startActivity(i);
            }
        });

        ImageButton logout = (ImageButton)findViewById(R.id.logout);
        logout.setImageResource(R.drawable.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove token
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder().url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/logout")
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    resp = response.body().string();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("Token");
                boolean status = editor.commit();
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new getNotesAsync().execute("");
    }

    @Override
    public void delNote(String id) {
      new DelNoteAsync().execute(id);
    }

    class getNotesAsync extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/getall")
                    .addHeader("x-access-token",token)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                resp = response.body().string();
                Log.d("demo", resp);
                JSONObject jo = new JSONObject(resp);
                JSONArray notesarr = jo.getJSONArray("notes");
                notes.clear();
                for(int each = 0;each<notesarr.length();each++)
                {
                    JSONObject obj = notesarr.getJSONObject(each);
                    Note n = new Note();
                    n.setId(obj.getString("_id"));
                    n.setUserid(obj.getString("userId"));
                    n.setText(obj.getString("text"));
                    notes.add(n);
                }

            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Session ended. Please Login.",Toast.LENGTH_LONG).show();
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("Token");
                boolean status = editor.commit();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Session ended. Please Login.",Toast.LENGTH_LONG).show();
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("Token");
                boolean status = editor.commit();
                finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adap.notifyDataSetChanged();
        }
    }


    class DelNoteAsync extends AsyncTask<String,Void,String>{

        String token;
        @Override
        protected String doInBackground(String... strings) {

            client = new OkHttpClient();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            token = sharedPref.getString("Token", "");
            Request request = new Request.Builder()
                    .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/delete?msgId="+strings[0])
                    .addHeader("x-access-token", token)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                resp = response.body().string();
                Log.d("demo","delete note -> response ,"+resp);
                JSONObject jo = new JSONObject(resp);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new getNotesAsync().execute("");
        }
    }
}
