package com.example.collegeproject;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegeproject.helper.DatabaseHelper;

public class Inning extends AppCompatActivity {
    int inning;
    RadioButton batsman1Radio, batsman2Radio;
    TextView bowlerView, overPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inning);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

        if(!databaseHelper.isInningDeclared(1)){
            inning = 1;
        }
        else if(!databaseHelper.isInningDeclared(2)){
            inning = 2;
        }
        else{

        }

        int limit, balls, runs, overBalls;
        double overs;
        overPanel = findViewById(R.id.overPanel);

        Cursor scoreCursor = databaseHelper.getInningScore(inning);
        scoreCursor.moveToNext();

        limit = scoreCursor.getInt(scoreCursor.getColumnIndex(DatabaseHelper.COLUMN_INNING_OVERS));
        balls = scoreCursor.getInt(scoreCursor.getColumnIndex(DatabaseHelper.COLUMN_INNING_BALLS));

        overBalls = balls % 6;
        overs = Math.ceil(balls/6);
        overPanel.setText(overs + "." + overBalls + " (" + limit + ")");

        Cursor cursorBatsmans = databaseHelper.getCurrentBatsmans(inning);
        if(cursorBatsmans.getCount() < 2){
            Intent intent = new Intent(getApplicationContext(), SelectBatsman.class);
            intent.putExtra("inning", inning);
            startActivity(intent);
        }

        Cursor cursorBowlers = databaseHelper.getCurrentBowlers(inning);
        if(cursorBowlers.getCount() < 1){
            Intent intent = new Intent(getApplicationContext(), SelectBowler.class);
            intent.putExtra("inning", inning);
            startActivity(intent);
        }

        batsman1Radio = findViewById(R.id.scoreBatsman1);
        batsman2Radio = findViewById(R.id.scoreBatsman2);
        bowlerView = findViewById(R.id.bowlerView);

        cursorBatsmans.moveToNext();
        batsman1Radio.setText(databaseHelper.getPlayerName(cursorBatsmans.getInt(cursorBatsmans.getColumnIndex(DatabaseHelper.COLUMN_BATSMAN_PLAYER))));
        cursorBatsmans.moveToNext();
        batsman2Radio.setText(databaseHelper.getPlayerName(cursorBatsmans.getInt(cursorBatsmans.getColumnIndex(DatabaseHelper.COLUMN_BATSMAN_PLAYER))));

        cursorBowlers.moveToNext();
        bowlerView.setText(databaseHelper.getPlayerName(cursorBowlers.getInt(cursorBowlers.getColumnIndex(DatabaseHelper.COLUMN_BOWLER_PLAYER))));

    }

    public void changeBowler(View view) {
        Intent intent = new Intent(getApplicationContext(), SelectBowler.class);
        intent.putExtra("inning", inning);
        startActivity(intent);
    }

    public void submitBall(View view) {
        Toast.makeText(getApplicationContext(), "Submit Button Clicked", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
    }
}
