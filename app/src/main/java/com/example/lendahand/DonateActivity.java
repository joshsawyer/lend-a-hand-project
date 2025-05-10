package com.example.lendahand;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public class DonateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

                        String userBio = jsonObject.getString("User_Bio");
                        JSONArray requestArray = jsonObject.getJSONArray("Requests");

                        ArrayList<DonationItem>  donationList = new ArrayList<>();

                        for (int i = 0; i < requestArray.length(); i++){
                            JSONObject requestObject = requestArray.getJSONObject(i);

                            String resourceName = requestObject.getString("Resource_Name");
                            int amountRequested = requestObject.getInt("Amount_Requested");
                            int amountReceived = requestObject.getInt("Amount_Received");
                            String requestBio = requestObject.getString("Request_Bio");
                            String dateRequested = requestObject.getString("Date_Requested");

                            donationList.add(new DonationItem(resourceName, amountRequested, amountReceived, requestBio));

                        }
                        runOnUiThread(() -> {
                            TextView bioView = findViewById(R.id.bioText); // Add this view in your XML
                            bioView.setText(userBio);

                            RecyclerView recyclerView = findViewById(R.id.requestedItemsRecyclerView);
                            DonationItemAdapter adapter = new DonationItemAdapter(donationList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(DonateActivity.this));
                            recyclerView.setAdapter(adapter);
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

//        // Setup RecyclerView
//        RecyclerView recyclerView = findViewById(R.id.requestedItemsRecyclerView);
//
//        ArrayList<DonationItem> donationList = new ArrayList<>();
//        donationList.add(new DonationItem("Blankets", 7, 3, "I went hungry for a week. no food only stompted on sweets from the floor."));
//        donationList.add(new DonationItem("Tinned Fish", 10, 4,"I went hungry for a week. no food only stompted on sweets from the floor."));
//        donationList.add(new DonationItem("Canned Beans", 5, 1,"I went hungry for a week. no food only stompted on sweets from the floor."));
//
//        DonationItemAdapter adapter = new DonationItemAdapter(donationList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recyclerView.setAdapter(adapter);
//
//        Spinner itemSpinner = findViewById(R.id.itemSpinner);
//        String[] items = {"Tinned Fish", "Blankets", "Canned Beans", "Water", "Toiletries"};
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        itemSpinner.setAdapter(spinnerAdapter);
    }
}
