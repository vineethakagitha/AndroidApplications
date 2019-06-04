package com.example.inclass05;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class AsynchGetKeyword extends AsyncTask<String,Void,String[]> {

    keyword keydata;

    public AsynchGetKeyword(keyword keydata) {
        this.keydata = keydata;
    }

    @Override
    protected String[] doInBackground(String... strings) {


        try {
            //StringBuilder keys = new StringBuilder();
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader buf = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String key = buf.readLine();
            String[] keys = key.split(";");
            Log.d("//demo",key);
            return keys;

        } catch (MalformedURLException e) {
            Log.d("//demo",e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("//demo",e.toString());
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(String[] strings) {

        keydata.handleKeyword(strings);
    }

    public static interface keyword
    {
            public void handleKeyword(String[] str);
    }


}