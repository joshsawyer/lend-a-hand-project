package com.example.lendahand;

import android.os.Bundle;

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

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Donate");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Donate");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
    }
}
