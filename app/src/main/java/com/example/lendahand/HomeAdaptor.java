package com.example.lendahand;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.ArrayList;


public class HomeAdaptor extends RecyclerView.Adapter<HomeAdaptor.ViewHolder> {

    private final ArrayList<HomeItem> homeItemList;
    private final OnUserClickListener listener;


    public interface OnUserClickListener {
        void onUserClick(HomeItem homeItem);
    }

    public HomeAdaptor(ArrayList<HomeItem> homeItemList, OnUserClickListener listener) {
        this.homeItemList = homeItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomeAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdaptor.ViewHolder holder, int position) {
        HomeItem homeItem = homeItemList.get(position);
        holder.nameTextView.setText(homeItem.getFullName());
        holder.itemsRequestedTextView.setText(homeItem.getAmountRequested() + " items requested");
        holder.progressBar.setProgress(homeItem.getPercentReceived());
        holder.userLocation.setText(homeItem.getUserLocation());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(homeItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView itemsRequestedTextView;
        private final ProgressBar progressBar;
        private final TextView userLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            itemsRequestedTextView = itemView.findViewById(R.id.itemsRequestedTextView);
            progressBar = itemView.findViewById(R.id.progressBar);
           userLocation = itemView.findViewById(R.id.userLocation);
        }
    }
}