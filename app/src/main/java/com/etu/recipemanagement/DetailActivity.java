package com.etu.recipemanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Scroller;

public class DetailActivity extends AppCompatActivity {


    private EditText textDescription;
    private ListView ingredientsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textDescription = findViewById(R.id.editTextDescription);
        ingredientsListView = findViewById(R.id.ingredientsList);

    }


    public void addItemToIngredientList(View view) {
        Intent toIngredient = new Intent(this, IngredientActivity.class);
    }
}
