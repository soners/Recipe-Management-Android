package com.etu.recipemanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class DetailActivity extends AppCompatActivity {

    private EditText textDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textDetails = findViewById(R.id.textDetails);
        textDetails.setMinLines(1);
        textDetails.setMaxLines(Integer.MAX_VALUE);



    }
}
