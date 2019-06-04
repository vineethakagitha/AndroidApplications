package com.example.recipepuppy;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {

    String title;
    String ingredients;
    String recipeUrl;
    String imgUrl;

    public Recipe(String title, String ingredients, String recipeUrl, String imgUrl) {
        this.title = title;
        this.ingredients = ingredients;
        this.recipeUrl = recipeUrl;
        this.imgUrl = imgUrl;
    }

    protected Recipe(Parcel in) {
        title = in.readString();
        ingredients = in.readString();
        recipeUrl = in.readString();
        imgUrl = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public String toString() {
        return "Recipe{" + "title='" + title + '\'' + ", ingredients='" + ingredients + '\'' + ", recipeUrl='" + recipeUrl + '\'' + ", imgUrl='" + imgUrl + '\'' + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(ingredients);
        dest.writeString(recipeUrl);
        dest.writeString(imgUrl);
    }
}
