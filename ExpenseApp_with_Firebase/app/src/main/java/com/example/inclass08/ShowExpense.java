package com.example.inclass08;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ShowExpense extends Fragment {

    Expense e;
    View view;


    private OnFragmentInteractionListener mListener;

    public ShowExpense() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_show_expense, container, false);
        TextView n = (TextView)view.findViewById(R.id.name);
        n.setText(e.exName);

        TextView c = (TextView)view.findViewById(R.id.category);
        c.setText(e.category);

        TextView a = (TextView)view.findViewById(R.id.amount);
        a.setText("$"+e.amount);

        TextView d = (TextView)view.findViewById(R.id.date);
        SimpleDateFormat spf= new SimpleDateFormat("MM/dd/yyyy");
        d.setText(spf.format(e.date));

        Button cl = (Button)view.findViewById(R.id.close);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mListener = (OnFragmentInteractionListener)getActivity();
//                mListener.close();
                Log.d("demo","Closing show expense details.");
                getFragmentManager().popBackStack();
            }
        });
        Button ed = (Button)view.findViewById(R.id.editExp);
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener = (OnFragmentInteractionListener)getActivity();
                mListener.edit(e);
            }
        });
        return view;
    }

    public void showExpense(Expense e)
    {
         this.e = e;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void close();
        void edit(Expense e);
    }
}