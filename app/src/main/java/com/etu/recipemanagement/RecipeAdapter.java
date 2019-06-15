package com.etu.recipemanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Recipe> recipes;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    public RecipeAdapter(ArrayList<Recipe> recipe) {
        recipes = recipe;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return recipes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.recipe_adapter, parent, false);
        }

        // get current item to be displayed
        Recipe currentItem = (Recipe) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.name);

        // get the TextView for item name and item description
        TextView textViewItemDetails = (TextView)
                convertView.findViewById(R.id.details);




        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem.getName());

        //sets the text for item name and item description from the current item object
        textViewItemDetails.setText(currentItem.getDesc());

        // returns the view for the current row
        return convertView;
    }
}
