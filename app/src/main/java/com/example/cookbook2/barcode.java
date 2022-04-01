package com.example.cookbook2;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

public class barcode extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 101;
    private CodeScanner mCodeScanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        this.setupPermission();

        String username=FirebaseAuth.getInstance().getUid();

        DatabaseReference mainroot=FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
        DatabaseReference root = mainroot.child("User Inventory");
        DatabaseReference locate = root.child("Fridge");//this is fridge

        TextView textView = (TextView) findViewById(R.id.text_view); // text view from activity.xml

        CodeScannerView scannerView = findViewById(R.id.scanner_view); // barcode from activity.xml
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(barcode.this, result.getText(), Toast.LENGTH_LONG).show();
                        textView.setText(result.getText());

                        mainroot.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String productName= snapshot.child("Productlist").child(result.getText()).child("Title").getValue().toString();
                                DataSnapshot fridgestuff=snapshot.child(username).child("Fridge");
                                if (fridgestuff.child(productName).hasChild("qty")){// this if else statement increases the qty of existing element if it already exists in the list.
                                    String temp= (String) fridgestuff.child(productName).child("qty").getValue();
                                    int qty=parseInt(temp);
                                    locate.child(productName).child("qty").setValue(String.valueOf(++qty));
                                }
                                else {
                                    locate.child(productName).child("qty").setValue("1");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
            }

        });

        // a click to let the scanner know to scan anew barcode
        scannerView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mCodeScanner.startPreview();
            }

        });

    }



    @Override
    protected void onResume(){
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause(){
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void setupPermission(){
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest();
        }
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
    }



    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "TURN ON CAMERA NOW!!", Toast.LENGTH_SHORT).show();
                } else {
                    //successful
                    return;
                }

            }
        }
    }
}


