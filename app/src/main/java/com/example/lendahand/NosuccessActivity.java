package com.example.lendahand;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class NosuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_nosuccess);

        Button returnHome = findViewById(R.id.returnHomeButton);
        returnHome.setOnClickListener(v -> {
            Intent intent = new Intent(NosuccessActivity.this, DonateActivity.class);
            intent.putExtra("userID", getIntent().getStringExtra("userID"));
            intent.putExtra("fullName", getIntent().getStringExtra("fullName"));
            startActivity(intent);
            finish();
        });
    }
}
