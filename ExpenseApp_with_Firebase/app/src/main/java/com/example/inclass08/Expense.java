package com.example.inclass08;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class Expense implements Serializable, Comparator<Expense>{

    public String exName;
    public String category;
    public double amount;
    public Date date;
    public String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compare(Expense o1, Expense o2) {
        if(o1.key.equals(o2.key))
        {
            return 1;
        }
        else
            return 0;
    }

    public class ExpenseDetails{

    }

    public Expense() {

        this.date = Calendar.getInstance().getTime();
    }

    @Override
    public String toString() {
        return "Expense{" + "exName='" + exName + '\'' + ", category='" + category + '\'' + ", amount=" + amount + '}';
    }

    public Expense(String exName, String category, double amount)

    {
        this.exName = exName;
        this.category = category;
        this.amount = amount;
        this.date = Calendar.getInstance().getTime();
    }


}
