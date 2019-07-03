package com.etu.recipemanagement;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.etu.recipemanagement.Data.IP;

public class LoginActivity extends AppCompatActivity {

    private String name, email, pass;
    private Button login, register, register_back, register_action;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login_login);
        register = findViewById(R.id.login_register);
        login.setOnClickListener(v -> {
            email = ((EditText)findViewById(R.id.login_email)).getText().toString();
            pass = ((EditText)findViewById(R.id.login_password)).getText().toString();
            if(email.isEmpty())
                Toast.makeText(getApplicationContext(), "Please fill the email box",
                        Toast.LENGTH_SHORT).show();
            else if(pass.isEmpty())
                Toast.makeText(getApplicationContext(), "Please enter your password",
                        Toast.LENGTH_SHORT).show();
            else {
                @SuppressLint("StaticFieldLeak")
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void,Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            HttpURLConnection con;
                            URL url = new URL(IP + "/api_login/?email=" +
                                    email + "&password=" + pass);
                            con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");

                            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                            StringBuilder content = new StringBuilder();
                            String line;
                            while (null != (line = br.readLine())) {
                                content.append(line);
                            }


                            JSONObject json = new JSONObject(content.toString());
                            String id = json.getInt("id") + "";
                            if(id.equals("0")) {
                                runOnUiThread(() -> {
                                    try {
                                        Toast.makeText(getApplicationContext(),
                                                json.getString("status"), Toast.LENGTH_SHORT).show();
                                    } catch(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            } else {
                                String name = json.getString("name");
                                Data.user = new User(id, name, email);
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                runOnUiThread(() -> {
                                    try {
                                        Toast.makeText(getApplicationContext(),
                                                "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
                task.execute();
            }
        });


        register.setOnClickListener(v -> {
            LinearLayout register_layout = findViewById(R.id.register_layout);
            LinearLayout login_layout = findViewById(R.id.login_layout);
            login_layout.setVisibility(View.GONE);
            register_layout.setVisibility(View.VISIBLE);
        });

        register_action = findViewById(R.id.register_action);
        register_action.setOnClickListener(view -> {
            name = ((EditText)findViewById(R.id.register_name)).getText().toString();
            email = ((EditText)findViewById(R.id.register_email)).getText().toString();
            pass = ((EditText)findViewById(R.id.register_password)).getText().toString();
            register(name, email, pass);

        });

        setBackButtons();


    }

    public void register(String r_name, String r_email, String r_pass) {

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void,Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    HttpURLConnection con;
                    URL url = new URL(IP + "/api_signup/?name=" +
                            r_name + "&email=" + r_email + "&password=" + r_pass);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    StringBuilder content = new StringBuilder();
                    String line;
                    while (null != (line = br.readLine())) {
                        content.append(line);
                    }


                    JSONObject json = new JSONObject(content.toString());
                    String id = json.getInt("id") + "";
                    if(id.equals("0")) {
                        runOnUiThread(() -> {
                            try {
                                Toast.makeText(getApplicationContext(),
                                        json.getString("status"), Toast.LENGTH_SHORT).show();
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        String name = json.getString("name");
                        String email = json.getString("email");
                        Data.user = new User(id, name, email);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.execute();
    }


    public void setBackButtons() {
        register_back = findViewById(R.id.register_back);
        register_back.setOnClickListener(v -> {
            LinearLayout register_layout = findViewById(R.id.register_layout);
            LinearLayout login_layout = findViewById(R.id.login_layout);
            login_layout.setVisibility(View.VISIBLE);
            register_layout.setVisibility(View.GONE);
        });
    }

    @Override
    public void onStart() {
        super.onStart();




    }
}
