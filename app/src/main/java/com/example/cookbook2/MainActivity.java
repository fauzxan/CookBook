package com.example.cookbook2;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button logout;
    private EditText item;
    private EditText location;
    private Button add;
    private Button remove;
    private ListView listview;
    private Button profile;
    private Button help;


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
        help=findViewById(R.id.help);

        String username=FirebaseAuth.getInstance().getUid();

        DatabaseReference mainroot=FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
        DatabaseReference root = mainroot.child("User Inventory");
        DatabaseReference locate = root.child("Fridge");//this is fridge



        //code for logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this,"Logged Out!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, com.example.cookbook2.StartActivity.class));// this takes you back to the StartActivity class
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, com.example.cookbook2.ProfileActivity.class));
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, com.example.cookbook2.HelpActivity.class));
            }
        });




        //code for adding item
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_item = item.getText().toString();//gets value of item from the text field
                //String txt_location = location.getText().toString();
                //if (txt_item.isEmpty()||txt_location.isEmpty()){
                //    Toast.makeText(MainActivity.this, "Field empty!",Toast.LENGTH_SHORT).show();
                //} else{
                if (!txt_item.isEmpty() && !txt_item.equals("\n"))
                {
                    locate.child(txt_item).child(txt_item).setValue(txt_item);//pushes the value into the database.\
                    locate.child(txt_item).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {// this is to return a snapshot of all the elements in the database at "locate"
                            if (snapshot.hasChild("qty")){// this if else statement increases the qty of existing element if it already exists in the list.
                                String temp= (String) snapshot.child("qty").getValue();
                                int qty=parseInt(temp);
                                locate.child(txt_item).child("qty").setValue(String.valueOf(++qty));
                            }
                            else {
                                locate.child(txt_item).child("qty").setValue("1");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else
                {
                    Toast.makeText(MainActivity.this,"Field is empty",Toast.LENGTH_SHORT);
                }
                //}
            }
        });



        ArrayList<String> list = new ArrayList<>();
        listview.setAdapter(makeAdapter(list));//makeAdapter has been made into a method



        //remove item code
        remove.setOnClickListener(new View.OnClickListener() {// if the remove button is clicked, we start looking for the items that have been clicked
            @Override
            public void onClick(View view)
            {
                Toast.makeText(MainActivity.this,"Select items to remove them",Toast.LENGTH_SHORT).show();

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {//looks for the item that has been clicked

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                    {
                        // i is the index of the list item that has been clicked. Which also happens to be the same as the index of the element in the list
                        String remove_item=list.get(i);
                        String[] remove_item_split=remove_item.split("\n");
                        String toBeRemoved=remove_item_split[0];
                        System.out.println(toBeRemoved);
                        locate.child(toBeRemoved).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String toCompare=""+snapshot.child("qty").getValue();
                                if (toCompare.equals("1")) {
                                    locate.child(toBeRemoved).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        //this method helps see if the item has been successfully removed from the database or an error occurred in the process.
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                String toast_msg = toBeRemoved + " has been removed successfully!";//this is the float message to be displayed once the item has been removed successfully
                                                Toast.makeText(MainActivity.this, toast_msg, Toast.LENGTH_SHORT);
                                                list.remove(toBeRemoved);
                                                listview.setAdapter(makeAdapter(list));//to refresh the list
                                            } else {//will pop this message if the item hasn't been removed
                                                Toast.makeText(MainActivity.this, "Item was not removed. There was a problem on the backend side", Toast.LENGTH_SHORT);
                                            }
                                        }
                                    });
                                }
                                else{
                                    System.out.println(snapshot.child("qty").getValue());
                                    String qty_str=""+snapshot.child("qty").getValue();
                                    int qty=Integer.parseInt(qty_str);//to retrieve qty from database using singlevalueeventlistener
                                    locate.child(toBeRemoved).child("qty").setValue(--qty);//decrementing the qty retrieved
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                System.out.println("here");
                            }
                        });




                    }
                });
            }
        });






        //whenever you add or remove value from the database, this method listens to it
        locate.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot ss: snapshot.getChildren()){
                    String temp=ss.getKey();
                    for (DataSnapshot ss2: snapshot.child(temp).getChildren()){
                        if (ss2.getKey().equals(temp)) list.add(ss2.getValue().toString());
                        else{
                            int foo=list.indexOf(temp);
                            String foo2=list.get(foo);
                            list.set(foo,foo2+"\n"+"Quantity:"+ss2.getValue().toString());
                        }
                    }
                }
                makeAdapter(list).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private ArrayAdapter<String> makeAdapter(ArrayList<String> list){//this method sets the adapter. Declared as a separate function because we this is used more than once
        return new ArrayAdapter<>(this,R.layout.list_item,list);//list_item is a .xml file (see res/layout)
    }
}