package com.example.lendahand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lendahand.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ArrayList<User> userList = new ArrayList<>();

        Button b = findViewById(R.id.button);


        /*CONNECTING TO SERVER*/
        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2864063/get_requests.php";

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
                            String fullName = obj.getString("User_FName") + " " + obj.getString("User_LName");
                            int amount = obj.getInt("Amount_Requested");
                            int resourceID = obj.getInt("Resource_ID");
                            int requestID = obj.getInt("Request_ID");
                            String resourceName = obj.getString("Resource_Name");
                            String requestBio = obj.getString("Request_Bio");

                            userList.add(new User(fullName, amount, 0));/*, resourceID, requestID, resourceName, requestBio));*/
                        }

                        runOnUiThread(() -> { /*Update recycler view*/
                            RecyclerView courseRV = findViewById(R.id.donationsRecyclerView);
                            UserAdapter userAdapter = new UserAdapter(userList, user -> {
                                /*When a user is clicked this goes to DonateActivity where the user's name will be sent to that activity*/
                                Intent intent = new Intent(HomeActivity.this, DonateActivity.class);
                                intent.putExtra("username", user.getName());
                                intent.putExtra("amount", user.getAmountRequested());
                                intent.putExtra("resource", user.getResourceName());
                                intent.putExtra("bio", user.getRequestBio());
                                intent.putExtra("requestID", user.getRequestID());
                                startActivity(intent);
                            });

                            courseRV.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                            courseRV.setAdapter(userAdapter);
                        });
//
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


//        RecyclerView courseRV = findViewById(R.id.donationsRecyclerView);
//
//        // Here, we have created new array list and added data to it
////        ArrayList<User> userArrayList = new ArrayList<User>();
////        userArrayList.add(new User("Kurt Wagner", 7, 43));
////        userArrayList.add(new User("John Cena", 5, 70));
////        userArrayList.add(new User("John Doe", 5, 30));
////        userArrayList.add(new User("Pravesh", 5, 10));
//
//        UserAdapter userAdapter = new UserAdapter(userList, user -> {
//            // Example action: go to ProfileActivity
//            Intent intent = new Intent(HomeActivity.this, DonateActivity.class);
//            intent.putExtra("username", user.getName());
//            startActivity(intent);
//        });
//
//        // we are initializing our adapter class and passing our arraylist to it.
//
//
//        // below line is for setting a layout manager for our recycler view.
//        // here we are creating vertical list so we will provide orientation as vertical
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//
//        // in below two lines we are setting layoutmanager and adapter to our recycler view.
//        courseRV.setLayoutManager(linearLayoutManager);
//        courseRV.setAdapter(userAdapter);
//

    }
}
