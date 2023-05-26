package com.example.koutapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class RecognizedTextActivity extends AppCompatActivity {

    EditText recText;
    Button scanBtn, recognizeBtn;
    private static final String TAG = "MAIN_TAG";
    private Uri imageUri = null;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private String[] cameraPermission;
    private String[] storagePermission;

    private ProgressDialog progressDialog;

//    EditText recText;
//    Button click;
//    Uri imageUri;
//
    private TextRecognizer textRecognizer;
//
//    ActivityResultLauncher<Intent> launcher;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognized_text);

        recText = findViewById(R.id.recText);
        scanBtn = findViewById(R.id.btn_scan);
        recognizeBtn = findViewById(R.id.btn_recognize);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE };
        storagePermission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE };

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputImage();
            }
        });

        recognizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri == null){
                    Toast.makeText(RecognizedTextActivity.this, "Please pick the image first", Toast.LENGTH_SHORT).show();
                }
                else {
                    recognizeText();
                }
            }
        });

//        recText = findViewById(R.id.recText);
//        click = findViewById(R.id.btn_scan);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.clear:
                    Toast.makeText(this, "hai", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.scan:

                    Toast.makeText(this, "haiscan", Toast.LENGTH_SHORT).show();
                    // Handle dashboard item click
                    return true;
                case R.id.copy:
                    // Handle notifications item click
                    return true;
                default:
                    return false;
            }
        });

//        scanBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ImagePicker.with(RecognizedTextActivity.this)
//                        .crop()	    			//Crop image(Optional), Check Customization for more option
//                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                        .start();
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                launcher.launch(intent);
//            }
//        });
//        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK){
//                            Intent data = result.getData();
//                            assert data != null;
//                            imageUri = data.getData();
//                            Toast.makeText(RecognizedTextActivity.this, "image selected", Toast.LENGTH_SHORT).show();
//                            recogniseText();
//                            Log.d("Launcher45", "onActivityResult: working");
//                        }
//                        else {
//                            Toast.makeText(RecognizedTextActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });


    }

    private void recognizeText() {
        Log.d(TAG, "recognizeText: ");
       progressDialog.setMessage("Preparing text..");
       progressDialog.show();

        try {
            InputImage inputImage = InputImage.fromFilePath(this, imageUri);
            progressDialog.setMessage("Recognising text..");
            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            progressDialog.dismiss();
                            String recognizedText = text.getText();
                            Log.d(TAG, "onSuccess: recognized text"+recognizedText);
                            recText.setText(recognizedText);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Log.e(TAG, "onFailure: ",e );
                            Toast.makeText(RecognizedTextActivity.this, "Failed recognition due to"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {

            progressDialog.dismiss();
            Log.e(TAG, "recognizeText: ",e );
            Toast.makeText(this, "Failed preparing image due to"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showInputImage() {
        PopupMenu popupMenu = new PopupMenu(this, scanBtn);
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "CAMERA");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "GALLERY");

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == 1){
                    Log.d(TAG, "onMenuItemClick: camera clicked");
                    if (checkCameraPermission()){
                        pickImageCamera();
                    }
                    else {
                        requestCameraPermission();
                    }

                } else if (id == 2) {
                    Log.d(TAG, "onMenuItemClick: gallery clicked");
                    if (checkStoragePermission()){
                        pickImageGallery();
                    }
                    else {
                        requestStoragePermission();
                    }
                }
                return true;
            }
        });
    }

    private void pickImageGallery(){
        Log.d(TAG, "pickImageGallery: ");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG, "onActivityResult: imageuri"+imageUri);
                    }
                    else {
                        Log.d(TAG, "onActivityResult: cancelled");
                        Toast.makeText(RecognizedTextActivity.this, "cancelled..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

   private void pickImageCamera(){
       Log.d(TAG, "pickImageCamera: ");
       ContentValues values = new ContentValues();
       values.put(MediaStore.Images.Media.TITLE, "Sample Title");
       values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description");
       imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
       cameraLauncher.launch(intent);

   }

   private ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
           new ActivityResultContracts.StartActivityForResult(),
           new ActivityResultCallback<ActivityResult>() {
               @Override
               public void onActivityResult(ActivityResult result) {
                   if (result.getResultCode() == Activity.RESULT_OK){
                       Log.d(TAG, "onActivityResult: imageuri"+imageUri);
                       Toast.makeText(RecognizedTextActivity.this, "image selected from camera", Toast.LENGTH_SHORT).show();
                   }
                   else {
                       Log.d(TAG, "onActivityResult: cancelled");
                       Toast.makeText(RecognizedTextActivity.this, "Cancelled camera image", Toast.LENGTH_SHORT).show();
                   }
               }
           }

   );

   private boolean checkStoragePermission(){
       boolean result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
       return result;
   }

   private void requestStoragePermission(){
       ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
   }

    private boolean checkCameraPermission(){
        boolean cameraResult = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storageResult = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return cameraResult && storageResult;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted){
                        pickImageCamera();
                    }
                    else {
                        Toast.makeText(this, "Camera and storage permissions are required", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "CANCELLED..", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{

                if (grantResults.length>0){


                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted){
                        pickImageGallery();
                    }
                    else {
                        Toast.makeText(this, "storage permissions are required", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "CANCELLED..", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }


    //    private void recogniseText() {
//        if (imageUri != null){
//            try {
//                Log.d("recT", "recogniseText: ");
//                InputImage inputImage = InputImage.fromFilePath(RecognizedTextActivity.this,imageUri);
//                Task<Text> result = textRecognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
//                    @Override
//                    public void onSuccess(Text text) {
//
//                        String recognisedText = text.getText();
//                        recText.setText(recognisedText);
////                        Toast.makeText(RecognizedTextActivity.this, "hmmmmm", Toast.LENGTH_SHORT).show();
//                        Log.d("RecognizedTextActivity", "Recognized Text: " + recognisedText);
//
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(RecognizedTextActivity.this, "e.getMessage()", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}