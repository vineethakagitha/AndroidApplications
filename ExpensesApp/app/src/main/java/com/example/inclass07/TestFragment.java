package com.example.inclass07;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TestFragment extends Fragment {

    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_test, container, false);
        v.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextData td = (TextData)getActivity();
                td.setSearch("Added ");
                Toast.makeText(getActivity(),"Added!",Toast.LENGTH_LONG).show();
            }
        });
        return  v;
    }

    public void setTextToTV(String s)
    {
        TextView tv = (TextView)getView().findViewById(R.id.name);
        tv.setText(s);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public static interface TextData{
        public void setSearch(String s);
    }


}
