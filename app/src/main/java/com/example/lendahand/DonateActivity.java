package com.example.lendahand;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DonateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        setupBottomNavigation();
        /*getting the user data after a request was clicked on*/
        String name = getIntent().getStringExtra("username");
        String resource = getIntent().getStringExtra("resource");
        String bio = getIntent().getStringExtra("bio");
        int amount = getIntent().getIntExtra("amount", 0);
        int requestID = getIntent().getIntExtra("requestID", -1);


        TextView nameView = findViewById(R.id.requestorName);
        TextView bioView = findViewById(R.id.bioText);
        nameView.setText(name);
        bioView.setText(bio);


        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Donate");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Donate");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.requestedItemsRecyclerView);

        ArrayList<DonationItem> donationList = new ArrayList<>();
        donationList.add(new DonationItem("Blankets", 7, 3));
        donationList.add(new DonationItem("Tinned Fish", 10, 4));
        donationList.add(new DonationItem("Canned Beans", 5, 1));

        DonationItemAdapter adapter = new DonationItemAdapter(donationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        Spinner itemSpinner = findViewById(R.id.itemSpinner);
        String[] items = {"Tinned Fish", "Blankets", "Canned Beans", "Water", "Toiletries"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        itemSpinner.setAdapter(spinnerAdapter);
    }
}
