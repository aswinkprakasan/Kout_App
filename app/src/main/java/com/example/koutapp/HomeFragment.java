package com.example.koutapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.Objects;


public class HomeFragment extends Fragment {

    FloatingActionButton scanFab, quoteFab;
    ExtendedFloatingActionButton addFab;
    TextView scanText, quoteText;
    EditText recText;
    Boolean isAllFABVisible;
    Uri imageuri;
    TextRecognizer textRecognizer;


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

        recText = view.findViewById(R.id.recText);
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        scanFab.setVisibility(View.GONE);
        quoteFab.setVisibility(View.GONE);
        scanText.setVisibility(View.GONE);
        quoteText.setVisibility(View.GONE);

        isAllFABVisible = false;


//        ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//
//            @Override
//            public void onActivityResult(Uri result) {
//
//            }
//        });
//        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == Activity.RESULT_OK) {
//                Intent data = result.getData();
//
//                if (data != null) {
//                    imageuri = data.getData();
//
//                    Toast.makeText(requireContext(), "Image selected", Toast.LENGTH_SHORT).show();
//
//                    recogniseText();
//                }
//            } else {
//                Toast.makeText(requireContext(), "Image not selected", Toast.LENGTH_SHORT).show();
//            }
//        });
//       Intent intent = new Intent(getActivity(), RecognizedTextActivity.class);
//        intent.putExtra("Key",recognisedText);
//        launcher.launch(intent);


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
                ImagePicker.with(requireActivity())
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
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

//    private void recogniseText() {
//        if (imageuri != null){
//            try {
//                InputImage inputImage = InputImage.fromFilePath(requireActivity(),imageuri);
//                Task<Text> result = textRecognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
//                    @Override
//                    public void onSuccess(Text text) {
//
//                        String recognisedText = text.getText();
//                        recText.setText(recognisedText);
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}