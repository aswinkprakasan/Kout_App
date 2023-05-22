package com.example.koutapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginTabFragment extends Fragment {

    EditText email, password;
    ProgressBar progressBar;
    Button button;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login_tab, container, false);

        email = view.findViewById(R.id.login_email);
        password = view.findViewById(R.id.login_password);
        progressBar = view.findViewById(R.id.progressBar);
        button = view.findViewById(R.id.login_button);
        mAuth = FirebaseAuth.getInstance();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String mailid, pass, repass;
                mailid = String.valueOf(email.getText());
                pass = String.valueOf(password.getText());

                if (TextUtils.isEmpty(mailid) || TextUtils.isEmpty(pass)){
                    Toast.makeText(getActivity(), "don't leave the blank empty", Toast.LENGTH_SHORT).show();
                }

                mAuth.signInWithEmailAndPassword(mailid, pass)
                        .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Login Successful ",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                    startActivity(intent);
                                    requireActivity().finish();

                                } else {
                                    Toast.makeText(requireContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        return view;
    }
}