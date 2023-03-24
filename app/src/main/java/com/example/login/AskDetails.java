package com.example.login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class AskDetails extends AppCompatActivity {
    ImageView profilePic;
    Button nextBtn, chooseBtn;
    ActivityResultLauncher pickImageLauncher;
    InputStream inputStream;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_details);
        profilePic = findViewById(R.id.profilePic);
        chooseBtn = findViewById(R.id.Choose);
        nextBtn = findViewById(R.id.Next);


        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            try {
                inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        chooseBtn.setOnClickListener(view->{
            pickImageLauncher.launch("image/*");

        });

    }

}