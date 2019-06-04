package com.example.inclass10;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddNote extends Activity
{

    String resp;
    String token;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        Button addn = (Button)findViewById(R.id.addBtn);
        addn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText not = (EditText)findViewById(R.id.note);
                text = not.getText().toString();
                if(text.equals("")){
                    not.setError("Enter note text");
                    return;
                }
                new saveNoteAsync().execute("");
            }
        });

        Button cancel = (Button)findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    class saveNoteAsync extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            token = sharedPref.getString("Token", "");
            RequestBody formBody = new FormBody.Builder()
                    .add("text", text)
                    .build();
            Request request = new Request.Builder().url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/post")
                    .addHeader("x-access-token", token)
                    .post(formBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                resp = response.body().string();
                Log.d("demo", resp);
                JSONObject jo = new JSONObject(resp);
                JSONObject no = jo.getJSONObject("note");
                Note n = new Note();
                n.setId(no.getString("_id"));
                n.setUserid(no.getString("userId"));
                n.setText(no.getString("text"));
                Log.d("demo","note addes. "+n.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finish();
            return null;
        }
    }
}
