package com.example.lendahand;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView signUpLink;  // This will be the clickable link for sign-up
    private  TextView user_Email,user_Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        loginButton = findViewById(R.id.continueButton);
        signUpLink = findViewById(R.id.signUpLink);


        user_Email = findViewById(R.id.emailInput);
        user_Password = findViewById(R.id.passwordInput);

        // Handle the "Continue" login button click
        loginButton.setOnClickListener(v -> {
            String Email = user_Email.getText().toString().trim();
            String Password = user_Password.getText().toString().trim();

            if(Email.isEmpty() || Password.isEmpty()){
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidEmail(Email)) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
                return;
            }

            if(Password.length() < 8){
                Toast.makeText(this, "Password must be 8+ characters", Toast.LENGTH_LONG).show();
            }
            if (!isValidPassword(Password)) {
                Toast.makeText(this, " Password must include upper, lower, number & special character/s", Toast.LENGTH_LONG).show();
                return;
            }

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
    public boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()].*");
    }

    public void login(String email, String password){
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        /*Request request = new Request.Builder()
                .url()*/
    }
}
