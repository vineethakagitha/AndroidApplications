package com.example.inclass10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends Activity {
    String token;
    String email;
    String password;
    String resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EditText ema = (EditText) findViewById(R.id.lEmailEdit);
        EditText pass = (EditText) findViewById(R.id.lPasswordEdit);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPref.getString("Token", "");
        if(!token.equals(""))
        {
            Intent i = new Intent(Login.this, Notes.class);
            startActivity(i);
        }
        Button login = (Button) findViewById(R.id.lloginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = ema.getText().toString();
                password = pass.getText().toString();

                if(email.equals(""))
                {
                    ema.setError("Enter email");
                    return;
                }
                if(password.equals("")){
                    pass.setError("Enter password");
                    return;
                }
                 new LoginAsync().execute("");
            }
        });

        Button signup = (Button) findViewById(R.id.lSignUpButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,SignUp.class);
                startActivity(i);
            }
        });


    }


    class LoginAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder().add("email", email).add("password", password).build();
            Request request = new Request.Builder().url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/login").post(formBody).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                resp = response.body().string();
                Log.d("demo", resp);
                JSONObject jo = new JSONObject(resp);
                token = jo.getString("token");

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Token", token);
                boolean status = editor.commit();
                Log.d("demo","token save status "+status);

                Intent i = new Intent(Login.this, Notes.class);
                startActivity(i);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}


