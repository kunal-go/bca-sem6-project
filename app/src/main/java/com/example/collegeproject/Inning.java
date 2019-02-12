package com.example.collegeproject;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegeproject.helper.DatabaseHelper;

public class Inning extends AppCompatActivity {
    int inning, batsman1, batsman2, bowler;
    TextView bowlerView, overPanel, scorePanel, teamNamePanel;

    RadioButton batsman1Radio, batsman2Radio;
    RadioButton deliveryLegal, deliveryWide, deliveryNoBall;
    RadioButton runsBat, runsBye, runsLegBye;
    RadioButton wicketNo, wicketNormal, wicketRunoutStriker, wicketRunOutNonStriker, wicketDeclare;

    EditText textRuns;

    /*
        Deliveries:
           1: Legal
           2: Wide
           3: No Ball

        Runs Through:
            1: Bat
            2: Bye
            3: Leg Bye

        Wicket
            1: No Wicket
            2: Bowled / Caught / Stumping
            3: Run Out (Striker)
            4: Run Out (Non-Striker)
            5: Declare
    */


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
            Toast.makeText(getApplicationContext(), "Match Finished", Toast.LENGTH_SHORT).show();
        }

//        Toast.makeText(getApplicationContext(), inning + "", Toast.LENGTH_SHORT).show();

        try {
            int limit, balls, runs, wickets, overBalls, overs;

            overPanel = findViewById(R.id.overPanel);
            scorePanel = findViewById(R.id.scorePanel);
            teamNamePanel = findViewById(R.id.battingTeamName);

            try{
                String teamName = databaseHelper.getBattingTeamName(inning);
                teamNamePanel.setText(teamName + " (Inning " + inning + ")");
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "Fetching Team Name Failed : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            try{
                Cursor scoreCursor = databaseHelper.getInningScore(inning);
                if(scoreCursor.getCount() > 0) {
                    if(scoreCursor.moveToNext()) {
                        limit = scoreCursor.getInt(scoreCursor.getColumnIndex(DatabaseHelper.COLUMN_INNING_OVERS));
                        balls = scoreCursor.getInt(scoreCursor.getColumnIndex(DatabaseHelper.COLUMN_INNING_BALLS));

                        overBalls = balls % 6;
                        overs = (balls - overBalls) / 6;
                        overPanel.setText(overs + "." + overBalls + " (" + limit + ")");

                        runs = scoreCursor.getInt(scoreCursor.getColumnIndex(DatabaseHelper.COLUMN_INNING_RUNS));
                        wickets = scoreCursor.getInt(scoreCursor.getColumnIndex(DatabaseHelper.COLUMN_INNING_WICKETS));

                        scorePanel.setText(runs + " - " + wickets);

                        if ((limit * 6) <= balls) {
                            Toast.makeText(getApplicationContext(), "Terminate Innings", Toast.LENGTH_LONG).show();
                            //            databaseHelper.declareInning(inning);
                            //            restartActivity();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No Score Found", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "No Score Found", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "Score Panel : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            Cursor cursorBatsmans = databaseHelper.getCurrentBatsmans(inning);
            if (cursorBatsmans.getCount() < 2) {
                Intent intent = new Intent(getApplicationContext(), SelectBatsman.class);
                intent.putExtra("inning", inning);
                startActivity(intent);
            }

            Cursor cursorBowlers = databaseHelper.getCurrentBowlers(inning);
            if (cursorBowlers.getCount() < 1) {
                Intent intent = new Intent(getApplicationContext(), SelectBowler.class);
                intent.putExtra("inning", inning);
                startActivity(intent);
            }

            if (cursorBatsmans.getCount() == 2 && cursorBowlers.getCount() == 1) {
                batsman1Radio = findViewById(R.id.scoreBatsman1);
                batsman2Radio = findViewById(R.id.scoreBatsman2);
                bowlerView = findViewById(R.id.bowlerView);

                cursorBatsmans.moveToNext();
                batsman1Radio.setText(databaseHelper.getPlayerName(cursorBatsmans.getInt(cursorBatsmans.getColumnIndex(DatabaseHelper.COLUMN_BATSMAN_PLAYER))));
                batsman1 = cursorBatsmans.getInt(cursorBatsmans.getColumnIndex(DatabaseHelper.COLUMN_ID));
                cursorBatsmans.moveToNext();
                batsman2Radio.setText(databaseHelper.getPlayerName(cursorBatsmans.getInt(cursorBatsmans.getColumnIndex(DatabaseHelper.COLUMN_BATSMAN_PLAYER))));
                batsman2 = cursorBatsmans.getInt(cursorBatsmans.getColumnIndex(DatabaseHelper.COLUMN_ID));

                cursorBowlers.moveToNext();
                bowlerView.setText(databaseHelper.getPlayerName(cursorBowlers.getInt(cursorBowlers.getColumnIndex(DatabaseHelper.COLUMN_BOWLER_PLAYER))));
                bowler = cursorBowlers.getInt(cursorBowlers.getColumnIndex(DatabaseHelper.COLUMN_ID));

                deliveryLegal = findViewById(R.id.deliveryLegal);
                deliveryNoBall = findViewById(R.id.deliveryNoBall);
                deliveryWide = findViewById(R.id.deliveryWide);

                runsBat = findViewById(R.id.throughBat);
                runsBye = findViewById(R.id.throughBye);
                runsLegBye = findViewById(R.id.throughLegBye);

                wicketNo = findViewById(R.id.wicketNoWicket);
                wicketNormal = findViewById(R.id.wicketBowled);
                wicketRunoutStriker = findViewById(R.id.wicketRunOutStriker);
                wicketRunOutNonStriker = findViewById(R.id.wicketRunOutNonStriker);
                wicketDeclare = findViewById(R.id.wicketDeclare);

                textRuns = findViewById(R.id.editTextRuns);
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public int getDelivery(){
        if(deliveryLegal.isChecked()){
            return 1;
        }
        else if(deliveryWide.isChecked()){
            return 2;
        }
        else if(deliveryNoBall.isChecked()){
            return 3;
        }
        return 1;
    }

    public int getRunsThrough(){
        if(runsBat.isChecked()){
            return 1;
        }
        else if(runsBye.isChecked()){
            return 2;
        }
        else if(runsLegBye.isChecked()){
            return 3;
        }
        return 1;
    }

    public int getWicket(){
        if(wicketNo.isChecked()){
            return 1;
        }
        else if(wicketNormal.isChecked()){
            return 2;
        }
        else if(wicketRunoutStriker.isChecked()){
            return 3;
        }
        else if(wicketRunOutNonStriker.isChecked()){
            return 4;
        }
        else if(wicketDeclare.isChecked()){
            return 5;
        }
        return 1;
    }

    public int getStriker(){
        if(batsman1Radio.isChecked()){
            return batsman1;
        }
        else{
            return batsman2;
        }
    }

    public int getNonStriker(){
        if(batsman1Radio.isChecked()){
            return batsman2;
        }
        else{
            return batsman1;
        }
    }

    public void changeBowler(View view) {
        Intent intent = new Intent(getApplicationContext(), SelectBowler.class);
        intent.putExtra("inning", inning);
        startActivity(intent);
    }

    public void submitBall(View view) {
        int runs;

        try {
            runs = Integer.parseInt(textRuns.getText().toString().trim());
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), "Invalid Runs", Toast.LENGTH_SHORT).show();
            return;
        }

        if(runs >= 0){
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
            databaseHelper.submitBall(inning, getStriker(), getNonStriker(), bowler, getDelivery(), getRunsThrough(), getWicket(), runs);
        }
        else {
            Toast.makeText(getApplicationContext(), "Invalid Runs", Toast.LENGTH_SHORT).show();
            return;
        }
        restartActivity();
    }

    public void restartActivity(){
        finish();
        startActivity(getIntent());
    }

    public void resetMatch(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.resetDatabase();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
