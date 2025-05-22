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
        TextView itemName, requestInfo, userRecieved, dateRequested;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            requestInfo = itemView.findViewById(R.id.DonationInfo);
            progressBar = itemView.findViewById(R.id.progressBar);
            userRecieved = itemView.findViewById(R.id.userRecieved);
            dateRequested = itemView.findViewById(R.id.dateRequested);
        }
    }

    @NonNull
    @Override
    public RequestItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item_card, parent, false);
        return new RequestItemAdapter.ViewHolder(v);
    }

    @Override 
    public void onBindViewHolder(@NonNull RequestItemAdapter.ViewHolder holder, int position) {
        RequestItem item = requestItemList.get(position);
        holder.itemName.setText(item.getItemName());
        String r = item.getRequested() + " Requested \n"+ (item.getRequested()-item.getReceived()) + " More Needed";
        holder.requestInfo.setText(r);
        holder.progressBar.setMax(item.getRequested());
        holder.progressBar.setProgress(item.getReceived());
        holder.userRecieved.setText(item.getRequestBio());
        // Split the date to remove time, assuming format is "YYYY-MM-DD HH:MM:SS"
        String[] dateParts = item.getDateRequested().split(" ");
        String justDate = dateParts.length > 0 ? dateParts[0] : item.getDateRequested();
        String p = "At " + justDate;
        holder.dateRequested.setText(p);


    }

    @Override
    public int getItemCount() {
        return requestItemList.size();
    }
}
