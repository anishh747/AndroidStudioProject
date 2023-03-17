package com.example.login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher pickImageLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    Button btn = findViewById(R.id.goToRegister);
    Button btn2 =findViewById(R.id.loadBtn);
    ImageView img = findViewById(R.id.imageView2);

    btn.setOnClickListener(view -> {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    });

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        btn2.setOnClickListener(view->{
            pickImageLauncher.launch("image/*");
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this,Dashboard.class));
            finish();
        }
    }



}