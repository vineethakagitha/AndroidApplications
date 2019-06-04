package com.example.inclass07;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class AddExpense extends Fragment {

    ArrayList<String> cats = null;


    private OnFragmentInteractionListener mListener;



    public AddExpense() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(@Nullable Bundle args) {

        cats =  args.getStringArrayList("Categories");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_expense, container, false);

        final Expense e = new Expense();

        final Spinner spinner = (Spinner)v.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                e.category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               e.category = parent.getItemAtPosition(0).toString();
            }

        });

        Button add = (Button)v.findViewById(R.id.addExp);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText)getView().findViewById(R.id.expName);
                e.exName = name.getText().toString();
                if(e.exName == "") {
                    name.setError("Enter expense name");
                    return;
                }
                EditText amt = (EditText)getView().findViewById(R.id.expAmt);
                if(amt.getText().toString().equals("")) {
                    amt.setError("Enter amount");
                    return;
                }
                else{e.amount = Double.parseDouble(amt.getText().toString());}

                if(e.category == null)
                {
                    TextView ca = (TextView)getView().findViewById(R.id.categoryLabel);
                    ca.setError("Select a Catergory");
                }

                mListener = (OnFragmentInteractionListener)getActivity();
                mListener.addExpense(e);
            }
        });

        Button cancel = (Button)v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener = (OnFragmentInteractionListener)getActivity();
                mListener.returnBack();
            }
        });

        return v;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void addExpense(Expense e);
        void returnBack();
    }
}
