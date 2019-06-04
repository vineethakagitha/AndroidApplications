package com.example.inclass07;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExpenseApp.OnFragmentInteractionListener,AddExpense.OnFragmentInteractionListener,ShowExpense.OnFragmentInteractionListener{


    ArrayList<String> categories = null;
    ArrayList<Expense> expenses = null;
    ExpenseApp shFragment;
    AddExpense adFragment;
    ShowExpense showFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categories = new ArrayList<String>();
        categories.add("Groceries");
        categories.add("Invoice");
        categories.add("Transportation");
        categories.add("Shopping");
        categories.add("Rent");
        categories.add("Trips");
        categories.add("Utilities");
        categories.add("Other");

        expenses = new ArrayList<Expense>();


        shFragment = new ExpenseApp();
        Bundle args = new Bundle();
        args.putParcelableArrayList("Expenses",expenses);
        shFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,shFragment,"ExpenseApp")
                .commit();


    }


    @Override
    public void callAddExpense() {
        adFragment = new AddExpense();
        Bundle args = new Bundle();
        args.putStringArrayList("Categories",categories);
        adFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,adFragment,"addExpense")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showExpenseDetails(Expense e) {
        Log.d("Demo",e.toString());
        showFragment = new ShowExpense();
        Bundle args = new Bundle();
        args.putParcelable("Expense",e);
        showFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,showFragment,"showExpense")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void removeExpense(int pos) {
        for(int i=0;i<expenses.size();i++)
        {
            Log.d("demo deletion",expenses.get(i).toString());
        }

    }

    @Override
    public void addExpense(Expense e) {
        expenses.add(new Expense(e.exName,e.category,e.amount));
        for(int i=0;i<expenses.size();i++)
        {
            Log.d("demo addition",expenses.get(i).toString());
        }
        Bundle args = new Bundle();
        args.putParcelableArrayList("Expenses",expenses);
        shFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,shFragment,"ExpenseApp")
                .commit();

    }

    @Override
    public void returnBack() {
        for(int i=0;i<expenses.size();i++)
        {
            Log.d("demo cancel",expenses.get(i).toString());
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void close() {
        for(int i=0;i<expenses.size();i++)
        {
            Log.d("demo close",expenses.get(i).toString());
        }
        getSupportFragmentManager().popBackStack();
    }
}
