package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.goToRegister);


        btn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
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


    public static class User {
        public String emailAddress;
        public String fullName;
        public String password;
        public boolean isFirst;

        public User(){}
        public User (String emailAddress, String fullName, String password){
            this.emailAddress = emailAddress;
            this.fullName = fullName;
            this.password = password;
            isFirst = true;
        }
    }
}