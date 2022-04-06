package com.example.cookbook2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    Button cart_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = new Intent(TestActivity.this, ShoppingList.class);

        cart_button = (Button) findViewById(R.id.cart_button);
        cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        String username=FirebaseAuth.getInstance().getUid();

        DatabaseReference mainroot = FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
        DatabaseReference root = mainroot.child("User Inventory");
        DatabaseReference locate = root.child("Fridge");//this is fridge

        mRecyclerView = (RecyclerView) findViewById(R.id.product_recycler);
        FirebaseHelper fb = new FirebaseHelper();
        fb.readProducts(new FirebaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Product> products, List<String> keys) {
                Log.i("DATA LOADED", "YES");
                new RecyclerView_Config().setConfig(mRecyclerView, TestActivity.this, products, keys);
            }

            @Override
            public void DataIsLoadedCart(List<Cart> carts, List<String> keys) {

            }

            @Override
            public void DataInserted() {


            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }

        });

    }
}