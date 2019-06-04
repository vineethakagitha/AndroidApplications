package com.example.inclass02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("BMI Calculator");

        findViewById(R.id.BMIbutton).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Toast t;
        Button btn = (Button) v;
        EditText wtet = (EditText)findViewById(R.id.weightInput);
        String wt = wtet.getText().toString();
        if(wt.equals(""))
        {
            wtet.setError("Should not be empty");
            return;
        }
        float weight = Float.parseFloat(wt);
        EditText ftet = (EditText)findViewById(R.id.feetInput);
        EditText inet = (EditText)findViewById(R.id.inchInput);
        String ft = ftet.getText().toString();
        if(ft.equals(""))
        {
            ftet.setError("Cannot be empty");
            return;
        }
        String in = inet.getText().toString();
        if(in.equals(""))
        {
            inet.setError("Cannot be empty");
            return;
        }
        int feet;
        int inch;
        try {
            feet = Integer.parseInt(ft);
            inch = Integer.parseInt(in);
        }
        catch (Exception e){
            t = Toast.makeText(getApplicationContext(), "Invalid input",
                    Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        if(inch >= 12 )
        {
            t = Toast.makeText(getApplicationContext(), "inch value should be less than 12",
                    Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        float height = (feet*12)+inch;
        if(height == 0 )
        {
            t = Toast.makeText(getApplicationContext(), "Height cannot be zero! please enter a value for feet or inch",
                    Toast.LENGTH_SHORT);
            t.show();
            ftet.setError("Cannot be zero");
            inet.setError("Cannot be zero");
            return;
        }
        ftet.setError(null);
        inet.setError(null);
        float BMI = (weight/(height*height))*703;

        TextView tv = (TextView) findViewById(R.id.displayOutput);
        tv.setText("Your BMI : "+ BMI+"\n"+"You are ");

        if(BMI <= 18.5)
        {
            tv.append("Underweight");
        }
        else if(BMI > 18.5 && BMI < 25)
        {
            tv.append("Normalweight");
        }
        else if(BMI >= 25 && BMI <30)
        {
            tv.append("Overweight");
        }
        else{
            tv.append("Obese");
        }


    }
}
