package com.etu.recipemanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private LinearLayout ingredients;
    private LinearLayout details;
    private ArrayList<String> ingredientsNames;
    private EditText ingredientName;
    private IngredientAdapter ingredientAdapter;
    private ListView ingredientsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ingredientName = findViewById(R.id.ingredientNameInput);
        ingredientsNames = new ArrayList<>();
        ingredientAdapter = new IngredientAdapter(this, ingredientsNames);
        ingredientsListView = findViewById(R.id.ingredientsListView);
        init();
    }

    private void init() {
        details = findViewById(R.id.details);
        ingredients = findViewById(R.id.ingredient);

        Button button = findViewById(R.id.add_ingredient);
        button.setOnClickListener((v) -> {
            ingredients.setVisibility(View.VISIBLE);
            details.setVisibility(View.GONE);
        });

        Button back = findViewById(R.id.ing_back);
        back.setOnClickListener((v) -> {
            ingredients.setVisibility(View.GONE);
            details.setVisibility(View.VISIBLE);
        });
    }

    public void addIngredientsToList(View view) {
        ingredientsNames.add(ingredientName.getText().toString());
        ingredientAdapter = new IngredientAdapter(this,ingredientsNames);
        ingredientsListView.setAdapter(ingredientAdapter);
    }
}
