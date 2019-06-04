package com.example.recipepuppy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdaptor extends RecyclerView.Adapter<RecipeAdaptor.ViewHolder> {
    ArrayList<Recipe> recipes;
    Activity a;


    public RecipeAdaptor(ArrayList<Recipe> recipes,Activity a)
    {
        this.recipes = recipes;
        this.a = a;
    }

    @NonNull
    @SuppressLint("RestrictedApi")
    @Override
    public RecipeAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_layout, viewGroup, false);
        RecipeAdaptor.ViewHolder viewHolder =  new RecipeAdaptor.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Recipe r = recipes.get(i);
        viewHolder.title.setText(r.title);
        viewHolder.ingredients.setText(r.ingredients);
        SpannableString content = new SpannableString(r.recipeUrl);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        viewHolder.url.setText(content);
        if(!recipes.get(i).imgUrl.equals("")){
                Picasso.get().load(recipes.get(i).imgUrl)
                        .resize(170, 146)
                        .centerCrop()
                        .into(viewHolder.image);
         }

         viewHolder.url.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 TextView t = (TextView)v;
                 Intent i = new Intent(Intent.ACTION_VIEW);
                 i.setData(Uri.parse(t.getText().toString()));
                 a.startActivity(i);

             }
         });
    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView ingredients;
        TextView url;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            ingredients = (TextView) itemView.findViewById(R.id.ingredients);
            url = (TextView) itemView.findViewById(R.id.url);
            image = (ImageView) itemView.findViewById(R.id.image);
        }


    }

}
