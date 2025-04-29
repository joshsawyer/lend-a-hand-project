package com.example.lendahand;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class LeaderboardActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private ArrayList<UserScore> leaderboardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Top Donators");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.leaderboardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        leaderboardList = new ArrayList<>();
        leaderboardList.add(new UserScore("John Cena", 20));
        leaderboardList.add(new UserScore("Ray Mysterio", 14));
        leaderboardList.add(new UserScore("Spongebob", 7));
        leaderboardList.add(new UserScore("Squidward", 6));
        leaderboardList.add(new UserScore("Pravesh", 5));
        leaderboardList.add(new UserScore("Patrick Star", 4));
        leaderboardList.add(new UserScore("Mr Krabs", 0));

        adapter = new LeaderboardAdapter(leaderboardList);
        recyclerView.setAdapter(adapter);
    }
}