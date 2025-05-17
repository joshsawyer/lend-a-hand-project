package com.example.lendahand;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Do NOT call setContentView here!

    }

    protected void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                if (!(this instanceof HomeActivity)) {
                    startActivity(new Intent(this, HomeActivity.class));
                    overridePendingTransition(0, 0);
                    finish(); // Optional: finish the current activity
                }
                return true;
            } else if (id == R.id.nav_profile) {
                if (!(this instanceof ProfileActivity)) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
                return true;
            } else if (id == R.id.nav_leaderboard) {
                if (!(this instanceof LeaderboardActivity)) {
                    startActivity(new Intent(this, LeaderboardActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
                return true;
            }
            return false;
        });
    }
}



