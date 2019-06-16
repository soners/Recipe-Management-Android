package com.etu.recipemanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class RecipeTask extends AsyncTask<Void, Void, Void> {


    private ListView listview;
    private Context mContext;

    public RecipeTask(ListView view) {

        listview = view;
    }

    public RecipeTask (Context context) {
        mContext = context;
    }
    public RecipeTask(ListView view, Context context) {

        listview = view;
        mContext = context;

    }


    @Override
    protected Void doInBackground(Void... voids) {

        ArrayList<Recipe> recipes = new ArrayList<>();



        try {
            HttpURLConnection con;
            URL url = new URL("http://35.184.224.87:8000/api_recipes/5/");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder content = new StringBuilder();
            String line;
            while (null != (line = br.readLine())) {
                content.append(line);
            }


            JSONObject json = new JSONObject(content.toString());

            JSONArray array = json.getJSONArray("recipes");


            for(int i=0; i<array.length(); i++) {

                JSONObject jsonobject = (JSONObject) array.get(i);
                String name = jsonobject.getString("name");
                int id = (int) jsonobject.get("id");
                String detail = jsonobject.getString("details");

                //System.out.println("id:" + id +  " name: " + name + " detail: " + detail);

                Recipe rec = new Recipe();
                rec.setId(id);
                rec.setDesc(detail);
                rec.setName(name);

                recipes.add(rec);
            }





        } catch (Exception e) {
            e.printStackTrace();
        }

        RecipeAdapter adapter = new RecipeAdapter(recipes);

        listview.setAdapter(adapter);

        return null;
    }



}
