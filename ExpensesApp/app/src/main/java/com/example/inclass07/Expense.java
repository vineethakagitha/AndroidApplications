package com.example.inclass07;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Expense implements Parcelable{

    String exName;
    String category;
    double amount;
    Date date;

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


    protected Expense(Parcel in) {
        exName = in.readString();
        category = in.readString();
        amount = in.readDouble();
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(exName);
        dest.writeString(category);
        dest.writeDouble(amount);
    }
}
