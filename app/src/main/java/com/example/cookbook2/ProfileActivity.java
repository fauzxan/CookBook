package com.example.cookbook2;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {



    private TextView username;
    private Button changePassword;
    private EditText oldpassword;
    private  EditText newpassword;
    //private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username=findViewById(R.id.username);
        changePassword=findViewById(R.id.changePassword);
        oldpassword=findViewById(R.id.oldpassword);
        newpassword=findViewById(R.id.newpassword);
        //back=findViewById(R.id.back);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        String usernameToAssign= String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getEmail());//this just retrieves the email of the user

        System.out.println(usernameToAssign);//log statement for testing

        username.setText(usernameToAssign);

            //AuthCredential credential = ;

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
                                                Toast.makeText(ProfileActivity.this, "Password Updated!",Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ProfileActivity.this, "Password not updated!",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Authentication failed!! Enter the correct old password",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}
