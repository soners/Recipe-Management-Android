package com.etu.recipemanagement;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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


        return null;
    }
}
