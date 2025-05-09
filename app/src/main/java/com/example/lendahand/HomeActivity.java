package com.example.lendahand;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
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


        ArrayList<HomeItem> homeItemList = new ArrayList<>();


        /*CONNECTING TO SERVER*/
        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2864063/get_homeitem.php";

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
                            String fullName = obj.getString("Full_Name");
                            int amountRequested = obj.getInt("Total_Requested");
                            String userID = obj.getString("User_ID");
                            int percentReceived = obj.getInt("Percentage_Received");

                            homeItemList.add(new HomeItem(fullName, amountRequested, percentReceived, userID));
                        }

                        runOnUiThread(() -> { /*Update recycler view*/
                            RecyclerView courseRV = findViewById(R.id.donationsRecyclerView);
                            HomeAdaptor userAdapter = new HomeAdaptor(homeItemList, homeItem -> {
                                /*When a user is clicked this goes to DonateActivity where the user's name will be sent to that activity*/
                                Intent intent = new Intent(HomeActivity.this, DonateActivity.class);
                                intent.putExtra("userID", homeItem.getUserID());
                                intent.putExtra("fullName", homeItem.getFullName());

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
