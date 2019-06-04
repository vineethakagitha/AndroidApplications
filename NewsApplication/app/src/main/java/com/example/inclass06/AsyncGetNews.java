package com.example.inclass06;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AsyncGetNews  extends AsyncTask<String,Void,String> {

    News instance;
    public AsyncGetNews(News i){
        instance = i;
    }
    protected String doInBackground(String... strings) {

        String result = "";
        try {
            //StringBuilder keys = new StringBuilder();
            URL url = new URL(strings[0]+"&category="+strings[1]);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                result = IOUtils.toString(con.getInputStream(),"UTF8");

            }

        } catch (MalformedURLException e) {
            Log.d("//demo",e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("//demo",e.toString());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        instance.function(s);
    }

    public static interface News
    {
        public void function(String str);
    }

}
