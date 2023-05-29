package com.example.koutapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UploadQuoteActivity extends AppCompatActivity {

    AutoCompleteTextView genre;
    EditText author, quote;
    Button btnQuote;
    SharedPreferences sp;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_quote);

        genre = findViewById(R.id.genre);
        author = findViewById(R.id.author);
        quote = findViewById(R.id.quote);
        btnQuote = findViewById(R.id.btn_quote);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar); // Replace `toolbar` with the ID of your Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sp = getSharedPreferences("UserPreference", Context.MODE_PRIVATE);




        btnQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newCategory, author1, quote1, userID;

                newCategory = genre.getText().toString();
                author1 = author.getText().toString();
                quote1 = quote.getText().toString();

                userID = mAuth.getCurrentUser().getUid();

                DocumentReference documentReference = fStore.collection("quotes").document();
                Map<String, Object> quoteData = new HashMap<>();
                quoteData.put("quote", quote1);
                quoteData.put("author", author1);
                quoteData.put("userID", userID);
                quoteData.put("category", newCategory);

                documentReference.set(quoteData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UploadQuoteActivity.this, "Added to database", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: failed to add");
                    }
                });


                Set<String> valueSet = sp.getStringSet("categories", new HashSet<>());
                List<String> valuesList = new ArrayList<>(valueSet);


                if (!newCategory.isEmpty()) {
                    valuesList.add(newCategory);
                }

                SharedPreferences.Editor editor = sp.edit();
                editor.putStringSet("categories", new HashSet<>(valuesList));
                editor.apply();


                Toast.makeText(UploadQuoteActivity.this, "Quote added successfully", Toast.LENGTH_SHORT).show();
            }
        });


        Set<String> valueSet = sp.getStringSet("categories", new HashSet<>());
        List<String> valuesList = new ArrayList<>(valueSet);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, valuesList);
        genre.setAdapter(adapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}