package com.example.lendahand;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton; // Button for logging in
    private TextView signUpLink;  // TextView used as a clickable link for sign-up
    private TextView user_Email, user_Password; // Fields to enter user email and password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Sets the layout of the activity

        // Initialize views by finding them from layout
        loginButton = findViewById(R.id.continueButton);
        signUpLink = findViewById(R.id.signUpLink);
        user_Email = findViewById(R.id.emailInput);
        user_Password = findViewById(R.id.passwordInput);

        // Handle login button click
        loginButton.setOnClickListener(v -> {
            String Email = user_Email.getText().toString().trim(); // Get email input
            String Password = user_Password.getText().toString().trim(); // Get password input

            // Check if any fields are empty
            if (Email.isEmpty() || Password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                return;
            }

            // Validate email format
            if (!isValidEmail(Email)) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
                return;
            }

            // Validate password strength
            if (!isValidPassword(Password)) {
                Toast.makeText(this, " Password must be 8+ characters including upper, lower, number & special character/s", Toast.LENGTH_LONG).show();
                return;
            }

            // Call login function if validation passes
            login(Email, Password);
        });

        // Handle sign-up link click
        signUpLink.setOnClickListener(v -> {
            // Start the RegisterActivity when user clicks "sign up"
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // Removes transition animation
        });
    }

    // Function to validate email format using regular expressions
    public boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    // Function to check password meets certain strength requirements
    public boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&       // at least one uppercase letter
                password.matches(".*[a-z].*") &&       // at least one lowercase letter
                password.matches(".*\\d.*") &&         // at least one digit
                password.matches(".*[!@#$%^&*()].*");  // at least one special character
    }

    // Function to perform login using OkHttp (HTTP client)
    public void login(String email, String password){
        OkHttpClient client = new OkHttpClient(); // Create an OkHttp client instance

        // Create request body with email and password as POST data
        RequestBody body = new FormBody.Builder()
                .add("Email", email)
                .add("Password", password)
                .build();

        // Build POST request to PHP login script
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2809967/Login.php")
                .post(body)
                .build();

        // Make asynchronous HTTP call
        client.newCall(request).enqueue(new Callback() {

            // If the request fails (e.g., no internet)
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace(); // Print error for debugging
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_LONG).show();
                    }
                });
            }

            // If response is received from the server
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){ // Check if response is HTTP 200 OK
                    String responseBody = response.body().string(); // Read response as string

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Show specific messages based on response from PHP
                            if (responseBody.equalsIgnoreCase("Wrong password")) {
                                Toast.makeText(LoginActivity.this, responseBody, Toast.LENGTH_LONG).show();
                            }
                            else if (responseBody.equalsIgnoreCase("User not found")) {
                                Toast.makeText(LoginActivity.this, responseBody, Toast.LENGTH_LONG).show();
                            }
                            else if (responseBody.equalsIgnoreCase("You have successfully logged in")){
                                Toast.makeText(LoginActivity.this, responseBody, Toast.LENGTH_LONG).show();

                                // If login is successful, go to HomeActivity
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0); // Remove transition animation
                                finish(); // Close LoginActivity so it can't be returned to with the back button
                            }
                        }
                    });
                }
                else {
                    // Handle failed HTTP response
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"something went wrong ", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
