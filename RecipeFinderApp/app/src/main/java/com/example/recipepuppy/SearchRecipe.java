package com.example.recipepuppy;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchRecipe extends AsyncTask<String,Void,String> {
    Recipies rec;

    public SearchRecipe(Recipies rec) {
        this.rec = rec;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        try {
            //StringBuilder keys = new StringBuilder();
            String sn = strings[0];
            URL url = new URL(sn);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            Log.d("//demo","URL Connected.");
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
    protected void onPostExecute(String res) {
        rec.parseRecipeList(res);
    }

    public static interface Recipies
    {
        public void parseRecipeList(String res);
    }

}
