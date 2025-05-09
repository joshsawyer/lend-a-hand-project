package com.example.lendahand;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private final ArrayList<UserScore> leaderboardList;

    public LeaderboardAdapter(ArrayList<UserScore> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }

    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeaderboardAdapter.ViewHolder holder, int position) {
        UserScore user = leaderboardList.get(position);

        holder.nameText.setText(user.name);
        holder.scoreText.setText(String.valueOf(user.score));
        holder.rankText.setText(String.valueOf(position + 1));

        // Set background color based on position
        switch (position) {
            case 0:
                holder.cardView.setCardBackgroundColor(Color.parseColor("#FFD54F")); // Gold
                break;
            case 1:
                holder.cardView.setCardBackgroundColor(Color.parseColor("#BDBDBD")); // Silver
                break;
            case 2:
                holder.cardView.setCardBackgroundColor(Color.parseColor("#A1887F")); // Bronze
                break;
            default:
                holder.cardView.setCardBackgroundColor(Color.parseColor("#F5F5F5")); // Default
                break;
        }
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText, scoreText, rankText;
        public ImageView avatarIcon;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.requestorName);
            scoreText = itemView.findViewById(R.id.scoreText);
            rankText = itemView.findViewById(R.id.rankText);
            avatarIcon = itemView.findViewById(R.id.avatarIcon);
            cardView = itemView.findViewById(R.id.leaderboardCard);
        }
    }
}