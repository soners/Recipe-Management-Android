package com.etu.recipemanagement;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        int id = getIntent().getIntExtra("id", 0);
        Button button = findViewById(R.id.back);
        button.setOnClickListener(v -> finish());


        Log.d("tag: ", id + "");


        AsyncTask<Void, Void, Void> task = new AsyncTask<Void,Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {


                try {
                    HttpURLConnection con;
                    URL url = new URL("http://35.184.224.87:8000/api_recipe/" + id);

                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    StringBuilder content = new StringBuilder();
                    String line;
                    while (null != (line = br.readLine())) {
                        content.append(line);
                    }


                    JSONObject json = new JSONObject(content.toString());




                    String name = json.getString("name");
                    int id = (int) json.get("id");
                    String detail = json.getString("detail");

                    Recipe rec = new Recipe();
                    rec.setId(id);
                    rec.setDesc(detail);
                    rec.setName(name);


                    runOnUiThread(() -> {


                        TextView namee = findViewById(R.id.name);
                        TextView details = findViewById(R.id.details);
                        namee.setText(name);
                        details.setText(detail);

                        Button deleteButton = findViewById(R.id.delete);
                        deleteButton.setOnClickListener(v -> {
                            deleteRecipe(id);
                        });

                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };



        task.execute();


    }

    private void deleteRecipe(int id) {

        AsyncTask <Void, Void, Void> deleteTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {


                try {
                    HttpURLConnection con;
                    URL url = new URL("http://35.184.224.87:8000/api_delete_recipe/" + id);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    StringBuilder content = new StringBuilder();
                    String line;
                    while (null != (line = br.readLine())) {
                        content.append(line);
                    }


                    runOnUiThread(() -> {

                        Intent intent = new Intent(RecipeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }



                return null;
            }
        };

        deleteTask.execute();

    }



}
