package com.example.inclass06;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncGetNews.News{
     String[] categories = null;
     String category = "";
     int numOfArticles = 0;
     ArrayList<Article> articles;
     int index = 0;
    ProgressBar progress = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        categories = new String[5];
        categories[0] = "Business";
        categories[1] = "Entertainment";
        categories[2] = "General";
        categories[3] = "Health";
        categories[4] = "Science";
        articles = new ArrayList<Article>();

        Button go = (Button)findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Category")
                        .setItems(categories, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                category = categories[which];
                                TextView tv = (TextView)findViewById(R.id.category);
                                tv.setText(category);
                                if(isConnectedToNetwork() == true) {
                                    progress = (ProgressBar) findViewById(R.id.progressBar);
                                    progress.setVisibility(View.VISIBLE);
                                    new AsyncGetNews(MainActivity.this).execute("https://newsapi.org/v2/top-headlines?country=us&apiKey=f37423f8f34c4a5aaf615cad86e87253", category);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        ImageButton prev = (ImageButton) findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = (index+numOfArticles-1)%numOfArticles;
                //progress = (ProgressBar) findViewById(R.id.progressBar);
                progress.setVisibility(View.VISIBLE);
                TextView t = (TextView)findViewById(R.id.title);
                t.setText(articles.get(index).title);
                TextView p = (TextView)findViewById(R.id.pubAt);
                p.setText(articles.get(index).publishedAt);
                TextView d = (TextView)findViewById(R.id.description);
                if(articles.get(index).content != null)
                    d.setText(articles.get(index).content);
                else
                    d.setText("");
                ImageView im = (ImageView)findViewById(R.id.img) ;
                if(articles.get(index).imgUrl  != null) {
                    Picasso.get()
                            .load(articles.get(index).imgUrl)
                            .resize(im.getWidth(), im.getHeight())
                            .placeholder(R.drawable.loading)
                            .centerCrop()
                            .into(im);
                    //progress = (ProgressBar) findViewById(R.id.progressBar);
                    progress.setVisibility(View.INVISIBLE);
                }
                else{
                    Log.d("demo","No Image");
                    im.setImageResource( android.R.drawable.picture_frame);
                }
                TextView s = (TextView) findViewById(R.id.articlePage);
                s.setText((index + 1) + " out of " + numOfArticles);
            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                index = (index+numOfArticles+1)%numOfArticles;
                //progress = (ProgressBar) findViewById(R.id.progressBar);
                progress.setVisibility(View.VISIBLE);
                TextView t = (TextView)findViewById(R.id.title);
                t.setText(articles.get(index).title);
                TextView p = (TextView)findViewById(R.id.pubAt);
                p.setText(articles.get(index).publishedAt);
               // progress.setVisibility(View.VISIBLE);
                TextView d = (TextView)findViewById(R.id.description);
                if(articles.get(index).content != null)
                    d.setText(articles.get(index).content);
                else
                    d.setText("");
                ImageView im = (ImageView)findViewById(R.id.img) ;
                if(articles.get(index).imgUrl  != null) {
                    Picasso.get()
                            .load(articles.get(index).imgUrl)
                            .placeholder(R.drawable.loading)
                            .resize(im.getWidth(), im.getHeight())
                            .centerCrop()
                            .into(im);
                }
                else{
                    Log.d("demo","No Image");
                    im.setImageResource( android.R.drawable.picture_frame);
                }
                //progress = (ProgressBar) findViewById(R.id.progressBar);
                progress.setVisibility(View.INVISIBLE);
                TextView s = (TextView)findViewById(R.id.articlePage);
                s.setText((index+1)+" out of "+numOfArticles);
            }
        });



    }

   boolean isConnectedToNetwork()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwinfo = cm.getActiveNetworkInfo();
        if(nwinfo == null || nwinfo.isConnected()!=true)
            return false;
        return true;
    }

    @Override
    public void function(String json) {

        JSONObject root = null;
        try {
            root = new JSONObject(json);
            JSONArray arr = root.getJSONArray("articles");
            //progress = (ProgressBar) findViewById(R.id.progressBar);
            TextView t = (TextView) findViewById(R.id.title);
            TextView p = (TextView) findViewById(R.id.pubAt);
            TextView d = (TextView) findViewById(R.id.description);
            ImageView im = (ImageView) findViewById(R.id.img);
            TextView s = (TextView) findViewById(R.id.articlePage);
            //progress = (ProgressBar) findViewById(R.id.progressBar);
            //progress.setVisibility(View.VISIBLE);
            if(arr.length() != 0) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jo = arr.getJSONObject(i);
                    Article a = new Article();
                    a.title = jo.getString("title");
                    a.publishedAt = jo.getString("publishedAt");
                    a.content = jo.getString("content");
                    a.imgUrl = jo.getString("urlToImage");
                    articles.add(a);
                }
                numOfArticles = arr.length();
                t.setText(articles.get(0).title);
                p.setText(articles.get(0).publishedAt);
                if (articles.get(0).content != null)
                    d.setText(articles.get(0).content);
                else
                    d.setText("");
                if (articles.get(index).imgUrl != null) {
                    Picasso.get()
                            .load(articles.get(0).imgUrl)
                            .resize(im.getWidth(), im.getHeight())
                            .centerCrop()
                            .into(im);
                    //progress = (ProgressBar) findViewById(R.id.progressBar);
                    progress.setVisibility(View.INVISIBLE);
                } else {
                    Log.d("demo","No Image");
                    im.setImageResource(android.R.drawable.picture_frame);
                }
                s.setText("1 out of " + numOfArticles);
            }
            else{
                t.setText("");
                p.setText("");
                d.setText("");
                im.setImageResource(android.R.drawable.picture_frame);
                s.setText("");
                Toast.makeText(getApplicationContext(),"No News Found",Toast.LENGTH_LONG).show();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
