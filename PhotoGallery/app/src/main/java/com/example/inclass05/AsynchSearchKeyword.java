package com.example.inclass05;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AsynchSearchKeyword extends AsyncTask<String,Void,ArrayList<String>>
{

    Images img;

    public AsynchSearchKeyword(Images img) {
        this.img = img;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        ArrayList<String> s = new ArrayList<>();
        try {
            //StringBuilder keys = new StringBuilder();
            String sn = strings[0]+"?keyword="+strings[1];
            URL url = new URL(sn);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader buf = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String imglink = "";
            while((imglink = buf.readLine())!= null)
            {
              s.add(imglink);
            }

        } catch (MalformedURLException e) {
            Log.d("//demo",e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("//demo",e.toString());
            e.printStackTrace();
        }
        return s;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        img.handleImgLinks(strings);
    }

    public static interface Images
    {
        public void handleImgLinks(ArrayList<String> str);
    }


}
