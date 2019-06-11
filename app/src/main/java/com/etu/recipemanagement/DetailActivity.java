package com.etu.recipemanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private EditText textDescription;
    private EditText ingredientName;
    private ListView ingredientsListView;
    private List<String> ingredientsNameList;
    private ArrayAdapter ingredientsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textDescription = findViewById(R.id.editTextDescription);
        ingredientName = findViewById(R.id.ingredientNameInput);
        ingredientsListView = findViewById(R.id.ingredintsListView);
        ingredientsNameList = new ArrayList<>();

    }

    public void addItemToIngredientList(View view) {
        if(ingredientsNameList.get(0) == null) {
            // first time
            ingredientsNameList.add(ingredientName.getText().toString());
            ingredientsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredientsNameList);
            ingredientsListView.setAdapter(ingredientsAdapter);
        } else {
            ingredientsNameList.add(ingredientName.getText().toString());
            ingredientsAdapter.notifyDataSetChanged();
        }
    }


}
