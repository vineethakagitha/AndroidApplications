package com.example.inclass03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayMyProf extends AppCompatActivity implements View.OnClickListener {

    Student stu;
    String img_res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_my_prof);
        setTitle("Display My Profile");

        findViewById(R.id.edit).setOnClickListener(this);

        stu = (Student)getIntent().getExtras().getSerializable(MainActivity.student_key);
        img_res = getIntent().getExtras().getString(MainActivity.image_key);
        Log.d("Student on display",stu.toString());
        TextView fname = (TextView)findViewById(R.id.fullName);
        String fullname = stu.getFname()+" "+stu.getLname();
        if (stu.getFname().length() > 15)
            fullname = stu.getFname()+"\n"+stu.getLname();
        fname.setText(fullname);
        TextView stid = (TextView)findViewById(R.id.idValue);
        stid.setText(stu.getId());
        TextView dept = (TextView)findViewById(R.id.departmentName);
        dept.setText(stu.getDept());

        ImageView avatar = (ImageView)findViewById(R.id.selectedavatar);
        if(img_res.equals("avatar_f_1"))
            avatar.setImageResource(R.drawable.avatar_f_1);
        else if(img_res.equals("avatar_f_2"))
            avatar.setImageResource(R.drawable.avatar_f_2);
        else if(img_res.equals("avatar_f_3"))
            avatar.setImageResource(R.drawable.avatar_f_3);
        else if(img_res.equals("avatar_m_1"))
            avatar.setImageResource(R.drawable.avatar_m_1);
        else if(img_res.equals("avatar_m_2"))
            avatar.setImageResource(R.drawable.avatar_m_2);
        else if(img_res.equals("avatar_m_3"))
            avatar.setImageResource(R.drawable.avatar_m_3);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Log.d("Student on edit",stu.toString());
        intent.putExtra(MainActivity.student_key,stu);
        setResult(RESULT_OK,intent);
        finish();
    }

}
