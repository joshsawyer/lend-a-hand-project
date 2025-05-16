package com.example.lendahand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends BaseActivity {

    ArrayList<HomeItem> homeItemList = new ArrayList<>();
    private HomeAdaptor userAdapter;


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
        setContentView(R.layout.activity_home);


        /*CONNECTING TO SERVER*/
        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2864063/get_users_summary_home.php";

        Request request = new Request.Builder()
                .url(url)
                .build();
        /*if fails*/
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            /*if Succeeds*/
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String requestData = response.body().string();

                    try {
                        JSONArray jsonArray = new JSONArray(requestData);


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String userID = obj.getString("User_ID");
                            String fullName = obj.getString("Full_Name");
                            int totalRequested = obj.getInt("Total_Requested");
                            int totalReceived = obj.getInt("Total_Received");
                            int percentReceived = (totalReceived*100/totalRequested);
                            String userBio = obj.getString("User_Bio");

                            homeItemList.add(new HomeItem(fullName, totalRequested, percentReceived, userID, userBio));
                        }

                        runOnUiThread(() -> { /*Update recycler view*/
                            RecyclerView courseRV = findViewById(R.id.donationsRecyclerView);
                            userAdapter = new HomeAdaptor(homeItemList, homeItem -> {
                                /*When a user is clicked this goes to DonateActivity where the user's name will be sent to that activity*/
                                Intent intent = new Intent(HomeActivity.this, DonateActivity.class);
                                intent.putExtra("userID", homeItem.getUserID());
                                intent.putExtra("fullName", homeItem.getFullName());

                                startActivity(intent);
                            });
                            SearchView searchView = findViewById(R.id.searchBar);
                            searchView.setQueryHint("Search");
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    fetchFilteredData(query);
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    fetchFilteredData(newText);
                                    return false;
                                }
                            });
                            /*Keeps searchbar expanded so te hitbox isn't just the icon*/
                            searchView.setIconifiedByDefault(false);

                            /*To see the search icon and searchbar text hint. makes them darker*/
                            searchView.post(() -> {
                                EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
                                if (searchEditText != null) {
                                    searchEditText.setTextColor(Color.BLACK);

                                    searchEditText.setTypeface(ResourcesCompat.getFont(HomeActivity.this, R.font.inter)); // Optional
                                    ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
                                    searchIcon.setColorFilter(Color.GRAY); // Choose any visible color

                                } else {
                                    Toast.makeText(HomeActivity.this, "Search bar text field not found", Toast.LENGTH_SHORT).show();
                                }
                            });


                            courseRV.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                            courseRV.setAdapter(userAdapter);
                            
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        setupBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void fetchFilteredData(String searchQuery) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/~s2864063/get_users_summary_home.php?search=" + Uri.encode(searchQuery);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(HomeActivity.this, "Error loading data", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    try {
                        JSONArray jsonArray = new JSONArray(responseData);

                        // Update the data list
                        homeItemList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String userID = obj.getString("User_ID");
                            String fullName = obj.getString("Full_Name");
                            int totalRequested = obj.getInt("Total_Requested");
                            int totalReceived = obj.getInt("Total_Received");
                            int percentReceived = (totalRequested == 0) ? 0 : (totalReceived * 100 / totalRequested);
                            String userBio = obj.getString("User_Bio");

                            homeItemList.add(new HomeItem(fullName, totalRequested, percentReceived, userID, userBio));
                        }

                        // Refresh the RecyclerView on the UI thread
                        runOnUiThread(() -> userAdapter.notifyDataSetChanged());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
