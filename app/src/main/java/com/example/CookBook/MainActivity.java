
package com.example.CookBook;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//looking for this change
public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button logout;
    private EditText item;
    private EditText location;
    private Button add;
    private Button remove;
    private RecyclerView listview;
    private Button profile;
    private Button cart;
    private Button barcode;
    private MyRecyclerViewAdapter adapter;

    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_items);

        logout = findViewById(R.id.logout);
        item = findViewById(R.id.item);
        //location = findViewById(R.id.location);
        add = findViewById(R.id.add);
        remove=findViewById(R.id.remove);
        listview = findViewById(R.id.list);
        profile=findViewById(R.id.profile);
        cart=findViewById(R.id.cart);
        barcode=findViewById(R.id.barcode);

        ArrayList<String> list = new ArrayList<>();

        //setup the recycler view
        LinearLayoutManager layout = new LinearLayoutManager(this);
        listview.setLayoutManager(layout);
        adapter = new MyRecyclerViewAdapter(this, list);
        listview.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(cart.getContext(),layout.getOrientation());
        listview.addItemDecoration(divider);

        String username=FirebaseAuth.getInstance().getUid();

        DatabaseReference mainroot=FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
        DatabaseReference root = mainroot.child("User Inventory");
        DatabaseReference locate = root.child("Location");//this is location of food stored

        //code for logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this,"Logged Out!",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, StartActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);// this takes you back to the StartActivity class
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, com.example.CookBook.ProfileActivity.class));
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ShoppingList.class));
            }
        });

        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BarcodeActivity.class));
            }
        });

        //code for adding item
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Please enter expiry date", Toast.LENGTH_LONG).show();
                //showDatePickerDialog();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        (DatePickerDialog.OnDateSetListener) MainActivity.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String mth = ""+month;
                        if (mth.length() == 1){
                            mth = "0"+mth;
                        }
                        date = year+"-"+mth+"-"+dayOfMonth;
                        String txt_item = item.getText().toString();//gets value of item from the text field
                        if (!txt_item.isEmpty() && !txt_item.equals("\n"))
                        {
                            locate.child(txt_item).child(txt_item).setValue(txt_item);//pushes the value into the database.\
                            UpdateItems adder = new UpdateItems();
                            adder.add(txt_item,date);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Field is empty",Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });

        //remove item code
        remove.setOnClickListener(new View.OnClickListener() {// if the remove button is clicked, we start looking for the items that have been clicked
            @Override
            public void onClick(View view)
            {
                Toast.makeText(MainActivity.this,"Select items to remove them",Toast.LENGTH_SHORT).show();
                adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        // i is the index of the list item that has been clicked. Which also happens to be the same as the index of the element in the list
                        String remove_item=list.get(i);
                        String[] remove_item_split=remove_item.split("\n");
                        String toBeRemoved=remove_item_split[0];

                        UpdateItems remover = new UpdateItems();
                        remover.remove(toBeRemoved);
                    }
                });
            }
        });

        //check for expiry dates on the current day
        root.child("Expiry Date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = 1+Calendar.getInstance().get(Calendar.MONTH);
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                String mth = ""+month;
                if (mth.length() == 1){
                    mth = "0"+mth;
                }
                String today = year+"-"+mth+"-"+day;

                if (snapshot.hasChild(today)){
                    for (DataSnapshot ss: snapshot.child(today).getChildren()){
                        int qty = parseInt(ss.getValue().toString());
                        UpdateItems removeExpired = new UpdateItems();
                        for (int i=0; i<qty; i++){
                            removeExpired.remove(ss.getKey());
                        }
                        //root.child("Cart").child(ss.getKey()).setValue(ss.getKey());//item is added to cart/shopping list when it is expired
                        Cart expired = new Cart();
                        expired.setItem_name(ss.getKey());
                        expired.setQuantity(ss.getValue().toString());
                        new FirebaseHelper().updateCart(expired, new FirebaseHelper.DataStatus() {
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //whenever you add or remove value from the database, this method listens to it
        locate.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot sss: snapshot.getChildren()){
                    String temp=sss.getKey();
                    list.add(temp);}

                for (DataSnapshot ss: snapshot.getChildren()){
                    String temp=ss.getKey();
                    for (DataSnapshot ss2: snapshot.child(temp).getChildren()){
                        if (ss2.getKey().equals("qty")){
                            int foo=list.indexOf(temp);
                            String foo2=list.get(foo);

                            String zED = ""+ss.child("zED").getValue();
                            ArrayList<String> dates = new ArrayList<String>(Arrays.asList(zED.split(",")));//list of expiry dates
                            String newDates = dates.remove(0);

                            list.set(foo,foo2+"\n"+"Quantity: "+ss2.getValue().toString()+"\n"+"Nearest expiry date: "+newDates);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        myAlarm();

    }

    //for the datepickerdialog
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        month = month+1;
        String mth = ""+month;
        if (mth.length() == 1){
            mth = "0"+mth;
        }
        date = year+"-"+mth+"-"+dayOfMonth;
    }

    public void myAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }


}
