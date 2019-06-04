package com.example.inclass07;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ExpenseAdaptor extends ArrayAdapter<Expense> {

    Expense exp = null;

    public ExpenseAdaptor( Context context, int resource,  List<Expense> objects) {
        super(context, resource, objects);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Expense exp = getItem(position);

        ViewHolder viewHolder = null;

        if(null == convertView){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_list_layout,parent,false);

            viewHolder = new ViewHolder();

            viewHolder.expName = convertView.findViewById(R.id.expName);
            viewHolder.expAmt = convertView.findViewById(R.id.expAmt);

            viewHolder.expName.setText(exp.exName);
            viewHolder.expAmt.setText("$"+exp.amount);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public static class ViewHolder{

        TextView expName;
        TextView expAmt;

        public  ViewHolder(){

        }

    }
}
