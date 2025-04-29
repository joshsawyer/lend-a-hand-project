package com.example.lendahand;


import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home"); // Set the title you want
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // Show back button
        }

        // Make the back button actually go back
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        RecyclerView courseRV = findViewById(R.id.donationsRecyclerView);

        // Here, we have created new array list and added data to it
        ArrayList<User> userArrayList = new ArrayList<User>();
        userArrayList.add(new User("Kurt Wagner", 7, 43));
        userArrayList.add(new User("John Cena", 5, 70));
        userArrayList.add(new User("John Doe", 5, 30));
        userArrayList.add(new User("Pravesh", 5, 10));

        // we are initializing our adapter class and passing our arraylist to it.
        UserAdapter userAdapter = new UserAdapter(userArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(userAdapter);

    }
}
