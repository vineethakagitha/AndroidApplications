package com.example.recipepuppy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IngredientsAdaptor.updateData,Search.OnFragmentInteractionListener{
     ArrayList<String> ingredients;
     Search s;
     Recipes r;
     Bundle arg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ingredients = new ArrayList<String>();
        ingredients.add("");
        arg = new Bundle();
        arg.putStringArrayList("Ingredients",ingredients);
        s = new Search();
        s.setArguments(arg);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,s,"SearchFragment")
                .commit();
    }


    @Override
    public void onFragmentInteraction(ArrayList<Recipe> re) {

        arg.putParcelableArrayList("Recipes",re);
        r = new Recipes();
        r.setArguments(arg);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("SearchFragment")
                .replace(R.id.container,r,"RecipeFragment")
                .commit();
    }

    public void updateIngredient(String str,int pos)
    {
        ingredients.set(pos,str);
        Log.d("demo","updated at"+pos+"-"+ingredients.get(pos));
        arg.putStringArrayList("Ingredients",ingredients);
        s.setArguments(arg);
    }

    public void addIngredient()
    {
        ingredients.add("");
        Log.d("demo","Added new at "+(ingredients.size()-1));
        arg.putStringArrayList("Ingredients",ingredients);
        s.setArguments(arg);
    }


    public void deleteIngredient(int pos) {
        ingredients.remove(pos);
        Log.d("demo","deleted at"+pos);
        arg.putStringArrayList("Ingredients",ingredients);
        s.setArguments(arg);
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Recipes fragment = (Recipes) getSupportFragmentManager().findFragmentByTag("RecipeFragment");
        Log.d("demo","Back button pressed");
        if (fragment.isAdded()) {
            Log.d("demo","Back button pressed, yes its recipe fragment");
            getSupportFragmentManager().popBackStack();
        }
        else{
            Log.d("demo","Back button pressed, No it is main page");
            super.onBackPressed();
        }
    }

}
