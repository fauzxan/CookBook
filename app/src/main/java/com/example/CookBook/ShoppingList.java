package com.example.CookBook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ShoppingList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    Button add_cart;
    Button clear_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        String username= FirebaseAuth.getInstance().getUid();
        DatabaseReference mainroot = FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
        DatabaseReference root = mainroot.child("User Inventory");

        Intent intent = new Intent(ShoppingList.this, AddCart.class);
        add_cart = (Button) findViewById(R.id.add_cart);
        add_cart.setOnClickListener(view -> startActivity(intent));

        clear_cart = findViewById(R.id.clear_cart);
        clear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root.child("Cart").removeValue();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.cart_recycler);
        FirebaseHelper fb = new FirebaseHelper();
        fb.readCart(new FirebaseHelper.DataStatus() {

            @Override
            public void DataInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }

            @Override
            public void DataIsLoadedCart(List<Cart> carts, List<String> keys){
                new CartRecyclerView_Config().setConfig(mRecyclerView, ShoppingList.this, carts, keys);
            }
        });
    }
}