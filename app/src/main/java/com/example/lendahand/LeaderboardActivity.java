package com.example.lendahand;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.SearchView;

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
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("LendAHandPrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");

        if (userId.equals("-1")) {
            // TODO: Redirect if needed
        }

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
        recyclerView = findViewById(R.id.leaderboardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LeaderboardAdapter(leaderboardList);
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.searchBar);
        searchView.setQueryHint("Search");
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchLeaderboardData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchLeaderboardData(newText);
                return false;
            }
        });

        fetchLeaderboardData(""); // Initial load
    }

    private void fetchLeaderboardData(String query) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2864063/get_top_donators.php?search=" + Uri.encode(query);

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("Leaderboard", "Failed to fetch data", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("SERVER_RESPONSE", responseData); // debug log

                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        ArrayList<UserScore> updatedList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String fullName = obj.getString("fullName");
                            int amount = obj.getInt("totalDonated");
                            String avatarUrl = obj.getString("avatarUrl");

                            updatedList.add(new UserScore(fullName, amount, avatarUrl));
                        }

                        runOnUiThread(() -> {
                            leaderboardList.clear();
                            leaderboardList.addAll(updatedList);
                            adapter.notifyDataSetChanged();
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Leaderboard", "Failed to parse JSON", e);
                    }
                }
            }
        });
    }
}