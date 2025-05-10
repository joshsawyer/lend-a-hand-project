package com.example.lendahand;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class RequestedItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private DonationItemAdapter adapter;
    private ArrayList<DonationItem> requestedItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requested, container, false);

        recyclerView = view.findViewById(R.id.requestedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Dummy data
        requestedItems = new ArrayList<>();
        requestedItems.add(new DonationItem("Canned Beans", 5, 1, "I went hungry for a week. no food only stompted on sweets from the floor."));
        requestedItems.add(new DonationItem("Tinned Fish", 7, 2, "I went hungry for a week. no food only stompted on sweets from the floor."));

        adapter = new DonationItemAdapter(requestedItems);
        recyclerView.setAdapter(adapter);

        return view;
    }
}