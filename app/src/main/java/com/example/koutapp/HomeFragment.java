package com.example.koutapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeFragment extends Fragment {

    FloatingActionButton scanFab, quoteFab;
    ExtendedFloatingActionButton addFab;
    TextView scanText, quoteText;
    Boolean isAllFABVisible;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        scanFab = view.findViewById(R.id.scan_fab);
        quoteFab = view.findViewById(R.id.quote_fab);
        addFab = view.findViewById(R.id.add_fab);
        scanText = view.findViewById(R.id.scan_text);
        quoteText = view.findViewById(R.id.quote_text);

        scanFab.setVisibility(View.GONE);
        quoteFab.setVisibility(View.GONE);
        scanText.setVisibility(View.GONE);
        quoteText.setVisibility(View.GONE);

        isAllFABVisible = false;

        addFab.shrink();

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFABVisible){
                    scanFab.show();
                    quoteFab.show();
                    scanText.setVisibility(View.VISIBLE);
                    quoteText.setVisibility(View.VISIBLE);

                    addFab.extend();

                    isAllFABVisible = true;
                }
                else {
                    scanFab.hide();
                    quoteFab.hide();
                    scanText.setVisibility(View.GONE);
                    quoteText.setVisibility(View.GONE);

                    addFab.shrink();

                    isAllFABVisible = false;
                }
            }
        });

        scanFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Scanner Opened", Toast.LENGTH_SHORT).show();
            }
        });

        quoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Add Quote", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}