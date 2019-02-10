package com.example.collegeproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegeproject.helper.DatabaseHelper;
import com.example.collegeproject.model.Player;

import java.util.ArrayList;

public class SelectBatsman extends AppCompatActivity {
    int inning;
    TextView header;
    ListView listView;
    Cursor cursor;
    DatabaseHelper databaseHelper;
    ArrayList<Player> playersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_batsman);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        listView = findViewById(R.id.selectBatsmanList);

        inning = getIntent().getExtras().getInt("inning");
        header = findViewById(R.id.selectBatsmanHeader);

        int nextPosition = databaseHelper.getNewBatsmanPosition(inning);
        if(nextPosition > 11){
            databaseHelper.declareInning(inning);
            Intent intent = new Intent(getApplicationContext(), Inning.class);
            startActivity(intent);
        }

        header.setText("Select Batsamn at no " + nextPosition);
        Toast.makeText(getApplicationContext(), inning + "", Toast.LENGTH_SHORT);

        cursor = databaseHelper.getAllBatsmans(inning);
        while (cursor.moveToNext()){
            playersList.add(new Player(
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_PLAYER_TEAM)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PLAYER_NAME))
            ));
        }

        ListBatsmanCustomAdaptor listBatsmanCustomAdaptor = new ListBatsmanCustomAdaptor(getApplicationContext(), R.layout.batsman_list_list_view, playersList);
        listView.setAdapter(listBatsmanCustomAdaptor);
    }
}
