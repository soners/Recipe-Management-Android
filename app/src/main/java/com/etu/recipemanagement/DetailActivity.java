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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.etu.recipemanagement.Data.IP;
import static com.etu.recipemanagement.Data.user;

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
    private FirebaseStorage storage;
    private StorageReference reference;

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


        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();


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
            @SuppressLint("StaticFieldLeak")
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void,Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        HttpURLConnection con;
                        StringBuilder sb = new StringBuilder();
                        sb.append(IP);
                        sb.append("/api_add_details_recipe/");
                        sb.append(recipeId);
                        // description
                        sb.append("?description=");
                        sb.append(description.getText().toString());

                        // ingredients
                        String inner_ingredients = "";
                        for (String ing : ingredientsNames){
                            inner_ingredients +=ing;
                        }
                        sb.append("&ingredients=");
                        sb.append(inner_ingredients);

                        // cooking steps
                        String inner_cooking_steps = "";
                        for (String stp : cookingStepList){
                            inner_cooking_steps += stp;
                        }
                        sb.append("&cooking_steps=");
                        sb.append(inner_cooking_steps);

                        // ingredients photos
                        String [] ingrediets_photos_urls = new String[selectedIngredientsPhotosList.size()];
                        int ing_i = 1;
                        for (Bitmap ing_photo : selectedIngredientsPhotosList) {

                            final StorageReference mountainsRef = reference.child(Data.user.getId()+"/"+recipeId+"/ing_photo"+ing_i+".jpg");
                            ingrediets_photos_urls[ing_i-1] = Data.user.getId()+"/"+recipeId+"/ing_photo"+ing_i+".jpg";

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ing_photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = mountainsRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                    // ...
                                }
                            });

                            ing_i++;
                            Thread.sleep(1000);
                        }
                        String full_ingrediets_photos_urls = "";
                        for (int j=0;j<ingrediets_photos_urls.length;j++){
                            if(j == ingrediets_photos_urls.length-1) {
                                full_ingrediets_photos_urls += ingrediets_photos_urls[j];
                                break;
                            }

                            full_ingrediets_photos_urls += ingrediets_photos_urls[j] + ",";
                        }
                        sb.append("&ingredients_photos=");
                        sb.append(full_ingrediets_photos_urls);

                        // cooking steps photos
                        String [] cooking_steps_photos_urls = new String[selectedCookingStepsPhotosList.size()];
                        int cook_i = 1;
                        for (Bitmap cook_photo : selectedCookingStepsPhotosList) {

                            final StorageReference mountainsRef = reference.child(Data.user.getId()+"/"+recipeId+"/cook_photo"+cook_i+".jpg");
                            cooking_steps_photos_urls[cook_i-1] = Data.user.getId()+"/"+recipeId+"/cook_photo"+cook_i+".jpg";

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            cook_photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = mountainsRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                    // ...
                                }
                            });

                            cook_i++;
                            Thread.sleep(1000);
                        }
                        String full_cooking_steps_photos_urls = "";
                        for (int j=0;j<cooking_steps_photos_urls.length;j++){
                            if(j == cooking_steps_photos_urls.length-1) {
                                full_cooking_steps_photos_urls += cooking_steps_photos_urls[j];
                                break;
                            }

                            full_cooking_steps_photos_urls += cooking_steps_photos_urls[j] + ",";
                        }
                        sb.append("&cooking_steps_photos=");
                        sb.append(full_cooking_steps_photos_urls);

                        // final photos
                        String [] final_photos_urls = new String[selectedFinalPhotosList.size()];
                        int final_i = 1;
                        for (Bitmap final_photo : selectedCookingStepsPhotosList) {

                            final StorageReference mountainsRef = reference.child(Data.user.getId()+"/"+recipeId+"/final_photo"+final_i+".jpg");
                            final_photos_urls[final_i-1] = Data.user.getId()+"/"+recipeId+"/final_photo"+final_i+".jpg";

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            final_photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = mountainsRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                    // ...
                                }
                            });

                            final_i++;
                            Thread.sleep(1000);
                        }
                        String full_final_photos_urls = "";
                        for (int j=0;j<final_photos_urls.length;j++){
                            if(j == final_photos_urls.length-1) {
                                full_final_photos_urls += final_photos_urls[j];
                                break;
                            }

                            full_final_photos_urls += final_photos_urls[j] + ",";
                        }
                        sb.append("&final_photos=");
                        sb.append(full_final_photos_urls);


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
            finish();
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
