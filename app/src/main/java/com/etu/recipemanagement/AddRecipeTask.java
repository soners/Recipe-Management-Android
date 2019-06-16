package com.etu.recipemanagement;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddRecipeTask extends AsyncTask <Void, Void, Void> {

    private String name, details;
    public AddRecipeTask() {
        this("","");
    }

    public AddRecipeTask(String r_name, String r_details) {
        name = r_name;
        details = r_details;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("35.184.224.87/api_add_recipe?name=" + name + "&details="
                    + details);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
