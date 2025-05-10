package com.example.lendahand;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestItemAdapter extends RecyclerView.Adapter<RequestItemAdapter.ViewHolder>{
    private ArrayList<RequestItem> requestItemList;

    public RequestItemAdapter(ArrayList<RequestItem> requestItemList) {
        this.requestItemList = requestItemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, requestInfo, requestBio;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            requestInfo = itemView.findViewById(R.id.requestInfo);
            progressBar = itemView.findViewById(R.id.progressBar);
            requestBio = itemView.findViewById(R.id.requestBio);
        }
    }

    @NonNull
    @Override
    public RequestItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donation_item_card, parent, false);
        return new RequestItemAdapter.ViewHolder(v);
    }

    @Override 
    public void onBindViewHolder(@NonNull RequestItemAdapter.ViewHolder holder, int position) {
        RequestItem item = requestItemList.get(position);
        holder.itemName.setText(item.getItemName());
        holder.requestInfo.setText(item.getRequested() + " Requested");
        holder.progressBar.setMax(item.getRequested());
        holder.progressBar.setProgress(item.getReceived());
        holder.requestBio.setText(item.getRequestBio());
    }

    @Override
    public int getItemCount() {
        return requestItemList.size();
    }
}
