package com.example.collegeproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.collegeproject.helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        if(databaseHelper.hasMatchStarted()){
            Intent intent = new Intent(getApplicationContext(), Inning.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Match not Started", Toast.LENGTH_SHORT).show();
        }
    }

    public void setTeam1(View view) {
        Intent intent = new Intent(getApplicationContext(), SetTeam.class);
        intent.putExtra("id", 1);
        startActivity(intent);
    }

    public void setTeam2(View view) {
        Intent intent = new Intent(getApplicationContext(), SetTeam.class);
        intent.putExtra("id", 2);
        startActivity(intent);
    }

    public void startMatch(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        if(databaseHelper.isTeamsSet()){
            Intent intent = new Intent(getApplicationContext(), SetToss.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Teams are not set", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetTeams(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.resetDatabase();
    }
}
