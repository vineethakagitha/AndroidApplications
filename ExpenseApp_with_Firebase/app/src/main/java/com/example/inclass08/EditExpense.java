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

import java.util.Calendar;


public class EditExpense extends Fragment {

    private OnFragmentInteractionListener mListener;
    Expense e;
    String newCat;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenses");

    public EditExpense() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_expense, container, false);

        EditText n = (EditText)view.findViewById(R.id.expName);
        n.setText(e.exName);

        Spinner c = (Spinner)view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newCat = e.category;
        c.setAdapter(adapter);
        String[] s = getResources().getStringArray(R.array.Categories);
        for(int i=0;i<s.length;i++)
        {
            if(s[i].equals(e.category)){
                c.setSelection(i);
            }
        }

        c.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newCat = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        EditText a = (EditText) view.findViewById(R.id.expAmt);
        a.setText(e.amount+"");

        Button addex = (Button)view.findViewById(R.id.editExp);
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
                e.category = newCat;
                e.date = Calendar.getInstance().getTime();
                String key = e.getKey();
                DatabaseReference exp = myRef.child(key);
                exp.setValue(e);
                getFragmentManager().popBackStack();
            }
        });

        Button cl = (Button)view.findViewById(R.id.cancel);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("demo","Cancel edit expense");
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    public void getExpense(Expense e)
    {
        this.e = e;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
