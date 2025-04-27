package com.example.lendahand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

//This class connects the item_person_card to the homepage

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final ArrayList<User> userList;

    public UserAdapter(ArrayList<User> userList){
        this.userList = userList;
    }


    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        User user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.itemsRequestedTextView.setText(String.valueOf(user.getItemsRequested()) + " items requested");
        holder.progressBar.setProgress(user.getProgress());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return userList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView itemsRequestedTextView;
        private final ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            itemsRequestedTextView = itemView.findViewById(R.id.itemsRequestedTextView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
