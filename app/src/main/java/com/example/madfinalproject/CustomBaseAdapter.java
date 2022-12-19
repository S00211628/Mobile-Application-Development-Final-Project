package com.example.madfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> listUserNames;
    ArrayList<String> listScore;
    ArrayList<String> listDates;
    int listProfilePictures[];
    LayoutInflater inflater;


    public CustomBaseAdapter(Context ctx, ArrayList<String> username, ArrayList<String> Score, ArrayList<String> gameDate, int[] ProfilePictures){
        this.context = ctx;
        this.listUserNames = username;
        this.listScore = Score;
        this.listDates = gameDate;
        this.listProfilePictures = ProfilePictures;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return listUserNames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.leaderboard_list_item, null);
        TextView tvUsername = (TextView) view.findViewById(R.id.tvUserName);
        TextView tvScore = (TextView) view.findViewById(R.id.tvScore);
        ImageView ivpp = (ImageView) view.findViewById(R.id.ivpp);

        tvUsername.setText(listUserNames.get(i));
        tvScore.setText("Score : "+ listScore.get(i) + " | Date: " + listDates.get(i));
        ivpp.setImageResource(listProfilePictures[i]);
        return view;
    }
}
