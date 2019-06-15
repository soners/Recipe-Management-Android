package com.etu.recipemanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
