package com.example.koutapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UploadQuoteActivity extends AppCompatActivity {

    AutoCompleteTextView genre;
    EditText author, quote;
    Button btnQuote;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_quote);

        genre = findViewById(R.id.genre);
        author = findViewById(R.id.author);
        quote = findViewById(R.id.quote);
        btnQuote = findViewById(R.id.btn_quote);

        sp = getSharedPreferences("UserPreference", Context.MODE_PRIVATE);

        btnQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Set<String> valueSet = sp.getStringSet("categories", new HashSet<>());
                List<String> valuesList = new ArrayList<>(valueSet);

                String newCategory = genre.getText().toString();
                if (!newCategory.isEmpty()) {
                    valuesList.add(newCategory);
                }

                SharedPreferences.Editor editor = sp.edit();
                editor.putStringSet("categories", new HashSet<>(valuesList));
                editor.apply();


                Toast.makeText(UploadQuoteActivity.this, "Added in shared preference", Toast.LENGTH_SHORT).show();
            }
        });


        Set<String> valueSet = sp.getStringSet("categories", new HashSet<>());
        List<String> valuesList = new ArrayList<>(valueSet);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, valuesList);
        genre.setAdapter(adapter);


    }
}