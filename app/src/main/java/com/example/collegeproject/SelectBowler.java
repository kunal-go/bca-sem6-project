package com.example.collegeproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegeproject.helper.DatabaseHelper;
import com.example.collegeproject.model.Player;

import java.util.ArrayList;

public class SelectBowler extends AppCompatActivity {
    int inning;
    ListView listView;
    Cursor cursor;
    DatabaseHelper databaseHelper;
    ArrayList<Player> playersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bowler);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        listView = findViewById(R.id.selectBowlerList);

        inning = getIntent().getExtras().getInt("inning");

        cursor = databaseHelper.getAllBowlers(inning);
        while (cursor.moveToNext()){
            playersList.add(new Player(
                    cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                    cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_PLAYER_TEAM)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PLAYER_NAME))
            ));
        }

        ListBatsmanCustomAdaptor listBatsmanCustomAdaptor = new ListBatsmanCustomAdaptor(getApplicationContext(), R.layout.batsman_list_list_view, playersList);
        listView.setAdapter(listBatsmanCustomAdaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                if(databaseHelper.addBowler(inning, cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)))){
//                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Inning.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SelectBowler.this, "Bowler not added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
