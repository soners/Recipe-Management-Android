package com.etu.recipemanagement;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout last;
    private Button add_recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        addRecipe();



    }

    private void addRecipe() {
        add_recipe = findViewById(R.id.add_recipe);
        add_recipe.setOnClickListener(v -> {
            if(last != null) last.setVisibility(View.GONE);
            LinearLayout add_recipe_action = findViewById(R.id.add_recipe_action);
            add_recipe_action.setVisibility(View.VISIBLE);
            last = add_recipe_action;
        });

        EditText new_recipe_name = findViewById(R.id.new_recipe_name);
        EditText new_recipe_details = findViewById(R.id.new_recipe_details);
        Button push_recipe = findViewById(R.id.push_recipe);

        push_recipe.setOnClickListener(task -> {
            String name = new_recipe_name.getText().toString();
            String details = new_recipe_details.getText().toString();

            if(name != null && details != null) {
                AsyncTask<Void, Void, Void> art = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            String link = "http://35.184.224.87:8000/api_add_recipe/?user_id=" + Data.user.getId() +"&name=" + name + "&details="
                                    + details;
                            URL url = new URL(link.replace(" ","%20"));
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("GET");

                            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                            StringBuilder content = new StringBuilder();
                            String line;
                            while (null != (line = br.readLine())) {
                                content.append(line);
                            }
                            runOnUiThread(() -> {
                                LinearLayout recipe_layout = findViewById(R.id.recipe_layout);
                                LinearLayout add_recipe_layout = findViewById(R.id.add_recipe_action);
                                recipe_layout.setVisibility(View.VISIBLE);
                                add_recipe_layout.setVisibility(View.GONE);
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
                art.execute();
            } else {
                new_recipe_name.setError("Please insert a name");
                new_recipe_details.setError("Please give details of the recipe");
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.my_recipes) {
            LinearLayout my_recipes = findViewById(R.id.recipe_layout);
            if(last != null) last.setVisibility(View.GONE);
            my_recipes.setVisibility(View.VISIBLE);
            last = my_recipes;
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
