package com.example.cookbook2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/*
This activity java file helps the user register their account onto the application. The user details are saved onto the Firebase upon successful
registration.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button register;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this,"Please enter email/password",Toast.LENGTH_SHORT).show();
                } else if (txt_password.length()<6){
                    Toast.makeText(RegisterActivity.this,"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_email,txt_password);
                }
            }
        }
        );

    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Registered!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, StartActivity.class));
                    finish();
                } else{
                    Toast.makeText(RegisterActivity.this,"Register failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}