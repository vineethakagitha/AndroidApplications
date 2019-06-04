package com.example.inclass07;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    private OnFragmentInteractionListener mListener;

    public ShowExpense() {
        // Required empty public constructor
    }

    public void setArguments(@Nullable Bundle args) {
        e = new Expense();
        e =  args.getParcelable("Expense");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_show_expense, container, false);

        TextView n = (TextView)v.findViewById(R.id.name);
        n.setText(e.exName);

        TextView c = (TextView)v.findViewById(R.id.category);
        c.setText(e.category);

        TextView a = (TextView)v.findViewById(R.id.amount);
        a.setText("$"+e.amount);

        TextView d = (TextView)v.findViewById(R.id.date);
        SimpleDateFormat spf= new SimpleDateFormat("MM/dd/yyyy");
        d.setText(spf.format(e.date));

        Button cl = (Button)v.findViewById(R.id.close);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener = (OnFragmentInteractionListener)getActivity();
                mListener.close();
            }
        });

        return v;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void close();
    }
}
