package com.example.lendahand;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;

public class DonatedItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private DonationItemAdapter adapter;
    private ArrayList<DonationItem> donatedItems;
    private TextView noDonationTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donated, container, false);

        recyclerView = view.findViewById(R.id.donatedRecyclerView);
        noDonationTextView = view.findViewById(R.id.noDonationTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        donatedItems = new ArrayList<>();
        adapter = new DonationItemAdapter(donatedItems);
        recyclerView.setAdapter(adapter);

        // Get userId from arguments
        String userId = getArguments() != null ? getArguments().getString("userId", "-1") : "-1";

        fetchDonatedItems(userId);

        return view;
    }

    public void fetchDonatedItems(String userId){

        String url = "https://lamp.ms.wits.ac.za/home/s2864063/user_donated_items.php?userID=" + userId;

        Request request = new Request.Builder().url(url).build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Log or toast error on UI if needed
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();  // this is the raw JSON string

                    try {
                        JSONObject userDetails = new JSONObject(responseBody);
                        JSONArray donatedItemsArray = userDetails.getJSONArray("DonatedItems");

                        ArrayList<DonationItem> donationItems = parseDonationItems(userDetails.getJSONArray("DonatedItems"));


                        // Now you have a list of DonationItem objects
                        requireActivity().runOnUiThread(() -> {
                            adapter.setItems(donationItems);  // update your adapter with new data
                            adapter.notifyDataSetChanged();   // refresh the RecyclerView
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    private ArrayList<DonationItem> parseDonationItems(JSONArray donatedItemsArray) throws JSONException {
        ArrayList<DonationItem> list = new ArrayList<>();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        if (donatedItemsArray.length() == 0) {
            mainHandler.post(() -> {
                noDonationTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            });
        }
        else{
            for (int i = 0; i < donatedItemsArray.length(); i++) {
                JSONObject item = donatedItemsArray.getJSONObject(i);

                String name = item.getString("Resource_Name");
                int quantity = item.getInt("Amount_Donated");
                String date = item.getString("Date_Donated");
                String userReceived = item.getString("User_FName") + " " + item.getString("User_LName");

                DonationItem donation = new DonationItem(name, quantity, userReceived, date);
                list.add(donation);
            }
        }

        return list;
    }


}
