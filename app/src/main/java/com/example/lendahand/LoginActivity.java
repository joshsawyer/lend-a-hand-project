package com.example.lendahand;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupRedirectButton; // only shown if user clicks "No Account? Sign Up"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.continueButton);
        //signupRedirectButton = findViewById(R.id.signupRedirectButton);

        loginButton.setOnClickListener(v -> {
            // TODO: Validate login (skip validation for now)
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish(); // Prevent back to login
        });

        signupRedirectButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            // Don't finish here - user might go back to login
        });
    }
}
