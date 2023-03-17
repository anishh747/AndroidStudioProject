package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText email, pass;
    Button goToRegister, loginBtn;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    goToRegister = findViewById(R.id.rBtn);
    email = findViewById(R.id.inputEmailLogin);
    pass = findViewById(R.id.inputPassLogin);
    loginBtn = findViewById(R.id.lBtn);
    firebaseAuth = FirebaseAuth.getInstance();
    firebaseUser = firebaseAuth.getCurrentUser();
    progressDialog = new ProgressDialog(this);

    goToRegister.setOnClickListener(view->{
        Intent intent = new Intent(Login.this,Register.class);
        startActivity(intent);
    });

    loginBtn.setOnClickListener(view -> LogIn());

    }
    private void LogIn() {
        String enterEmail = email.getText().toString();
        String password = pass.getText().toString();

        if(enterEmail.isEmpty())
        {
            email.setError("Cannot be empty");
        } else if (password.isEmpty()) {
            pass.setError("Cannot be empty");
        }else{
            progressDialog.setMessage("Processing");
            progressDialog.setTitle("Logging In");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(enterEmail,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent iii = new Intent(Login.this,Dashboard.class);
                    iii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(iii);
                    Toast.makeText(Login.this, "SUCCESSFULL", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}