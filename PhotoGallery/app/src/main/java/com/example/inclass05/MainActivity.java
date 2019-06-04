package com.example.inclass05;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsynchGetKeyword.keyword,AsynchSearchKeyword.Images,AsynchGetImage.Images {

    String[] keywords;
    String keyword = "";
    ArrayList<String> imgLinks;
    ImageButton left = null;
    ImageButton right = null;
    int index = 0;
    int numOfImages = 0;
    ProgressBar progress = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        imgLinks = new ArrayList<String>();
        TextView tv = (TextView)findViewById(R.id.loadStatus);
        tv.setVisibility(View.INVISIBLE);
        left = (ImageButton)findViewById(R.id.prev);
        right = (ImageButton)findViewById(R.id.next);
        left.setEnabled(false);
        right.setEnabled(false);
        if(isConnectedToNetwork()) {
            new AsynchGetKeyword(MainActivity.this).execute("http://dev.theappsdr.com/apis/photos/keywords.php");

        }
        else{
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        Button go = (Button)findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Keyword")
                        .setItems(keywords, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                keyword = keywords[which];
                                TextView tv = (TextView)findViewById(R.id.keyword);
                                tv.setText(keyword);
                                new AsynchSearchKeyword(MainActivity.this).execute("http://dev.theappsdr.com/apis/photos/index.php",keyword);
                            }
                        });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        left.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                TextView tv = (TextView)findViewById(R.id.loadStatus);
                tv.setVisibility(View.VISIBLE);
                tv.setText("Loading Previous image");
                ImageView im = (ImageView)findViewById(R.id.photos);
                im.setVisibility(View.INVISIBLE);
                new AsynchGetImage(MainActivity.this).execute(imgLinks.get((index+numOfImages-1)%numOfImages));
                index = (index+numOfImages-1)%numOfImages;
            }
        });
        right.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                TextView tv = (TextView)findViewById(R.id.loadStatus);
                tv.setVisibility(View.VISIBLE);
                tv.setText("Loading next image");
                ImageView im = (ImageView)findViewById(R.id.photos);
                im.setVisibility(View.INVISIBLE);
                new AsynchGetImage(MainActivity.this).execute(imgLinks.get((index+numOfImages+1)%numOfImages));
                index = (index+numOfImages+1)%numOfImages;
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
    public void handleKeyword(String[] str) {
        this.keywords = str;

    }

    @Override
    public void handleImgLinks(ArrayList<String> str) {
        numOfImages = str.size();
        if(numOfImages == 0)
        {
            ImageView im = (ImageView)findViewById(R.id.photos);
            //"@drawable/editbox_dropdown_light_frame"
            im.setImageResource( android.R.drawable.editbox_dropdown_light_frame);
            Toast.makeText(getApplicationContext(),"No Images Found", Toast.LENGTH_SHORT).show();
            return;
        }
        if(numOfImages > 1) {
            left.setEnabled(true);
            right.setEnabled(true);
        }
        imgLinks = str;
        progress.setVisibility(View.VISIBLE);
        TextView tv = (TextView)findViewById(R.id.loadStatus);
        tv.setVisibility(View.VISIBLE);
        tv.setText("Loading ...");
        ImageView im = (ImageView)findViewById(R.id.photos);
        im.setVisibility(View.INVISIBLE);
        new AsynchGetImage(MainActivity.this).execute(imgLinks.get(0));
    }

    @Override
    public void showImage(Bitmap b) {
        progress.setVisibility(View.INVISIBLE);
        TextView tv = (TextView)findViewById(R.id.loadStatus);
        tv.setVisibility(View.INVISIBLE);
        ImageView im = (ImageView)findViewById(R.id.photos);
        im.setVisibility(View.VISIBLE);
        im.setImageBitmap(b);
    }
}
