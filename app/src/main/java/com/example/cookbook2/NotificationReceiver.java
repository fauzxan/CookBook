package com.example.cookbook2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {

    String username= FirebaseAuth.getInstance().getUid();
    DatabaseReference mainroot= FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
    DatabaseReference root = mainroot.child("User Inventory");


    @Override
    public void onReceive(Context context, Intent intent) {

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = 1+Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String mth = ""+month;
        if (mth.length() == 1){
            mth = "0"+mth;
        }
        String today = year+"-"+mth+"-"+day;

        root.child("Expiry Date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(today)){
                    NotificationHelper notificationHelper = new NotificationHelper(context);
                    notificationHelper.createNotification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}