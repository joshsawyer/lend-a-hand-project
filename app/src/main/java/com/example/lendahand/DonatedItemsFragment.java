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

public class DonatedItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private DonationItemAdapter adapter;
    private ArrayList<DonationItem> donatedItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donated, container, false);

        recyclerView = view.findViewById(R.id.donatedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Dummy data
        donatedItems = new ArrayList<>();
        donatedItems.add(new DonationItem("Blankets", 5, "Josh", "yesterday"));
        donatedItems.add(new DonationItem("Water Bottles", 10, "Kobe", "yesterday"));

        adapter = new DonationItemAdapter(donatedItems);
        recyclerView.setAdapter(adapter);

        return view;
    }
}