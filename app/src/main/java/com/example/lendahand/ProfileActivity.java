package com.example.lendahand;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;

public class ProfileActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private Button requestItemButton;
    private OkHttpClient client = new OkHttpClient();
    TextView userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("LendAHandPrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");

        if (!userId.equals("-1")) {
            // User is logged in
        } else {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.username);

        fetchUserInfo(userId);
        checkForNewReceivedDonations(userId);  // <-- Added this line

        setupBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        requestItemButton = findViewById(R.id.requestItemButton);

        viewPager.setAdapter(new ProfilePagerAdapter(this, userId));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Donated");
            else tab.setText("Requested");
        }).attach();

        requestItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, RequestActivity.class);
            startActivity(intent);
        });
    }

    private static class ProfilePagerAdapter extends FragmentStateAdapter {
        private final String userId;

        public ProfilePagerAdapter(@NonNull FragmentActivity fragmentActivity, String userId) {
            super(fragmentActivity);
            this.userId = userId;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment;
            if (position == 0) {
                fragment = new DonatedItemsFragment();
            } else {
                fragment = new RequestedItemsFragment();
            }

            Bundle args = new Bundle();
            args.putString("userId", userId);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    private void fetchUserInfo(String userId) {
        String url = "https://lamp.ms.wits.ac.za/home/s2864063/profile.php?userID=" + userId;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(ProfileActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonString = response.body().string();

                    try {
                        JSONObject json = new JSONObject(jsonString);
                        String fname = json.getString("User_FName");
                        String lname = json.getString("User_LName");
                        String imageUrl = json.getString("Avatar_URL");
                        String fullName = fname + " " + lname;

                        runOnUiThread(() -> {
                            userName.setText(fullName);
                            ImageView profileImage = findViewById(R.id.profileImage);
                            Glide.with(ProfileActivity.this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.ic_profile_placeholder)
                                    .circleCrop()
                                    .into(profileImage);
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(ProfileActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show()
                        );
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(ProfileActivity.this, "Server error", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void checkForNewReceivedDonations(String userId) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2864063/get_latest_received_donation.php?userID=" + userId;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Do nothing
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject obj = new JSONObject(json);
                        String latestDonationTime = obj.getString("latest");

                        SharedPreferences prefs = getSharedPreferences("LendAHandPrefs", MODE_PRIVATE);
                        String lastSeen = prefs.getString("last_seen_donation", "");

                        if (latestDonationTime != null && !latestDonationTime.equals("") && !latestDonationTime.equals(lastSeen)) {
                            prefs.edit().putString("last_seen_donation", latestDonationTime).apply();

                            runOnUiThread(() -> {
                                Toast toast = Toast.makeText(ProfileActivity.this, "Someone just donated to you!", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP | Gravity.END, 30, 300);
                                toast.show();
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
