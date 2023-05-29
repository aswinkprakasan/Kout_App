package com.example.koutapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManageAccountFragment extends Fragment {

    EditText uploadName, uploadEmail, uploadNumber;
    Button saveButton;
    FloatingActionButton editFAB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_account, container, false);

        uploadName = view.findViewById(R.id.upload_name);
        uploadEmail = view.findViewById(R.id.upload_email);
        uploadNumber = view.findViewById(R.id.upload_number);
        editFAB = view.findViewById(R.id.edit_fab);

        uploadName.setEnabled(false);
        uploadEmail.setEnabled(false);
        uploadNumber.setEnabled(false);

        editFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadName.setEnabled(true);
                uploadEmail.setEnabled(true);
                uploadNumber.setEnabled(true);
            }
        });


        return view;
    }
}