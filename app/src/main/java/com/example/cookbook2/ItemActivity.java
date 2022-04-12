package com.example.cookbook2;

import static java.lang.Integer.parseInt;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ItemActivity extends Fragment implements DatePickerDialog.OnDateSetListener {

    private EditText item;
    private ImageButton add;
    private Switch remove;
    private RecyclerView listview;
    private MyRecyclerViewAdapter adapter;
    private String date;
    private View view;

    ArrayList<String> list = new ArrayList<>();

    //list = MainActivity.list;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_items);
        view = inflater.inflate(R.layout.activity_edit_items, container, false);

        item = (EditText) view.findViewById(R.id.item);
        add = (ImageButton) view.findViewById(R.id.add);
        remove = (Switch) view.findViewById(R.id.remove);
        listview = (RecyclerView) view.findViewById(R.id.list);

        //setup the recycler view
        LinearLayoutManager layout = new LinearLayoutManager(view.getContext());
        listview.setLayoutManager(layout);
        adapter = new MyRecyclerViewAdapter(view.getContext(), list);
        listview.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(listview.getContext(),layout.getOrientation());
        listview.addItemDecoration(divider);

        String username= FirebaseAuth.getInstance().getUid();

        DatabaseReference mainroot= FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
        DatabaseReference root = mainroot.child("User Inventory");
        DatabaseReference locate = root.child("Location");//this is location of food stored

        //code for adding item
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "Please enter expiry date", Toast.LENGTH_LONG).show();
                //showDatePickerDialog();
                String monthtoday = ""+1+Calendar.getInstance().get(Calendar.MONTH);
                if (monthtoday.length() == 1){ monthtoday = "0"+monthtoday; }
                String daytoday = ""+Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                if (daytoday.length() == 1){ daytoday = "0"+daytoday; }
                String today = Calendar.getInstance().get(Calendar.YEAR)+"-"+monthtoday+"-"+daytoday;
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        view.getContext(),
                        (DatePickerDialog.OnDateSetListener) ItemActivity.this,
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
                        String dayt = ""+dayOfMonth;
                        if (dayt.length() == 1){
                            dayt = "0"+dayt;
                        }
                        date = year+"-"+mth+"-"+dayt;
                        String txt_item = item.getText().toString();//gets value of item from the text field
                        if (!txt_item.isEmpty() && !txt_item.equals("\n") && !(date.compareTo(today)<1))
                        {
                            locate.child(txt_item).child(txt_item).setValue(txt_item);//pushes the value into the database.\
                            UpdateItems adder = new UpdateItems();
                            adder.add(txt_item,date);
                        }
                        else
                        {
                            Toast.makeText(getContext(),"Item cannot be added",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //remove item code
        remove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Toast.makeText(view.getContext(),"Tap on items to remove",Toast.LENGTH_SHORT).show();
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
                String dayt = ""+day;
                if (dayt.length() == 1){
                    dayt = "0"+dayt;
                }
                String today = year+"-"+mth+"-"+dayt;

                for (DataSnapshot sss: snapshot.getChildren()){
                    String datetemp = ""+sss.getKey();
                    if (datetemp.compareTo(today)<1){
                        for (DataSnapshot ss: snapshot.child(datetemp).getChildren()){
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

                /*if (snapshot.hasChild(today)){
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
                }*/
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
                root.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot ss: snapshot.child("Expiry Date").getChildren()){
                            for (DataSnapshot sss: ss.getChildren()){
                                String itemname = sss.getKey();
                                if (!list.contains(itemname)){
                                    list.add(itemname);
                                }
                            }
                        }
                        for (DataSnapshot ss: snapshot.child("Location").getChildren()){
                            String temp=ss.getKey();
                            for (DataSnapshot ss2: snapshot.child("Location").child(temp).getChildren()){
                                String childname = ""+ss2.getKey();
                                if (childname.equals("qty")){
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


//        myAlarm();

        return view;
    }

    //for the datepickerdialog
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
    }


}
