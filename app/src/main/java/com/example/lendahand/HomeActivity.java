package com.example.lendahand;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences("LendAHandPrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");
        if (userId.equals("-1")) {
            // Not logged in, redirect if needed
        }

        RecyclerView courseRV = findViewById(R.id.donationsRecyclerView);
        TextView emptyTextView = findViewById(R.id.emptyTextView);

        emptyTextView.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RequestActivity.class);
            startActivity(intent);
        });

        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2864063/get_users_summary_home.php";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(HomeActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String requestData = response.body().string();

                    try {
                        JSONArray jsonArray = new JSONArray(requestData);
                        homeItemList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String userID = obj.getString("User_ID");
                            String fullName = obj.getString("Full_Name");
                            int totalRequested = obj.getInt("Total_Requested");
                            int totalReceived = obj.getInt("Total_Received");
                            int percentReceived = (totalRequested == 0) ? 0 : (totalReceived * 100 / totalRequested);
                            String userLocation = "Lives in " + obj.getString("User_Location");

                            homeItemList.add(new HomeItem(fullName, totalRequested, percentReceived, userID, userLocation));
                        }

                        runOnUiThread(() -> {
                            userAdapter = new HomeAdaptor(homeItemList, homeItem -> {
                                Intent intent = new Intent(HomeActivity.this, DonateActivity.class);
                                intent.putExtra("userID", homeItem.getUserID());
                                intent.putExtra("fullName", homeItem.getFullName());
                                startActivity(intent);
                            });

                            courseRV.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                            courseRV.setAdapter(userAdapter);

                            if (homeItemList.isEmpty()) {
                                emptyTextView.setVisibility(View.VISIBLE);
                                courseRV.setVisibility(View.GONE);
                            } else {
                                emptyTextView.setVisibility(View.GONE);
                                courseRV.setVisibility(View.VISIBLE);
                            }

                            SearchView searchView = findViewById(R.id.searchBar);
                            searchView.setQueryHint("Search");
                            searchView.setIconifiedByDefault(false);
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

                            searchView.post(() -> {
                                EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
                                if (searchEditText != null) {
                                    searchEditText.setTextColor(Color.BLACK);
                                    searchEditText.setTypeface(ResourcesCompat.getFont(HomeActivity.this, R.font.inter));
                                    ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
                                    searchIcon.setColorFilter(Color.GRAY);
                                }
                            });
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        setupBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void fetchFilteredData(String searchQuery) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/~s2864063/get_users_summary_home.php?search=" + Uri.encode(searchQuery);

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(HomeActivity.this, "Error loading data", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        homeItemList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String userID = obj.getString("User_ID");
                            String fullName = obj.getString("Full_Name");
                            int totalRequested = obj.getInt("Total_Requested");
                            int totalReceived = obj.getInt("Total_Received");
                            int percentReceived = (totalRequested == 0) ? 0 : (totalReceived * 100 / totalRequested);
                            String userLocation = "Lives in " + obj.getString("User_Location");

                            homeItemList.add(new HomeItem(fullName, totalRequested, percentReceived, userID, userLocation));
                        }

                        runOnUiThread(() -> {
                            TextView emptyTextView = findViewById(R.id.emptyTextView);
                            RecyclerView courseRV = findViewById(R.id.donationsRecyclerView);

                            userAdapter.notifyDataSetChanged();

                            if (homeItemList.isEmpty()) {
                                emptyTextView.setVisibility(View.VISIBLE);
                                courseRV.setVisibility(View.GONE);
                            } else {
                                emptyTextView.setVisibility(View.GONE);
                                courseRV.setVisibility(View.VISIBLE);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
