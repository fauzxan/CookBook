package com.example.cookbook2;

import static java.lang.Integer.parseInt;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*
The class FirebaseHelper
 */

public class FirebaseHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceProducts;
    private DatabaseReference mReferenceCart;
    //private List<Product> products = new ArrayList<>();
    private List<Cart> carts = new ArrayList<>();

    public FirebaseHelper(){
        try {
            String username = FirebaseAuth.getInstance().getUid();
            mDatabase = FirebaseDatabase.getInstance();
//            mReferenceProducts = mDatabase.getReference(username).child("User Inventory").child("Location");
            mReferenceProducts = mDatabase.getReference("Product");
            mReferenceCart = mDatabase.getReference(username).child("User Inventory").child("Cart");
        }
        catch (Exception e){
            Log.i("ERROR" ,  e.toString());
        }
    }

    public interface DataStatus{
        void DataIsLoadedCart(List<Cart> carts, List<String> keys);
        void DataInserted();
    }
    public void readCart(final DataStatus dataStatus){

        mReferenceCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carts.clear();
                List<String> keys = new ArrayList<>();

                for(DataSnapshot keyNode: snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Cart cart = keyNode.getValue(Cart.class);
                    carts.add(cart);
                }
                dataStatus.DataIsLoadedCart(carts,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("PROBLEM FIREBASE", "not nice");
            }
        });
    }

    public void updateCart(Cart cart, final DataStatus dataStatus){
        String key = cart.getItem_name();
        String qtystr = cart.getQuantity();
        int qty = parseInt(qtystr);
        mReferenceCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(key)){
                    String qtyy = (String) snapshot.child(key).child("quantity").getValue();
                    int quantity = parseInt(qtyy);
                    cart.setQuantity(String.valueOf(quantity+qty));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mReferenceCart.child(key).setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dataStatus.DataInserted();
            }
        });
    }
}
