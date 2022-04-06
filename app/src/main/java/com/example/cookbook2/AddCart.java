package com.example.cookbook2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddCart extends AppCompatActivity {

    EditText item_name;
    EditText item_quantity;
    Button add_button;
    Button back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);
        Intent intent = new Intent(AddCart.this, ShoppingList.class);

        item_name = (EditText) findViewById(R.id.item_name_input);
        item_quantity = (EditText) findViewById(R.id.item_quantity_input);
        add_button = (Button) findViewById(R.id.add_item_button);
        back_button = (Button) findViewById(R.id.back_button_cart);


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart cart = new Cart();
                cart.setItem_name(item_name.getText().toString());
                cart.setQuantity(item_quantity.getText().toString());
                new FirebaseHelper().addCart(cart, new FirebaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Product> products, List<String> keys) {

                    }

                    @Override
                    public void DataIsLoadedCart(List<Cart> carts, List<String> keys) {

                    }

                    @Override
                    public void DataInserted() {
                        Toast.makeText(AddCart.this, "The item has been added successfully into cart", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });

            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
}