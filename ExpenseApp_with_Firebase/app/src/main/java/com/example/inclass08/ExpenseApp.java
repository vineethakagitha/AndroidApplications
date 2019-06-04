package com.example.inclass08;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class ExpenseApp extends Fragment {

    ArrayList<Expense> expenses;
    HashMap<String,Expense> expen;
    ExpenseAdaptor adap;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenses");
    ExpenseI ei;


    public ExpenseApp(){
          expenses = new ArrayList<Expense>();
          expen = new HashMap<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_expense_app, container, false);
        TextView ls = (TextView)v.findViewById(R.id.listStatus);
        ei = (ExpenseI)getActivity();
        if(expenses.size() == 0)
        {
            ls.setText("There is no expense to show,\n" + "Please add your expenses from the menu.");
        }
        else{
            ls.setText("");
        }
        ListView lv = (ListView)v.findViewById(R.id.expList);

        adap = new ExpenseAdaptor(getActivity(),R.layout.expense_list_layout,expenses);
        lv.setAdapter(adap);
        //adap.setNotifyOnChange(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense e = (Expense)parent.getItemAtPosition(position);
                Log.d("demo",e.toString());
                ei = (ExpenseI)getActivity();
                ei.showExpense(e);
            }
        });


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                myRef.child(expenses.get(position).getKey()).removeValue();
                expenses.remove(position);
                adap.notifyDataSetChanged();
                return false;
            }
        });

        FloatingActionButton add = (FloatingActionButton)v.findViewById(R.id.addBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ei.addExpense();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenses.clear();
                for(DataSnapshot d : dataSnapshot.getChildren())
                {
                      String key = d.getKey();
                      Expense e = d.getValue(Expense.class);
                      expen.put(key,e);
                      expenses.add(e);
                }
                adap.notifyDataSetChanged();

                //Toast.makeText(getActivity(),"Expense Deleted",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d("demo","Child Added.");
//                String key = dataSnapshot.getKey();
//                Expense e = dataSnapshot.getValue(Expense.class);
//                if(!expen.containsKey(key))
//                {
//                    expen.put(key,e);
//                    expenses.add(e);
//                }
//                adap.notifyDataSetChanged();
//                TextView ls = (TextView)getView().findViewById(R.id.listStatus);
//                if(expenses.size() == 0)
//                {
//                    ls.setText("There is no expense to show,\n" + "Please add your expenses from the menu.");
//                }
//                else{
//                    ls.setText("");
//                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

//                String key = dataSnapshot.getKey();
//                Expense e = dataSnapshot.getValue(Expense.class);
//                expen.put(key,e);
//                for(int i=0;i<expenses.size();i++)
//                {
//                    Expense each = expenses.get(i);
//                    if(each.getKey().equals(key))
//                    {
//                        expenses.remove(i);
//                        expenses.add(i,e);
//                        break;
//                    }
//                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                String key = dataSnapshot.getKey();
//                Expense e = dataSnapshot.getValue(Expense.class);
//                expen.remove(key);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView ls = (TextView) getView().findViewById(R.id.listStatus);
        if(expenses.size() == 0)
        {
            ls.setText("There is no expense to show,\n" + "Please add your expenses from the menu.");
        }
        else{
            ls.setText("");
        }
    }

    public interface ExpenseI{
        public void addExpense();
        public void showExpense(Expense e);
    }

}