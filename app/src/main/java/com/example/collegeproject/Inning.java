package com.example.collegeproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.collegeproject.helper.DatabaseHelper;

public class Inning extends AppCompatActivity {
    int inning;

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

        Cursor cursorBatsmans = databaseHelper.getCurrentBatsmans(inning);
        if(cursorBatsmans.getCount() < 2){
            Intent intent = new Intent(getApplicationContext(), SelectBatsman.class);
            intent.putExtra("inning", inning);
            startActivity(intent);
        }
    }
}
