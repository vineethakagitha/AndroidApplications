package com.example.inclass08;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExpenseApp.ExpenseI,ShowExpense.OnFragmentInteractionListener {


    ArrayList<Expense> expenses;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference expes = myRef.child("expenses");

        expenses = new ArrayList<>();
        //Bundle b = new Bundle();
        //b.putParcelableArrayList("Expenses",expenses);
        ExpenseApp expApp = new ExpenseApp();
        //expApp.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,expApp ,"ExpenseApp")
                .commit();
    }

    @Override
    public void addExpense() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new AddExpense(),"AddExpense")
                .addToBackStack("ExpenseApp")
                .commit();
    }

    @Override
    public void showExpense(Expense e) {
        ShowExpense s = new ShowExpense();
        s.showExpense(e);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,s,"ShowExpense")
                .addToBackStack("ExpenseApp")
                .commit();
    }

    @Override
    public void close() {

    }

    @Override
    public void edit(Expense e) {
        EditExpense s = new EditExpense();
        s.getExpense(e);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,s,"EditExpense")
                .addToBackStack("ExpenseApp")
                .commit();
    }
}
