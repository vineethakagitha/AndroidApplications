package com.example.inclass03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SelectAvatar extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);
        setTitle("Select Avatar");

        findViewById(R.id.avatar_f_1).setOnClickListener(this);
        findViewById(R.id.avatar_f_2).setOnClickListener(this);
        findViewById(R.id.avatar_f_3).setOnClickListener(this);
        findViewById(R.id.avatar_m_1).setOnClickListener(this);
        findViewById(R.id.avatar_m_2).setOnClickListener(this);
        findViewById(R.id.avatar_m_3).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

         ImageView im  = (ImageView )v;
         Intent intent = new Intent();
         String name = getResources().getResourceEntryName(im.getId());
         intent.putExtra(MainActivity.image_key,name);
         setResult(RESULT_OK,intent);
         finish();
    }

}
