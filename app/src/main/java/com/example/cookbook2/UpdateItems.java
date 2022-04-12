package com.example.cookbook2;

import static java.lang.Integer.parseInt;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UpdateItems {

    String username = FirebaseAuth.getInstance().getUid();

    DatabaseReference mainmain = FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    DatabaseReference mainroot = FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
    DatabaseReference root = mainroot.child("User Inventory");
    DatabaseReference expire = root.child("Expiry Date");
    DatabaseReference locate = root.child("Location");

    public void remove(String toBeRemoved){
        locate.child(toBeRemoved).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String toCompare=""+snapshot.child("qty").getValue();

                String date_str = snapshot.child("zED").getValue().toString();
                ArrayList<String> dates = new ArrayList<String>(Arrays.asList(date_str.split(",")));
                String removedate = dates.remove(0);

                if (toCompare.equals("1")) {
                    locate.child(toBeRemoved).removeValue();
                }
                else{
                    String qty_str=""+snapshot.child("qty").getValue();
                    int qty=Integer.parseInt(qty_str);//to retrieve qty from database using singlevalueeventlistener
                    locate.child(toBeRemoved).child("qty").setValue(--qty);//decrementing the qty retrieved

                    String newdates = TextUtils.join(",",dates);
                    String newnew = newdates+",";
                    locate.child(toBeRemoved).child("zED").setValue(newnew);
                }
                com.example.cookbook2.UpdateExpiry remover = new com.example.cookbook2.UpdateExpiry();
                remover.deleteItem(toBeRemoved,removedate);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void add(String toBeAdded, String date){

        locate.child(toBeAdded).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild("qty")){// this if else statement increases the qty of existing element if it already exists in the list.
                    String temp= ""+snapshot.child("qty").getValue();
                    int qty=parseInt(temp);
                    locate.child(toBeAdded).child("qty").setValue(String.valueOf(++qty));

                    String old=snapshot.child("zED").getValue().toString().concat(""+date+",");
                    List<String> toSort = Arrays.asList(old.split(","));
                    Collections.sort(toSort);
                    String newDates = TextUtils.join(",",toSort)+",";
                    locate.child(toBeAdded).child("zED").setValue(newDates);
                }
                else {
                    locate.child(toBeAdded).child("qty").setValue("1");
                    locate.child(toBeAdded).child("zED").setValue(""+date+",");
                }
                com.example.cookbook2.UpdateExpiry adder = new com.example.cookbook2.UpdateExpiry();
                adder.addItem(toBeAdded,date);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}