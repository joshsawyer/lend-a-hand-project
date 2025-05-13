package com.example.lendahand;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProfileActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private Button requestItemButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("LendAHandPrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1"); // Default is -1 if not set

        if (userId != "-1") {
            // User is logged in
        } else {
            // No user found, maybe redirect to LoginActivity
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Hook views
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        requestItemButton = findViewById(R.id.requestItemButton);

        // Set up ViewPager2 adapter
        viewPager.setAdapter(new ProfilePagerAdapter(this));

        // Attach TabLayout to ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Donated");
            else tab.setText("Requested");
        }).attach();

        // Request item button → open RequestActivity
        requestItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, RequestActivity.class);
            startActivity(intent);
        });
    }

    // Pager Adapter for tabs
    private static class ProfilePagerAdapter extends FragmentStateAdapter {
        public ProfilePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new DonatedItemsFragment();
            } else {
                return new RequestedItemsFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}