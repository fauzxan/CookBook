package com.example.cookbook2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/*
The file is one of the fragments which describes how the user can add items to the inventory list by scanning an item. This file contains a button, which
on click opens the camera, to scan the barcode.
 */

public class OpenBarcodeActivity extends Fragment {

    private View view;
    private Button button;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_openbarcode, container, false);

        button = (Button) view.findViewById(R.id.barcodeopener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), BarcodeActivity.class));
            }
        });
        return view;
    }
}

