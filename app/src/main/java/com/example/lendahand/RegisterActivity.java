package com.example.lendahand;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private Spinner locationSpinner;
    private String selectedLocation = "";
    private Button registerButton;  // Button to trigger registration
    private EditText user_FName, user_LName, user_Email, user_Password, user_Number, user_ID; // Fields to input user data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Set layout for Register Activity

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Sign Up");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sign Up");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Initialize the input fields and button by finding them in the layout
        user_FName = findViewById(R.id.usernameInput);
        user_LName = findViewById(R.id.lNameInput);
        user_Email = findViewById(R.id.emailInput2);
        user_Password = findViewById(R.id.passwordInput);
        user_Number = findViewById(R.id.contactNo);
        user_ID = findViewById(R.id.idNo);
        locationSpinner = findViewById(R.id.locationSpinner);
        registerButton = findViewById(R.id.submitButton); // Register button initialization

        /* Populate the spinner */
        List<String> locations = Arrays.asList(
                "Select Location","Johannesburg", "Pretoria", "Cape Town", "Durban",
                "Gqeberha (Port Elizabeth)", "Bloemfontein", "East London",
                "Kimberley", "Polokwane", "Mthatha", "Nelspruit (Mbombela)",
                "Rustenburg", "Pietermaritzburg", "George", "Mahikeng",
                "Upington", "Welkom"
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,

                android.R.layout.simple_spinner_item,
                locations
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        /* Keep track of what the user picks */
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedLocation = parent.getItemAtPosition(pos).toString();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { selectedLocation = ""; }
        });

        // Set up an onClickListener for the register button
        registerButton.setOnClickListener(v -> {
            // Get input values from the EditText fields
            String FName = user_FName.getText().toString().trim();
            String LName = user_LName.getText().toString().trim();
            String Email = user_Email.getText().toString().trim();
            String Password = user_Password.getText().toString().trim();
            String Number = user_Number.getText().toString().trim();
            String ID = user_ID.getText().toString().trim();
            locationSpinner = findViewById(R.id.locationSpinner);



            // Check if any field is empty
            if (FName.isEmpty() || LName.isEmpty() || Email.isEmpty() || Password.isEmpty() || Number.isEmpty() || ID.isEmpty() || selectedLocation.equals("Select Location")) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                return;
            }

            // Validate first name (should only contain letters)
            if (!isValidFName(FName)) {
                Toast.makeText(this, "Sorry, first name must only contain letters ", Toast.LENGTH_LONG).show();
                return;
            }

            // Validate last name (should only contain letters)
            if (!isValidLName(LName)) {
                Toast.makeText(this, "Sorry, last name must only contain letters", Toast.LENGTH_LONG).show();
                return;
            }

            // Validate email format
            if (!isValidEmail(Email)) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
                return;
            }

            // Validate password (must be at least 8 characters, include uppercase, number & special character)
            if (Password.length() < 8 || !isValidPassword(Password)) {
                Toast.makeText(this, "Password must be 8+ characters and include atleast 1 UPPERCASE, NUMBER & SPECIAL CHARACTER", Toast.LENGTH_LONG).show();
                return;
            }

            // Validate phone number (must be a 10-digit number starting with 06, 07, or 08)
            if (!isValidNumber(Number)) {
                Toast.makeText(this, "Please enter a valid 10-digit phone number starting with 06, 07, or 08", Toast.LENGTH_LONG).show();
                return;
            }

            // Validate South African ID number
            if (!isValidID(ID)) {
                Toast.makeText(this, "Please enter a valid South African ID", Toast.LENGTH_LONG).show();
                return;
            }


            // If all fields are valid, proceed to register the user
            register(FName, LName, Email, Password, Number, ID, selectedLocation);
        });
    }

    // Function to validate email format using regular expression
    public boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    // Function to validate password strength (length, uppercase, number, special character)
    public boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&    // At least one uppercase letter
                password.matches(".*[a-z].*") &&    // At least one lowercase letter
                password.matches(".*\\d.*") &&      // At least one digit
                password.matches(".*[!@#$%^&*()].*"); // At least one special character
    }

    // Function to validate first name (should contain only letters)
    public boolean isValidFName(String fname) {
        return fname.matches("^[A-Za-z]+$");
    } 

    // Function to validate last name (should contain only letters)
    public boolean isValidLName(String lname) {
        return lname.matches("^[A-Za-z]+$");
    }

    // Function to validate phone number (should be 10 digits, starting with 06, 07, or 08)
    public boolean isValidNumber(String number) {
        return number.matches("^0[6-8][0-9]{8}$");
    }

    // Function to validate South African ID number
    public boolean isValidID(String ID) {
        if (ID.length() != 13) return false; // ID must be 13 digits long
        if (!ID.matches("\\d+")) return false; // ID must be numeric

        String DOB = ID.substring(0, 6); // Date of Birth (YYMMDD)
        int gender = Integer.parseInt(ID.substring(6, 10)); // Gender (codes from 0000 to 9999)
        int citizenship = Integer.parseInt(ID.substring(10, 11)); // Citizenship (0 = South African, 1 = Permanent resident)

        if (!isValidDate(DOB)) return false; // Validate date
        if (gender > 9999) return false; // Gender code must be less than or equal to 9999
        return citizenship == 0 || citizenship == 1; // Citizenship must be 0 or 1
    }

    // Function to validate the date part of the South African ID (YYMMDD)
    public boolean isValidDate(String DOB) {
        int year = Integer.parseInt(DOB.substring(0, 2));
        int month = Integer.parseInt(DOB.substring(2, 4));
        int day = Integer.parseInt(DOB.substring(4, 6));

        // Validate if the date is a valid calendar date
        if (year > 25) return false; // Year must be less than or equal to 25 (for 2000-2025)
        if (month > 12) return false; // Month must be between 1 and 12
        if (day > 31) return false; // Day must be between 1 and 31
        return true;
    }

    // Function to send registration data to the server using OkHttp
    public void register(String fname, String lname, String email, String password, String number, String id, String location) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("FName", fname)
                .add("LName", lname)
                .add("Email", email)
                .add("Password", password)
                .add("Number", number)
                .add("ID", id)
                .add("Location", location)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2864063/Register.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_LONG).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String body = response.body().string();

                Log.e("RegisterRaw", "Server response: " + body);
                runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(body);
                        String status = json.getString("status");

                        if (status.equals("success")) {
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            String message = json.optString("message", "Registration failed");
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, "Response error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
