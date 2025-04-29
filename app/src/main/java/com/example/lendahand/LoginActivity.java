package com.example.lendahand;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView signUpLink;  // This will be the clickable link for sign-up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        loginButton = findViewById(R.id.continueButton);
        signUpLink = findViewById(R.id.signUpLink);

        // Handle the "Continue" login button click
        loginButton.setOnClickListener(v -> {
            // TODO: Validate login (skip validation for now)
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class); // Navigate to HomeActivity
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish(); // Prevent back to login
        });

        // Handle the sign-up link click
        signUpLink.setOnClickListener(v -> {
            // Navigate to RegisterActivity for sign-up
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }
}
