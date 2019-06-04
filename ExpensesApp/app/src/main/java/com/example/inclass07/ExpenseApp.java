package com.example.inclass07;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ExpenseApp extends Fragment {

    ArrayList<Expense> expenses;
    ExpenseAdaptor adap;

    private OnFragmentInteractionListener mListener;

    public ExpenseApp(){

    }

    @Override
    public void setArguments(@Nullable Bundle args) {

        expenses =  args.getParcelableArrayList("Expenses");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_expenses_app, container, false);
        TextView ls = (TextView)v.findViewById(R.id.listStatus);
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
        adap.setNotifyOnChange(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense e = (Expense)parent.getItemAtPosition(position);
                Log.d("demo",e.toString());
                mListener = (OnFragmentInteractionListener)getActivity();
                mListener.showExpenseDetails(e);
            }
        });


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                expenses.remove(position);
                adap.notifyDataSetChanged();
                TextView ls = (TextView)getView().findViewById(R.id.listStatus);
                if(expenses.size() == 0)
                {
                    ls.setText("There is no expense to show,\n" + "Please add your expenses from the menu.");
                }
                else{
                    ls.setText("");
                }
                Toast.makeText(getActivity(),"Expense Deleted",Toast.LENGTH_LONG).show();
                mListener = (OnFragmentInteractionListener)getActivity();
                mListener.removeExpense(position);

                return false;
            }
        });

        Button add = (Button)v.findViewById(R.id.addBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener = (OnFragmentInteractionListener)getActivity();
                mListener.callAddExpense();
            }
        });


        return v;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void callAddExpense();
        void showExpenseDetails(Expense e);
        void removeExpense(int i);
    }
}
