package com.example.cookbook2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ShoppingList extends Fragment {

    private RecyclerView mRecyclerView;
    ImageView add_cart;
    ImageView clear_cart;
    EditText item_name;
    EditText item_quantity;
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_shopping_list, container, false);

        String username= FirebaseAuth.getInstance().getUid();
        DatabaseReference mainroot = FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
        DatabaseReference root = mainroot.child("User Inventory");

        item_name = (EditText) view.findViewById(R.id.input);
        item_quantity = (EditText) view.findViewById(R.id.inputquantity);

        Intent intent = new Intent(view.getContext(), AddCart.class);
        add_cart = (ImageView) view.findViewById(R.id.add);

        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInteger(item_quantity.getText().toString())){
                    Cart cart = new Cart();
                    cart.setItem_name(item_name.getText().toString());
                    cart.setQuantity(item_quantity.getText().toString());
                    new FirebaseHelper().updateCart(cart, new FirebaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoadedCart(List<Cart> carts, List<String> keys) {

                        }

                        @Override
                        public void DataInserted() {
                            Toast.makeText(view.getContext(), "The item has been added successfully into cart", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }

                else{
                    Toast.makeText(view.getContext(),"Please input a number",Toast.LENGTH_LONG).show();
                }
            }
        });
//        add_cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(intent);
//            }
//        });

        clear_cart = (ImageView) view.findViewById(R.id.clear_cart);
        clear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root.child("Cart").removeValue();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.cart_recycler);
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
                new CartRecyclerView_Config().setConfig(mRecyclerView, view.getContext(), carts, keys);
            }
        });

//        mRecyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                makeToast("Removed: " + items.get(i));
//                removeItem(i);
//                return false;
//            }
//        });

        return view;
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}