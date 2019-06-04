package com.example.inclass08;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddExpense extends Fragment {

    Expense e;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenses");

    public AddExpense() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.expense_add_fragment, container, false);
        e = new Expense();

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


         Button addex = (Button)v.findViewById(R.id.addExp);
         addex.setOnClickListener(new View.OnClickListener() {
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

                 String key = myRef.push().getKey();
                 DatabaseReference exp = myRef.child(key);
                 e.setKey(key);
                 exp.setValue(e);
                 getFragmentManager().popBackStack();
             }
         });

         Button can = (Button)v.findViewById(R.id.cancel);
         can.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.d("demo","Cancel clicked");
                 getFragmentManager().popBackStack();
             }
         });

        return v;
    }

}
