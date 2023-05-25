package com.example.koutapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class RecognizedTextActivity extends AppCompatActivity {

    EditText recText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognized_text);

        Intent intent = getIntent();
        String recognText = intent.getStringExtra("Key");

        recText = findViewById(R.id.recText);

        recText.setText(recognText);


    }
}