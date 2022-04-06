package com.example.cookbook2;

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

public class FirebaseHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceProducts;
    private DatabaseReference mReferenceCart;
    private List<Product> products = new ArrayList<>();
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
        void DataIsLoaded(List<Product> products, List<String> keys);
        void DataIsLoadedCart(List<Cart> carts, List<String> keys);
        void DataInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public void readProducts(final DataStatus dataStatus){
//        if(mode == "product") {
            mReferenceProducts.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    products.clear();
                    List<String> keys = new ArrayList<>();

                    for (DataSnapshot keyNode : snapshot.getChildren()) {
                        keys.add(keyNode.getKey());
                        Product product = keyNode.getValue(Product.class);
                        products.add(product);
                    }
                    dataStatus.DataIsLoaded(products, keys);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("PROBLEM FIREBASE", "not nice");
                }
            });
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




    public void addCart(Cart cart , final DataStatus dataStatus){
        String key = mReferenceCart.push().getKey();
        mReferenceCart.child(key).setValue(cart)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dataStatus.DataInserted();
                    }
                });
    }
}
