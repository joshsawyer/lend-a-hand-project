package com.example.lendahand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText user_Name, user_Email, user_Password, user_Number, user_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user_Name = findViewById(R.id.usernameInput);
        user_Email = findViewById(R.id.emailInput2);
        user_Password = findViewById(R.id.passwordInput);
        user_Number = findViewById(R.id.contactNo);
        user_ID = findViewById(R.id.idNo);

        registerButton = findViewById(R.id.submitButton);

        registerButton.setOnClickListener(v -> {
            String Name = user_Name.getText().toString().trim();
            String Email = user_Email.getText().toString().trim();
            String Password = user_Password.getText().toString().trim();
            String Number = user_Number.getText().toString().trim();
            String ID = user_ID.getText().toString().trim();

            if (Name.isEmpty() || Email.isEmpty() || Password.isEmpty() || Number.isEmpty() || ID.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                return;
            }

            if (Name.length() < 4 || !isValidName(Name)) {
                Toast.makeText(this, "Sorry, Name must be at least 4 characters and only contain letters, spaces, or hyphens", Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidEmail(Email)) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
                return;
            }

            if (Password.length() < 8 || !isValidPassword(Password)) {
                Toast.makeText(this, "Password must be 8+ characters and include upper, lower, number & special character", Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidNumber(Number)) {
                Toast.makeText(this, "Please enter a valid 10-digit phone number starting with 06, 07, or 08", Toast.LENGTH_LONG).show();
                return;
            }

            if(!isValidID(ID)){
                Toast.makeText(this, "Please enter a valid South African ID", Toast.LENGTH_LONG).show();
                return;
            }

            //if all fields are valid the allow user to register
            registerUser(Name, Email, Password, Number, ID);

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

    public boolean isValidName(String name) {
        return name.matches("^[A-Za-z]+([ '-][A-Za-z]+)*$");
    }

    public boolean isValidNumber(String number){
        char firstNo = number.charAt(0);
        return number.matches("^0[6-8][0-9]{8}$");
    }

    public boolean isValidID(String ID){
        if(ID.length() != 13) return false;
        if(!ID.matches("\\d+")) return false;
        String DOB = ID.substring(0,6);
        int gender = Integer.parseInt(ID.substring(6, 10));
        int citizenship = Integer.parseInt(ID.substring(10, 11));

        if(!isValidDate(DOB)) return false;

        if(gender > 9999) return false;

        if (citizenship == 0 || citizenship == 1) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isValidDate(String DOB){
        int year,month,day,gender,Citizenship;
        year = Integer.parseInt(DOB.substring(0,2));
        month = Integer.parseInt(DOB.substring(2,4));
        day = Integer.parseInt(DOB.substring(4,6));

        if(year > 25) return false;
        if(month > 12) return false;
        if(day > 31) return false;

        return true;
    }

    public void registerUser(String name, String email, String password, String number, String ID){
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("Name", name)
                .add("Email", email)
                .add("Password", password)
                .add("Number", number)
                .add("ID", ID)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2809967/Register.php")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseText = response.body().string();
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responseText.equalsIgnoreCase("You have successfully registered")) {
                                Toast.makeText(RegisterActivity.this, responseText, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }
                        }
                    });
                }

                else {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"something went wrong", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_LONG).show();
                    }
                }
                );
            }
        });

    }
}