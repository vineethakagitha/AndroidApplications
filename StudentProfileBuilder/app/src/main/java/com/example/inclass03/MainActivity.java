package com.example.inclass03;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    static Student stu;
    static int req_code = 0;
    static int image_selected = 0;
    static String image_key = "image";
    static String student_key = "student";
    static String img_id = "id";
    ImageView avatar ;
    EditText fname;
    EditText lname;
    EditText id;
    RadioGroup dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Profile");

        fname = (EditText) findViewById(R.id.firstName);
        lname = (EditText) findViewById(R.id.lastName);
        id = (EditText) findViewById(R.id.studentID);
        dept = (RadioGroup) findViewById(R.id.department);

        avatar = (ImageView)findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                   req_code = 0;
                   Intent i = new Intent(MainActivity.this,SelectAvatar.class);
                   startActivityForResult(i,req_code);
                   Toast.makeText(getApplicationContext(),"Select Image", Toast.LENGTH_SHORT).show();
            }
        });
         Button sav = (Button)findViewById(R.id.save);
         sav.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Toast t;
                if(image_selected == 0)
                {
                    Toast.makeText(getApplicationContext(),"Please select a image",Toast.LENGTH_LONG).show();
                    return;
                }
                String firname = fname.getText().toString();
                String lasname = lname.getText().toString();
                Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                Matcher ms = ps.matcher(firname);
                boolean bs = ms.matches();
                if (bs == false) {
                    fname.setError("First name cannot be empty and no invalid characters in the name");
                    return;
                }
                Pattern ls = Pattern.compile("^[a-zA-Z ]*$");
                ms = ls.matcher(lasname);
                bs = ms.matches();
                if (bs == false) {
                    lname.setError("Invalid characters in the name");
                    return;
                }
                String stid = id.getText().toString();
                if(stid.equals("") ) {
                    id.setError("Cannot be Empty");
                    return;
                }
                if(stid.length() != 9) {
                    id.setError("Invalid Student ID! student ID should be 9 digits");
                    return;
                }
                if(dept.getCheckedRadioButtonId() == -1)
                {
                    RadioButton other = (RadioButton)findViewById(R.id.other);
                    other.setError("Select department");
                    return;
                }
                else
                {
                    RadioButton other = (RadioButton)findViewById(R.id.other);
                    other.setError(null);
                }
                RadioButton deptname = (RadioButton) findViewById(dept.getCheckedRadioButtonId());

                stu = new Student(firname, lasname, stid, deptname.getText().toString());
                //Toast.makeText(getApplicationContext(), stu.toString(), Toast.LENGTH_LONG).show();
                req_code = 1;
                Intent i = new Intent(MainActivity.this,DisplayMyProf.class);
                i.putExtra(image_key,img_id);
                i.putExtra(student_key, stu);
                startActivityForResult(i,req_code);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0)
        {
            if(resultCode == RESULT_OK)
            {
                img_id = data.getExtras().getString(image_key);
                image_selected = 1;
                if(img_id.equals("avatar_f_1"))
                   avatar.setImageResource(R.drawable.avatar_f_1);
                else if(img_id.equals("avatar_f_2"))
                    avatar.setImageResource(R.drawable.avatar_f_2);
                else if(img_id.equals("avatar_f_3"))
                    avatar.setImageResource(R.drawable.avatar_f_3);
                else if(img_id.equals("avatar_m_1"))
                    avatar.setImageResource(R.drawable.avatar_m_1);
                else if(img_id.equals("avatar_m_2"))
                    avatar.setImageResource(R.drawable.avatar_m_2);
                else if(img_id.equals("avatar_m_3"))
                    avatar.setImageResource(R.drawable.avatar_m_3);
                else
                    image_selected = 0;
            }
        }
        else if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                stu = (Student)data.getExtras().getSerializable(student_key);

                fname.setText(stu.getFname());
                lname.setText(stu.getLname());
                id.setText(stu.getId());
                if(stu.getDept().equals("CS"))
                    dept.check(R.id.CS);
                else if(stu.getDept().equals("SIS"))
                    dept.check(R.id.SIS);
                else if(stu.getDept().equals("BIO"))
                    dept.check(R.id.BIO);
                else
                    dept.check(R.id.other);
            }
        }
    }


}
