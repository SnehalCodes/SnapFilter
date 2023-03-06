package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImgchooseActivity extends AppCompatActivity implements View.OnClickListener {
    String filterType;
    TextView txtFilterName;
    ImageView imgDisplay;
    AppCompatButton btnChooseImg;
    ImageButton btnNext;
    Bitmap selectedImageBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgchoose);
        initView();
    }

    private void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("FilterApp", Context.MODE_PRIVATE);
        filterType = sharedPreferences.getString("filterType", "");
        txtFilterName = findViewById(R.id.txtFilterName);
        imgDisplay = findViewById(R.id.imgDisplay);
        btnChooseImg = findViewById(R.id.btnChooseImg);
        btnNext = findViewById(R.id.btnNext);
        setFilterName(filterType);
        btnChooseImg.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }

    private void setFilterName(String filterType) {
        switch (filterType)
        {
            case "BlackWhite":
                String text = "Black and white";
                txtFilterName.setText(text);
                break;
            case "Sketch":
                text = "Sketch";
                txtFilterName.setText(text);
                break;
            case "Cartoon":
                text = "Cartoon";
                txtFilterName.setText(text);
                break;
            case "Rainbow":
                text = "Rainbow Color";
                txtFilterName.setText(text);
                break;
            case "Reflection":
                text = "Reflection";
                txtFilterName.setText(text);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnChooseImg:
                CheckPermission();
                break;
            case R.id.btnNext:
                if(selectedImageBitmap == null)
                {
                    Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
                }else
                {
                    NextActivity();
                }
                break;
        }

    }

    private void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED||
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
        } else {
            selectImage(ImgchooseActivity.this);
        }
    }


    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 0);

            } else if (options[item].equals("Choose from Gallery")) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage(ImgchooseActivity.this);
            }
            else
            {
                CheckPermission();
            }


        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        selectedImageBitmap = (Bitmap) data.getExtras().get("data");
                        try {
                            imgDisplay.setImageBitmap(selectedImageBitmap);
                        } catch (Exception e) {
                            Log.e("imageException", e.toString());
                        }

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        selectedImageBitmap = null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            imgDisplay.setImageBitmap(selectedImageBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//                                try {
//                                    if (bitmap != null) {
//                                        CustomerProfileImg = BitMapToString(bitmap);
//                                    }
//                                } catch (Exception e) {
//                                    Log.e("imageException", e.toString());
//                                }
//
//                                cursor.close();
//                            }
//                        }
                    }
                    break;

            }
        }
    }
    public void NextActivity()
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent in1 = new Intent(this, ApplyFilterActivity.class);
        in1.putExtra("image",byteArray);
        startActivity(in1);
    }
}
