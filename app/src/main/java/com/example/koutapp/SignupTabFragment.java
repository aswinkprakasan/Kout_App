package com.example.koutapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignupTabFragment extends Fragment {
    EditText email, password, repassword;
    ProgressBar progressBar;
    Button button;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_signup_tab, container, false);

        email = view.findViewById(R.id.signup_email);
        password = view.findViewById(R.id.signup_password);
        repassword = view.findViewById(R.id.signup_confirm);
        progressBar = view.findViewById(R.id.progressBar);
        button = view.findViewById(R.id.signup_button);
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String mailid, pass, repass;
                mailid = String.valueOf(email.getText());
                pass = String.valueOf(password.getText());
                repass = String.valueOf(repassword.getText());

                if (TextUtils.isEmpty(mailid) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass)) {
                    Toast.makeText(getActivity(), "don't leave the blank empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    mAuth.createUserWithEmailAndPassword(mailid, pass).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(requireContext(), "Account created", Toast.LENGTH_SHORT).show();
                                userID = mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("mailId", mailid);
                                user.put("password", pass);

                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "onSuccess: account created for userid" + userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(requireContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        return view;
    }
}