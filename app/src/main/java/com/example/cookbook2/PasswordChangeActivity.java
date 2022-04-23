package com.example.cookbook2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordChangeActivity extends AppCompatActivity {

    private TextInputEditText oldpassword;
    private TextInputEditText newpassword;
    private Button changePassword;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        String usernameToAssign= String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        oldpassword= (TextInputEditText) findViewById(R.id.oldpassword);
        newpassword= (TextInputEditText) findViewById(R.id.newpassword);
        changePassword= (Button) findViewById(R.id.changePassword);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code to change the password

                System.out.println(oldpassword.toString());

                user.reauthenticate(EmailAuthProvider
                        .getCredential(usernameToAssign, oldpassword.getText().toString()))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    user.updatePassword(newpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(view.getContext(), "Password Updated!",Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(view.getContext(), "Password not updated!",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(view.getContext(), "Authentication failed!! Enter the correct old password",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
