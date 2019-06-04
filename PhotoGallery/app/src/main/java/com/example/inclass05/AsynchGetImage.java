package com.example.inclass05;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsynchGetImage extends AsyncTask<String,Void, Bitmap> {


    Images img;

    public AsynchGetImage(Images img) {
        this.img = img;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        try {
            //StringBuilder keys = new StringBuilder();

            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            Bitmap bits = BitmapFactory.decodeStream(con.getInputStream());
            return bits;

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
    protected void onPostExecute(Bitmap b) {
        Log.d("demo","showing image");
        img.showImage(b);
    }

    public static interface Images
    {
        public void showImage(Bitmap b);
    }

}
