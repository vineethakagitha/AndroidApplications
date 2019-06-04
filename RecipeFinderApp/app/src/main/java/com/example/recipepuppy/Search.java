package com.example.recipepuppy;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Search extends Fragment implements SearchRecipe.Recipies{


    ArrayList<String> ingredients;
    RecyclerView mRecyclerView;
    IngredientsAdaptor mAdapter;
    ArrayList<Recipe> recipes;
    String recpName = "";
    ProgressDialog dia;

    private OnFragmentInteractionListener mListener;

    public Search() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        ingredients = args.getStringArrayList("Ingredients");
        if(mAdapter!=null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Recipe Puppy");
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        mListener = (OnFragmentInteractionListener)getActivity();

        dia = new ProgressDialog(getActivity());
        dia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dia.setTitle("Loading...");
        dia.setMessage("Loading...");
        dia.setCancelable(false);
        dia.setIndeterminate(false);
        dia.setMax(100);
        dia.setProgress(0);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.ingredientsList);
        mRecyclerView.setHasFixedSize(true); //for efficiency purpose

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new IngredientsAdaptor(ingredients,(IngredientsAdaptor.updateData)getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        recipes = new ArrayList<>();

        v.findViewById(R.id.frag_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               v.clearFocus();
            }
        });

        Button search = (Button)v.findViewById(R.id.searchBtn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText r = (EditText)getView().findViewById(R.id.dish);
                String ing = String.join(",",ingredients);
                String url = "http://www.recipepuppy.com/api/?i="+ing+"&q="+r.getText().toString();
                Log.d("demo","URL is : "+url);
                dia.show();
                new SearchRecipe(Search.this).execute(url);
            }
        });
        return  v;
    }


    @Override
    public void parseRecipeList(String res) {

        JSONObject root = null;
        try {
            root = new JSONObject(res);
            JSONArray arr = root.getJSONArray("results");
            for(int i=0;i<arr.length();i++){
                JSONObject jo = arr.getJSONObject(i);
                Recipe r = new Recipe(jo.optString("title"),
                        jo.optString("ingredients"),
                        jo.optString("href"),
                        jo.optString("thumbnail"));
                Log.d("demo","recipe - "+r.toString());
                recipes.add(r);
                int pro = ((i+1)/arr.length())*100;
                dia.setProgress(pro);
                //o.name = jo.optString("name")  to avoid JSONParse Exception
            }
            dia.dismiss();
            if(arr.length() == 0)
            {
                Toast.makeText(getActivity(),"No results Found, Please try again.",Toast.LENGTH_LONG).show();
            }
            else {
                mListener.onFragmentInteraction(recipes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(ArrayList<Recipe> re);
    }
}
