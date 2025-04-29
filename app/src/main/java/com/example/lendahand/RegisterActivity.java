package com.example.lendahand;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.submitButton);

        registerButton.setOnClickListener(v -> {
            // TODO: Validate registration (skip for now)
            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }
}