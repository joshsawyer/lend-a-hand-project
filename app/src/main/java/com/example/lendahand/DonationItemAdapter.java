package com.example.lendahand;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DonationItemAdapter extends RecyclerView.Adapter<DonationItemAdapter.ViewHolder> {

    private ArrayList<DonationItem> donationItemList;

    public DonationItemAdapter(ArrayList<DonationItem> donationItemList) {
        this.donationItemList = donationItemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, requestInfo;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            requestInfo = itemView.findViewById(R.id.requestInfo);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    @NonNull
    @Override
    public DonationItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donation_item_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonationItem item = donationItemList.get(position);
        holder.itemName.setText(item.getItemName());
        holder.requestInfo.setText(item.getRequested() + " Requested");
        holder.progressBar.setMax(item.getRequested());
        holder.progressBar.setProgress(item.getCurrent());
    }

    @Override
    public int getItemCount() {
        return donationItemList.size();
    }
}
