package com.example.lendahand;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Adapter to connect item_person_card to RecyclerView
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final ArrayList<User> userList;
    private final OnUserClickListener listener;

    // Listener interface for clicks
    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    // Adapter constructor with click listener
    public UserAdapter(ArrayList<User> userList, OnUserClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.itemsRequestedTextView.setText(user.getItemsRequested() + " items requested");
        holder.progressBar.setProgress(user.getProgress());

        // Make entire card clickable
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder for the card views
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