package com.example.cookbook2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class OpenBarcodeActivity extends Fragment {

    private View view;
    private Button button;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_cart);
        view = inflater.inflate(R.layout.activity_openbarcode, container, false);

        button = (Button) view.findViewById(R.id.barcodeopener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), BarcodeActivity.class));
            }
        });

//        binding = ActivityOpenbarcodeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
        return view;
    }
}

