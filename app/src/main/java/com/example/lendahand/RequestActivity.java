package com.example.lendahand;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RequestActivity extends BaseActivity {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        SharedPreferences prefs = getSharedPreferences("LendAHandPrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Request");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Spinner setup
        Spinner itemSpinner = findViewById(R.id.itemSpinner);
        String[] items = {"Tinned Fish", "Blankets", "Canned Food", "Baby Formula",
                "Airtime Voucher (R12)","Stationery Pack", "Sanitary Pads","Jackets"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        itemSpinner.setAdapter(spinnerAdapter);

        // Get references to views
        EditText amountInput = findViewById(R.id.amountInput);
        EditText descriptionInput = findViewById(R.id.descriptionInput);
        Button confirmButton = findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedItem = itemSpinner.getSelectedItem().toString();
                String amountStr = amountInput.getText().toString().trim();
                String descriptionStr = descriptionInput.getText().toString().trim();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String currentDate = sdf.format(new Date());

                if (!amountStr.isEmpty()) {
                    try {
                        int amount = Integer.parseInt(amountStr);

                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("item", selectedItem);
                        jsonObj.put("amount", amount);
                        jsonObj.put("date", currentDate);
                        jsonObj.put("userId", userId);
                        jsonObj.put("description", descriptionStr);  // <-- Add description here

                        postData("https://lamp.ms.wits.ac.za/home/s2864063/submit_requests.php", jsonObj.toString());
                    } catch (NumberFormatException | IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    amountInput.setError("Please enter a number");
                }
            }
        });
    }

    void postData(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "Request failed. Please try again.", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "No response body";

                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Request submitted successfully!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        final String errorMessage = "Server error: " + response.code() + "\n" + responseBody;
                        Log.e("RequestActivity", "Server error: " + response.code() + "\n" + responseBody);
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
    }
}
