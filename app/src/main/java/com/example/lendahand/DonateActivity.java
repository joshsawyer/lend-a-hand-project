package com.example.lendahand;

import static java.lang.String.valueOf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        String userId = prefs.getString("user_id", "-1"); // Default is -1 if not set

        if (userId != "-1") {
            // User is logged in
        } else {
            // No user found, maybe redirect to LoginActivity
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        setupBottomNavigation();
        /*getting the user data after a user on the home page was clicked on*/
        String name = getIntent().getStringExtra("fullName");
        String userID = getIntent().getStringExtra("userID");
        /*CONNECTING TO SERVER USING USER-ID*/
        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2864063/get_user_requests.php?userID=" + userID;

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String jsonData = response.body().string();
                    try{
                        JSONObject jsonObject = new JSONObject(jsonData);

                        String userLocation = "Lives in " + jsonObject.getString("User_Location");
                        String userPhone = "Phone Number: " + jsonObject.getString("User_Phone");
                        JSONArray requestArray = jsonObject.getJSONArray("Requests");

                        ArrayList<RequestItem>  requestList = new ArrayList<>();
                        ArrayList<String> itemNames = new ArrayList<>();
                        Map<String, Integer> amountNeededMap = new HashMap<>();

                        for (int i = 0; i < requestArray.length(); i++){
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
                                    //Toast.makeText(DonateActivity.this, "Please enter an amount.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DonateActivity.this, NosuccessActivity.class);
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("fullName", name);
                                    startActivity(intent);
                                    Toast.makeText(DonateActivity.this, "Please enter an amount.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                int enteredAmount = Integer.parseInt(inputText);

                                if (enteredAmount > needed) {
                                    //Toast.makeText(DonateActivity.this, "You're donating too much! Only " + needed + " more needed.", Toast.LENGTH_LONG).show();
                                    amountInput.setText(""); // clear field
                                    Intent intent = new Intent(DonateActivity.this, NosuccessActivity.class);
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("fullName", name);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(DonateActivity.this, "You're donating too much! Only " + needed + " more needed.", Toast.LENGTH_LONG).show();
                                } else {

                                    int requestID = 0;
                                    for(int i = 0; i<requestList.size();i++) {
                                        if(requestList.get(i).getItemName() == spinner.getSelectedItem().toString()) {
                                            requestID = requestList.get(i).getRequestID();
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
                                            String responseStr = response.body().string();
                                            runOnUiThread(() -> {
                                                Toast.makeText(DonateActivity.this, "Donation submitted!", Toast.LENGTH_SHORT).show();
                                                // optionally refresh data here
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
                                                    }
                                            );
                                        }

                                    });
                                    Toast.makeText(DonateActivity.this, "Thank you for your donation!", Toast.LENGTH_SHORT).show();

                                }
                            });


                        });
                    }
                    catch (JSONException e){
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





        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Donate");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Donate");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }
}
