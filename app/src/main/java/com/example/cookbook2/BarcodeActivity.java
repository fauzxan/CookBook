package com.example.cookbook2;

import static java.lang.Integer.parseInt;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class BarcodeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private static final int CAMERA_REQUEST_CODE = 101;
    private CodeScanner mCodeScanner;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        this.setupPermission();

        String username=FirebaseAuth.getInstance().getUid();

        DatabaseReference mainmain = FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        DatabaseReference mainroot=FirebaseDatabase.getInstance("https://cookbook-59b04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(username);
        DatabaseReference root = mainroot.child("User Inventory");
        DatabaseReference locate = root.child("Location");//this is location of food stored

        TextView textView = (TextView) findViewById(R.id.text_view); // text view from activity.xml

        CodeScannerView scannerView = findViewById(R.id.scanner_view); // BarcodeActivity from activity.xml
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BarcodeActivity.this, "Please enter expiry date", Toast.LENGTH_LONG).show();
                        textView.setText(result.getText());

                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                BarcodeActivity.this,
                                (DatePickerDialog.OnDateSetListener) BarcodeActivity.this,
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

                                mainmain.addListenerForSingleValueEvent(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot)
                                    {
                                        String productName= snapshot.child("Productlist").child(result.getText()).child("Title").getValue().toString();
                                        DataSnapshot fridgestuff=snapshot.child(username).child("User Inventory").child("Location");
                                        locate.child(productName).child(productName).setValue(productName);
                                        if (fridgestuff.child(productName).hasChild("qty")){// this if else statement increases the qty of existing element if it already exists in the list.
                                            String temp= ""+fridgestuff.child(productName).child("qty").getValue();
                                            int qty=parseInt(temp);
                                            locate.child(productName).child("qty").setValue(String.valueOf(++qty));

                                            String old=fridgestuff.child(productName).child("zED").getValue().toString().concat(""+date+",");
                                            List<String> toSort = Arrays.asList(old.split(","));
                                            Collections.sort(toSort);
                                            String newDates = TextUtils.join(",",toSort)+",";
                                            locate.child(productName).child("zED").setValue(newDates);
                                        }
                                        else {
                                            locate.child(productName).child("qty").setValue("1");
                                            locate.child(productName).child("zED").setValue(""+date+",");
                                        }
                                        UpdateExpiry adder = new UpdateExpiry();
                                        adder.addItem(productName,date);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error)
                                    {

                                    }
                                });
                            }
                        });
                    }
                });
            }

        });

        // a click to let the scanner know to scan anew BarcodeActivity
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
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
    }
}