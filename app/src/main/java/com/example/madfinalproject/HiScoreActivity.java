package com.example.madfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HiScoreActivity extends AppCompatActivity {


    Button btnQuit;
    Button btnPlayAgain;

    ListView listView;
    String UserName;
    int profilePictures [] = {R.drawable.pp1,R.drawable.pp2,R.drawable.pp3,R.drawable.pp4,R.drawable.pp5,R.drawable.pp6};
    ListView highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hiscore_activity);

        btnQuit = findViewById(R.id.btnQuit);
        btnQuit.setText("Quit");
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnPlayAgain.setText("Return");

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HiScoreActivity.this, MainActivity.class));
            }
        });

        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HiScoreActivity.this, Game_Screen.class));
            }
        });

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        Bundle extras = getIntent().getExtras();
        DatabaseHandler db = new DatabaseHandler(this);


        if (extras != null){
            String userName = extras.getString("UserName");
            int score = extras.getInt("Score");

            Log.v("UserName", userName);
            Log.v("Score", Integer.toString(score));
            Log.v("Date", formattedDate);
            db.addHiScore(new HiScore(formattedDate, userName, score));

        }

        listView = findViewById(R.id.list);

//        db.emptyHiScores();


        // Calling SQL statement
        List<HiScore> top5HiScores = db.getTopFiveScores();
        top5HiScores = db.getTopFiveScores();

        ArrayList<String> usernames = new ArrayList();
        ArrayList<String> scores = new ArrayList();
        ArrayList<String> dates = new ArrayList();
        for (HiScore hs : top5HiScores){
            usernames.add(hs.getPlayer_name());
            scores.add(String.valueOf(hs.getScore()));
            dates.add(hs.getGame_date());
        }

        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), usernames, scores, dates, profilePictures);
        listView.setAdapter(customBaseAdapter);

    }

}
