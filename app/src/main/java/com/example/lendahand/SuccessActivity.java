package com.example.lendahand;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_success); // Make sure this matches your XML file

        Button returnHome = findViewById(R.id.returnHomeButton); // Now it's safe
        returnHome.setOnClickListener(v -> {
            Intent intent = new Intent(SuccessActivity.this, DonateActivity.class);
            intent.putExtra("userID", getIntent().getStringExtra("userID"));
            intent.putExtra("fullName", getIntent().getStringExtra("fullName"));
            startActivity(intent);
            finish();
        });
    }
}
