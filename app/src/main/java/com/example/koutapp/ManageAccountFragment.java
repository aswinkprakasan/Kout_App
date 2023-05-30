package com.example.koutapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ManageAccountFragment extends Fragment {

    EditText uploadName, uploadEmail, uploadNumber;
    Button saveButton;
    FloatingActionButton editFAB;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    DocumentReference doc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_account, container, false);

        uploadName = view.findViewById(R.id.upload_name);
        uploadEmail = view.findViewById(R.id.upload_email);
        uploadNumber = view.findViewById(R.id.upload_number);
        editFAB = view.findViewById(R.id.edit_fab);
        saveButton = view.findViewById(R.id.save_button);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String userID = mAuth.getCurrentUser().getUid();

        doc = fStore.collection("users").document(userID);

        uploadName.setEnabled(false);
        uploadEmail.setEnabled(false);
        uploadNumber.setEnabled(false);
        saveButton.setEnabled(false);

        getuserdata();

        editFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadName.setEnabled(true);
                uploadNumber.setEnabled(true);
                saveButton.setEnabled(true);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name, num;
                name = uploadName.getText().toString();
                num = uploadNumber.getText().toString();


                Map<String, Object> upData = new HashMap<>();
                upData.put("name", name);
                upData.put("phonenumber", num);

                doc.update(upData);

                Toast.makeText(getActivity(), "Account updated", Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }

    private void getuserdata() {
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String email = documentSnapshot.getString("mailId");
                    String name1 = documentSnapshot.getString("name");
                    String phn = documentSnapshot.getString("phonenumber");

                    if (email != null) {
                        uploadEmail.setText(email);
                    }
                    if(name1 != null)
                    {
                        uploadName.setText(name1);
                    }
                    if(phn!=null)
                    {
                        uploadNumber.setText(phn);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}