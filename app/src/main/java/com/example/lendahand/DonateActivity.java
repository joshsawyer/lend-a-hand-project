package com.example.lendahand;

import static java.lang.String.valueOf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("LendAHandPrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        setupBottomNavigation();

        String name = getIntent().getStringExtra("fullName");
        String userID = getIntent().getStringExtra("userID");

        if (userId.equals(userID)) {
            Toast.makeText(this, "You cannot donate to yourself.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        fetchRequestorProfileImage(userID);

        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2864063/get_user_requests.php?userID=" + userID;

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);

                        String userLocation = "Lives in " + jsonObject.getString("User_Location");
                        String userPhone = "Phone Number: " + jsonObject.getString("User_Phone");
                        JSONArray requestArray = jsonObject.getJSONArray("Requests");

                        ArrayList<RequestItem> requestList = new ArrayList<>();
                        ArrayList<String> itemNames = new ArrayList<>();
                        Map<String, Integer> amountNeededMap = new HashMap<>();

                        for (int i = 0; i < requestArray.length(); i++) {
                            JSONObject requestObject = requestArray.getJSONObject(i);
                            String resourceName = requestObject.getString("Resource_Name");
                            int amountRequested = requestObject.getInt("Amount_Requested");
                            int amountReceived = requestObject.getInt("Amount_Received");
                            String requestBio = requestObject.getString("Request_Bio");
                            String dateRequested = requestObject.getString("Date_Requested");
                            int requestID = requestObject.getInt("Request_ID");

                            requestList.add(new RequestItem(resourceName, amountRequested, amountReceived, requestBio, dateRequested, requestID));
                            int amountStillNeeded = amountRequested - amountReceived;
                            amountNeededMap.put(resourceName, amountStillNeeded);
                            itemNames.add(resourceName);
                        }

                        runOnUiThread(() -> {
                            TextView bioView = findViewById(R.id.LocationText);
                            bioView.setText(userLocation);
                            TextView phoneNo = findViewById(R.id.phoneNumber);
                            phoneNo.setText(userPhone);

                            RecyclerView recyclerView = findViewById(R.id.requestedItemsRecyclerView);
                            RequestItemAdapter adapter = new RequestItemAdapter(requestList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(DonateActivity.this));
                            recyclerView.setAdapter(adapter);

                            Spinner spinner = findViewById(R.id.itemSpinner);
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(DonateActivity.this, android.R.layout.simple_spinner_item, itemNames);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(spinnerAdapter);

                            EditText amountInput = findViewById(R.id.amountInput);
                            Button confirmButton = findViewById(R.id.confirmButton);

                            confirmButton.setOnClickListener(v -> {
                                String selectedItem = spinner.getSelectedItem().toString();
                                int needed = amountNeededMap.get(selectedItem);
                                String inputText = amountInput.getText().toString();

                                if (inputText.isEmpty()) {
                                    Intent intent = new Intent(DonateActivity.this, NosuccessActivity.class);
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("fullName", name);
                                    startActivity(intent);
                                    Toast.makeText(DonateActivity.this, "Please enter an amount.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                int enteredAmount = Integer.parseInt(inputText);

                                if (enteredAmount > needed) {
                                    amountInput.setText("");
                                    Intent intent = new Intent(DonateActivity.this, NosuccessActivity.class);
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("fullName", name);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(DonateActivity.this, "You're donating too much! Only " + needed + " more needed.", Toast.LENGTH_LONG).show();
                                } else {
                                    int requestID = 0;
                                    for (RequestItem item : requestList) {
                                        if (item.getItemName().equals(selectedItem)) {
                                            requestID = item.getRequestID();
                                            break;
                                        }
                                    }

                                    Log.d("DONOR_ID", "Donating as user: " + userId);

                                    RequestBody formBody = new FormBody.Builder()
                                            .add("donor_id", String.valueOf(userId))
                                            .add("request_id", valueOf(requestID))
                                            .add("amount", valueOf(enteredAmount))
                                            .build();

                                    Request postRequest = new Request.Builder()
                                            .url("https://lamp.ms.wits.ac.za/home/s2864063/submit_donation.php")
                                            .post(formBody)
                                            .build();

                                    client.newCall(postRequest).enqueue(new Callback() {
                                        @Override
                                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                            runOnUiThread(() -> {
                                                Toast.makeText(DonateActivity.this, "Donation submitted!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(DonateActivity.this, SuccessActivity.class);
                                                intent.putExtra("userID", userID);
                                                intent.putExtra("fullName", name);
                                                startActivity(intent);
                                                finish();
                                            });
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                            e.printStackTrace();
                                            runOnUiThread(() -> {
                                                Toast.makeText(DonateActivity.this, "Failed to donate", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(DonateActivity.this, NosuccessActivity.class);
                                                intent.putExtra("userID", userID);
                                                intent.putExtra("fullName", name);
                                                startActivity(intent);
                                                finish();
                                            });
                                        }
                                    });
                                    Toast.makeText(DonateActivity.this, "Thank you for your donation!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });

        TextView nameView = findViewById(R.id.requestorName);
        nameView.setText(name);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Donate");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Donate");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void fetchRequestorProfileImage(String recipientUserId) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2864063/profile.php?userID=" + recipientUserId;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(DonateActivity.this, "Could not load profile image", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String imageUrl = jsonObject.getString("Avatar_URL");

                        runOnUiThread(() -> {
                            ImageView profileImage = findViewById(R.id.profileImage);
                            Glide.with(DonateActivity.this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.ic_profile_placeholder)
                                    .circleCrop()
                                    .into(profileImage);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}

