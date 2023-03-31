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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText inputfname,inputemail,inputpass,inputconfirmPass;
    Button goToLogin, register;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    MainActivity.User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        goToLogin = findViewById(R.id.LoginBtn);
        register = findViewById(R.id.registerBtn);
        inputfname = findViewById(R.id.inputName);
        inputemail = findViewById(R.id.inputEmail);
        inputpass = findViewById(R.id.inputPass);
        inputconfirmPass = findViewById(R.id.inputConfirmPass);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserINFO");
        progressDialog = new ProgressDialog(this);


        goToLogin.setOnClickListener(view -> {
        Intent iii = new Intent(Register.this,Login.class);
        startActivity(iii);
    });

    register.setOnClickListener(view -> PerformAuth());
    }

    private void PerformAuth() {
        String fname = inputfname.getText().toString();
        String enterEmail = inputemail.getText().toString();
        String password = inputpass.getText().toString();
        String confirmPassword = inputconfirmPass.getText().toString();

        if(fname.isEmpty() ||  enterEmail.isEmpty()||confirmPassword.isEmpty()||password.isEmpty())
        {
            inputfname.setError("Cannot be empty");
        } else if (!password.equals(confirmPassword)) {
            inputconfirmPass.setError("Passwords do not match");
        }else{
            addUserDataToDatabase(enterEmail,fname,password);


            progressDialog.setMessage("Processing");
            progressDialog.setTitle("REgistering");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(enterEmail,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent iii = new Intent(Register.this,Login.class);
                    iii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(iii);
                    Toast.makeText(Register.this, "SUCCESSFULL", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void addUserDataToDatabase(String enterEmail, String fname, String password){
        user = new MainActivity.User(enterEmail, fname, password);
        databaseReference.child(enterEmail.replace(".",",")).setValue(user).addOnFailureListener(task -> {
            Toast.makeText(this, "DATABASE ERROR", Toast.LENGTH_SHORT).show();
            System.exit(0);
        });
    }
}