package com.example.recipepuppy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class Recipes extends Fragment {

    ArrayList<Recipe> recipes;
    RecyclerView mRecyclerView;
    RecipeAdaptor adap;

    public Recipes() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        recipes = args.getParcelableArrayList("Recipes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Recipes");
        View v =  inflater.inflate(R.layout.fragment_recipes, container, false);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recipesList);
        mRecyclerView.setHasFixedSize(true); //for efficiency purpose

        // use a linear layout manager Horizontal
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        adap = new RecipeAdaptor(recipes,getActivity());
        mRecyclerView.setAdapter(adap);

        Button fin = (Button)v.findViewById(R.id.finish);
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return v;
    }




}
