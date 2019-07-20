package com.etu.recipemanagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.etu.recipemanagement.Data.IP;

public class DetailActivity extends AppCompatActivity {

    private LinearLayout ingredients;
    private LinearLayout details;
    private LinearLayout cookingSteps;
    private ArrayList<String> ingredientsNames;
    private EditText ingredientName;
    private EditText description;
    private IngredientAdapter ingredientAdapter;
    private ListView ingredientsListView;
    private List<Bitmap> selectedIngredientsPhotosList;
    private List<Bitmap> selectedCookingStepsPhotosList;
    private List<Bitmap> selectedFinalPhotosList;
    private ArrayList<String> cookingStepList;
    private EditText cookingStepName;
    private CookingStepsAdapter cookingStepsAdapter;
    private ListView cookingStepsListView;
    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ingredientName = findViewById(R.id.ingredientNameInput);
        cookingSteps = findViewById(R.id.cooking_steps);
        ingredientsNames = new ArrayList<>();
        ingredientAdapter = new IngredientAdapter(this, ingredientsNames);
        ingredientsListView = findViewById(R.id.ingredientsListView);
        selectedIngredientsPhotosList = new ArrayList<>();
        selectedCookingStepsPhotosList = new ArrayList<>();
        selectedFinalPhotosList = new ArrayList<>();
        cookingStepName = findViewById(R.id.cookingStepsNameInput);
        cookingStepList = new ArrayList<>();
        cookingStepsAdapter = new CookingStepsAdapter(this, cookingStepList);
        cookingStepsListView = findViewById(R.id.cookingStepsListView);
        description = findViewById(R.id.editTextDescription);

        init();
    }

    private void init() {

        recipeId = getIntent().getIntExtra("id", 0);

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

        Button addCookingSteps = findViewById(R.id.addCookingSteps);
        addCookingSteps.setOnClickListener((v) -> {
            cookingSteps.setVisibility(View.VISIBLE);
            details.setVisibility(View.GONE);
        });

        Button cookingStepsBack = findViewById(R.id.cooking_steps_back);
        cookingStepsBack.setOnClickListener((v) -> {
            cookingSteps.setVisibility(View.GONE);
            details.setVisibility(View.VISIBLE);
        });

        Button addCookingStepButton = findViewById(R.id.add_cooking_step);
        addCookingStepButton.setOnClickListener((v) -> {
            cookingStepList.add(cookingStepName.getText().toString());
            cookingStepsAdapter = new CookingStepsAdapter(this,cookingStepList);
            cookingStepsListView.setAdapter(cookingStepsAdapter);
        });

        Button addCookingStepsPhotos = findViewById(R.id.addCookingStepsPhotos);
        addCookingStepsPhotos.setOnClickListener((v) -> {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},3);
            } else {
                Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(toGallery,4);
            }
        });

        Button addFinalPhotos = findViewById(R.id.addFinalPhoto);
        addFinalPhotos.setOnClickListener((v) -> {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},5);
            } else {
                Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(toGallery,6);
            }
        });

        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener((v) -> {
            Toast.makeText(getApplicationContext(),"asdjhajkshdkjashdkj",Toast.LENGTH_SHORT).show();
            @SuppressLint("StaticFieldLeak")
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void,Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(),"serhat",Toast.LENGTH_SHORT).show());

                    try {
                        HttpURLConnection con;
                        StringBuilder sb = new StringBuilder();
                        sb.append("http://10.5.38.161:8000");
                        sb.append("/api_add_details_recipe/");
                        sb.append(recipeId);
                        sb.append("?description=");
                        sb.append(description.getText().toString());
                        String inner_ingredients = "";
                        for (String ing : ingredientsNames){
                            inner_ingredients +=ing;
                        }
                        sb.append("&ingredients=");
                        sb.append(inner_ingredients);
                        String inner_cooking_steps = "";
                        for (String stp : cookingStepList){
                            inner_cooking_steps += stp;
                        }
                        sb.append("&cooking_steps=");
                        sb.append(inner_cooking_steps);
                        Log.e("AHAHAHAHAHAHAH",sb.toString());

                        URL url = new URL(sb.toString());

                        con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");


                        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        StringBuilder content = new StringBuilder();
                        String line;
                        while (null != (line = br.readLine())) {
                            content.append(line);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            task.execute();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(toGallery,2);
            }
        } else if(requestCode == 3) {
            Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(toGallery,4);
        } else if(requestCode == 5) {
            Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(toGallery,6);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap selectedPhoto;
        if(requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            try {
                selectedPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                selectedIngredientsPhotosList.add(selectedPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if(requestCode == 4 && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            try {
                selectedPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                selectedCookingStepsPhotosList.add(selectedPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if(requestCode == 6 && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            try {
                selectedPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                selectedFinalPhotosList.add(selectedPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
