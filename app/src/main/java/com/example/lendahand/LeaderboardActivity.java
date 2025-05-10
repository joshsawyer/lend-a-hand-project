package com.example.lendahand;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LeaderboardActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private ArrayList<UserScore> leaderboardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        setupBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Top Donators");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Top Donators");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        leaderboardList = new ArrayList<>();

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2864063/get_top_donators.php";

        okhttp3.Request request = new Request.Builder()
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
                            String fullName = obj.getString("fullName");
                            int amount = obj.getInt("totalDonated");
                            leaderboardList.add(new UserScore(fullName, amount));
                        }

                        runOnUiThread(() -> { /*Update recycler view*/
                            RecyclerView recyclerView = findViewById(R.id.leaderboardRecyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(LeaderboardActivity.this));
                            adapter = new LeaderboardAdapter(leaderboardList);
                            recyclerView.setAdapter(adapter);
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//        recyclerView = findViewById(R.id.leaderboardRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        leaderboardList = new ArrayList<>();
//        leaderboardList.add(new UserScore("John Cena", 20));
//        leaderboardList.add(new UserScore("Ray Mysterio", 14));
//        leaderboardList.add(new UserScore("Spongebob", 7));
//        leaderboardList.add(new UserScore("Squidward", 6));
//        leaderboardList.add(new UserScore("Pravesh", 5));
//        leaderboardList.add(new UserScore("Patrick Star", 4));
//        leaderboardList.add(new UserScore("Mr Krabs", 0));
//
//        adapter = new LeaderboardAdapter(leaderboardList);
//        recyclerView.setAdapter(adapter);
    }
}