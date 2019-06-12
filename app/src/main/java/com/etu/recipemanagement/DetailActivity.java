package com.etu.recipemanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private LinearLayout ingredients;
    private LinearLayout details;
    private ArrayList<String> ingredientsNames;
    private EditText ingredientName;
    private IngredientAdapter ingredientAdapter;
    private ListView ingredientsListView;
    private Bitmap selectedPhoto;
    private List<Bitmap> selectedPhotosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ingredientName = findViewById(R.id.ingredientNameInput);
        ingredientsNames = new ArrayList<>();
        ingredientAdapter = new IngredientAdapter(this, ingredientsNames);
        ingredientsListView = findViewById(R.id.ingredientsListView);
        selectedPhotosList = new ArrayList<>();
        init();
    }

    private void init() {
        details = findViewById(R.id.details);
        ingredients = findViewById(R.id.ingredient);

        Button ingredientButton = findViewById(R.id.add_ingredient);
        ingredientButton.setOnClickListener((v) -> {
            ingredients.setVisibility(View.VISIBLE);
            details.setVisibility(View.GONE);
        });

        Button ingredientBack = findViewById(R.id.ing_back);
        ingredientBack.setOnClickListener((v) -> {
            ingredients.setVisibility(View.GONE);
            details.setVisibility(View.VISIBLE);
        });

        Button addIngredientButton = findViewById(R.id.add_ingredient_list);
        addIngredientButton.setOnClickListener((v) -> {
            ingredientsNames.add(ingredientName.getText().toString());
            ingredientAdapter = new IngredientAdapter(this,ingredientsNames);
            ingredientsListView.setAdapter(ingredientAdapter);
        });

        Button addIngredientPhotos = findViewById(R.id.addIngredientPhotos);
        addIngredientPhotos.setOnClickListener((v) -> {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
            } else {
                Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(toGallery,2);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(toGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            try {
                selectedPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                selectedPhotosList.add(selectedPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
