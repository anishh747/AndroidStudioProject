package com.example.login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AskDetails extends AppCompatActivity {
    ImageView profilePic;
    Button nextBtn, chooseBtn;
    ActivityResultLauncher pickImageLauncher;
    InputStream inputStream;
    Bitmap bitmap;
    ByteArrayOutputStream baos;
    byte[] imgData;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_details);
        profilePic = findViewById(R.id.profilePic);
        chooseBtn = findViewById(R.id.Choose);
        nextBtn = findViewById(R.id.Next);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserINFO");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");

        Bundle extras = getIntent().getExtras();
        String userEmail = extras.getString("USER_EMAIL");

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            try {
                inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        chooseBtn.setOnClickListener(view -> {
            pickImageLauncher.launch("image/*");
        });

        nextBtn.setOnClickListener(view -> {
            uploadImageToFirebase(userEmail);
            updateIsFirst(userEmail);
            Intent intent = new Intent(AskDetails.this,Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }

    private void uploadImageToFirebase(String userEmail) {
        baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imgData = baos.toByteArray();
        UploadTask uploadTask = storageReference.child(userEmail.replace(".",",")+".jpg").putBytes(imgData);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(this, "IMAGE UPLOADED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "IMAGE UPLOAD FAILED"+e, Toast.LENGTH_SHORT).show();
        });
    }

    private void updateIsFirst(String userEmail) {
        databaseReference.child(userEmail.replace(".",",")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("isFirst").setValue(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AskDetails.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}