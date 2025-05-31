package com.example.lendahand;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import okhttp3.Request;
import okhttp3.Response;

public class RequestedItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestItemAdapter adapter;
    private ArrayList<RequestItem> requestedItems;
    private TextView noDataText;

    private static final String BASE_URL = "https://lamp.ms.wits.ac.za/home/s2864063/user_requested_items.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requested, container, false);

        SharedPreferences prefs = requireContext().getSharedPreferences("LendAHandPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");

        recyclerView = view.findViewById(R.id.requestedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        noDataText = view.findViewById(R.id.noDataTextView);

        requestedItems = new ArrayList<>();
        adapter = new RequestItemAdapter(requestedItems);
        recyclerView.setAdapter(adapter);

        fetchRequestedItems(userId);

        return view;
    }

    private void fetchRequestedItems(String userId) {
        OkHttpClient client = new OkHttpClient();

        String url = BASE_URL + "?userID=" + userId;


        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(Looper.getMainLooper());

            @Override
            public void onFailure(@NonNull Call call,@NonNull IOException e) {
                mainHandler.post(() ->
                        Toast.makeText(getContext(), "Failed to load requests", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call,@NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    mainHandler.post(() ->
                            Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                String jsonResponse = response.body().string();
                Log.d("RequestedItems", "Raw JSON response: " + jsonResponse);

                try {
                    if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                        throw new JSONException("Empty or null JSON");
                    }

                    JSONArray jsonArray = new JSONArray(jsonResponse);
                    ArrayList<RequestItem> tempList = new ArrayList<>();

                    if (jsonArray.length() == 0) {
                        mainHandler.post(() -> {
                            noDataText.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        });
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            String itemName = obj.getString("itemName");
                            int requested = obj.getInt("requested");
                            int received = obj.getInt("received");
                            String requestBio = obj.getString("requestBio");
                            String dateRequested = obj.getString("dateRequested");
                            int requestID = obj.getInt("requestID");

                            RequestItem item = new RequestItem(itemName, requested, received, requestBio, dateRequested, requestID);
                            tempList.add(item);
                        }

                        mainHandler.post(() -> {
                            noDataText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            requestedItems.clear();
                            requestedItems.addAll(tempList);
                            adapter.notifyDataSetChanged();
                        });
                    }

                } catch (JSONException e) {
                    Log.e("JSONError", "Parsing error: " + e.getMessage());
                    mainHandler.post(() ->
                            Toast.makeText(getContext(), "JSON Parsing error", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}

