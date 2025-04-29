package com.example.lendahand;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.Toolbar;

public class RequestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Request");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Request");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Spinner setup
        Spinner itemSpinner = findViewById(R.id.itemSpinner);
        String[] items = {"Tinned Fish", "Blankets", "Canned Beans", "Water", "Toiletries"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        itemSpinner.setAdapter(spinnerAdapter);

        // Inputs connected but logic will be added later
        EditText amountInput = findViewById(R.id.amountInput);
        Button confirmButton = findViewById(R.id.confirmButton);
    }
}