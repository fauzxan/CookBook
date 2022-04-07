package com.example.cookbook2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class ShoppingList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    Button add_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Intent intent = new Intent(ShoppingList.this, AddCart.class);
        add_cart = (Button) findViewById(R.id.add_cart);
        add_cart.setOnClickListener(view -> startActivity(intent));

//        String username= FirebaseAuth.getInstance().getUid();

//        DatabaseReference mainroot = FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
//        DatabaseReference root = mainroot.child("User Inventory");
//        DatabaseReference locate = root.child("Fridge");//this is fridge

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