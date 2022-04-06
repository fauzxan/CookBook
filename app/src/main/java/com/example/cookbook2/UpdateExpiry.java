package com.example.cookbook2;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateExpiry {

    String username= FirebaseAuth.getInstance().getUid();

    DatabaseReference mainmain = FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    DatabaseReference mainroot=FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
    DatabaseReference root = mainroot.child("User Inventory");
    DatabaseReference expire = root.child("Expiry Date");
    DatabaseReference locate = root.child("Location");

    public void addItem(String tobeadded, String date){
        expire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(date).hasChild(tobeadded)){
                    String qty = (String) snapshot.child(date).child(tobeadded).getValue();
                    int quantity = parseInt(qty);
                    expire.child(date).child(tobeadded).setValue(String.valueOf(++quantity));
                }
                else{
                    expire.child(date).child(tobeadded).setValue("1");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void deleteItem(String tobedelete, String date){
        expire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String qty = (String) snapshot.child(date).child(tobedelete).getValue();
                if (qty.equals("1")){
                    expire.child(date).child(tobedelete).removeValue();
                }
                else{
                    int quantity = parseInt(qty);
                    expire.child(date).child(tobedelete).setValue(String.valueOf(quantity-1));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
