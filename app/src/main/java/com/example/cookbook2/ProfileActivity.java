package com.example.cookbook2;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends Fragment {

    private TextView username;
    private ImageView logout;
    private Button updatePass;
    private Button update;
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_profile, container, false);
        username= (TextView) view.findViewById(R.id.username);
//        logout= (ImageView) view.findViewById(R.id.logout);
        updatePass = (Button) view.findViewById(R.id.changePassword1);

        setHasOptionsMenu(true);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        String usernameToAssign= String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getEmail());//this just retrieves the email of the user

        System.out.println(usernameToAssign);//log statement for testing

        username.setText(usernameToAssign);

            //AuthCredential credential = ;

        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), PasswordChangeActivity.class));
            }
        });

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(view.getContext(),"Logged Out!",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(view.getContext(), StartActivity.class));// this takes you back to the StartActivity class
//            }
//        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item1){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(view.getContext(),"Logged Out!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(view.getContext(), StartActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
