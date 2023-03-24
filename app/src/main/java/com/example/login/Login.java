package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    EditText email, pass;
    Button goToRegister, loginBtn;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserINFO");

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
                    if(checkIfFirstLogin(enterEmail)==true){
                        progressDialog.dismiss();
                        Intent iii = new Intent(Login.this,AskDetails.class);
                        iii.putExtra("USER_EMAIL",enterEmail);
                        //startActivity(iii);
                        //Toast.makeText(Login.this, "ASK DETAILS", Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.dismiss();
                        Intent iii = new Intent(Login.this,Dashboard.class);
                        iii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        //startActivity(iii);
                        //Toast.makeText(Login.this, "LOG IN PAGE", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean checkIfFirstLogin(String enterEmail) {
        final boolean[] isFirst = new boolean[1];
        databaseReference.child(enterEmail.replace(".",",")).get().addOnCompleteListener(task -> {
            DataSnapshot dataSnapshot = task.getResult();
            isFirst[0] = (boolean) dataSnapshot.child("isFirst").getValue();
        });
        Toast.makeText(this, "isFirst:"+isFirst[0], Toast.LENGTH_SHORT).show();
        return isFirst[0];
    }

}