package com.example.lendahand;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
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
    public void login(String email, String password) {
        OkHttpClient client = new OkHttpClient();

        // Create request body with email and password
        RequestBody body = new FormBody.Builder()
                .add("Email", email)
                .add("Password", password)
                .build();

        // Build POST request to your PHP login script
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2864063/Login.php")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_LONG).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    runOnUiThread(() -> {
                        try {

                            JSONObject json = new JSONObject(responseBody);
                            String status = json.getString("status");

                            if (status.equals("success")) {
                                String userId = json.getString("user_id");
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                // Save user ID to SharedPreferences
                                SharedPreferences prefs = getSharedPreferences("LendAHandPrefs", MODE_PRIVATE);
                                prefs.edit().putString("user_id", userId).apply();
                                
                                // Redirect to home screen
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();

                            } else {
                                String message = json.optString("message", "Login failed");
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Server error", Toast.LENGTH_LONG).show()
                    );
                }
            }
        });
    }
}
