package com.example.cookbook2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private TextView mainbody;
    private Button back;
    private RecyclerView cart;
    private MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mainbody=findViewById(R.id.mainbody);
        back=findViewById(R.id.back);
        cart=findViewById(R.id.cart);
        ArrayList<String> list = new ArrayList<>();

        //setup the recycler view
        LinearLayoutManager layout = new LinearLayoutManager(this);
        cart.setLayoutManager(layout);
        adapter = new MyRecyclerViewAdapter(this, list);
        cart.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(cart.getContext(),layout.getOrientation());
        cart.addItemDecoration(divider);

        String username= FirebaseAuth.getInstance().getUid();

        DatabaseReference mainroot= FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
        DatabaseReference root = mainroot.child("User Inventory");
        DatabaseReference locate = root.child("Location");//this is location of food stored
        DatabaseReference cartloc = root.child("Cart");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, com.example.cookbook2.MainActivity.class));
            }
        });

        cartloc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot ss: snapshot.getChildren()){
                    list.add(ss.getKey());
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
}