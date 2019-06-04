package com.example.recipepuppy;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class IngredientsAdaptor extends RecyclerView.Adapter<IngredientsAdaptor.ViewHolder> {
    ArrayList<String> ingredients;
    updateData search;


    public IngredientsAdaptor(ArrayList<String> ingredients,updateData search)
    {
           this.ingredients = ingredients;
           this.search = search;
    }

    @NonNull
    @SuppressLint("RestrictedApi")
    @Override
    public IngredientsAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredients_layout, viewGroup, false);
        ViewHolder viewHolder =  new ViewHolder(view);
        //viewHolder.ingredientText.setSelection(0);
        Log.d("demo","onCreateViewHolder");
        viewHolder.add.setVisibility(View.VISIBLE);
        viewHolder.delete.setVisibility(View.VISIBLE);
        return viewHolder;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull final IngredientsAdaptor.ViewHolder viewHolder, final int i) {
        Log.d("demo","onBindViewHolder");
        String ing = ingredients.get(i);
        viewHolder.ingredientText.setText(ing);
        if(ing.equals(""))
        {
            Log.d("demo", "Setting delete button to Invisible - " + i);
            viewHolder.delete.setVisibility(View.INVISIBLE);
            viewHolder.add.setVisibility(View.VISIBLE);
        }
        else{
            Log.d("demo", "Setting delete button to visible - " + i);
            viewHolder.add.setVisibility(View.INVISIBLE);
            viewHolder.delete.setVisibility(View.VISIBLE);
        }
        if(i == 5){
            Log.d("demo", "Last item " + i);
            viewHolder.delete.setVisibility(View.INVISIBLE);
            viewHolder.add.setVisibility(View.INVISIBLE);
        }

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "deleting - " + i);
                search.deleteIngredient(i);
            }
        });


        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                EditText text = (EditText)viewHolder.ingredientText ;
                String ingredient = text.getText().toString();

                    Log.d("demo","update ingredient at position "+i+" to value - "+ingredient);
                    search.updateIngredient(ingredient,i);

                viewHolder.add.setVisibility(View.INVISIBLE);
                Log.d("demo", "Setting delete button to visible - " + i);
                viewHolder.delete.setVisibility(View.VISIBLE);
                search.addIngredient();

            }
        });
    }


    public interface updateData{
        void updateIngredient(String s,int pos);
        void addIngredient();
        void deleteIngredient(int pos);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText ingredientText;
        FloatingActionButton add;
        FloatingActionButton delete;

        public ViewHolder(View itemView) {
            super(itemView);
            ingredientText = (EditText) itemView.findViewById(R.id.ingredientName);
            add = (FloatingActionButton)itemView.findViewById(R.id.add);
            delete = (FloatingActionButton)itemView.findViewById(R.id.delete);

            //ingredientText.setText(ingredients.get(getAdapterPosition()));

        }

    }

}
